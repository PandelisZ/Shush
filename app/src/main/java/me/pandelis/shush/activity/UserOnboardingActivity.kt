package me.pandelis.shush.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.TextView
import me.pandelis.shush.R
import me.pandelis.shush.classes.AppDatabase
import me.pandelis.shush.utils.DbWorkerThread
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.security.interfaces.RSAPrivateKey
import android.R.attr.publicKey
import android.app.Person
import me.pandelis.shush.models.Profile
import me.pandelis.shush.utils.savePrivateKey
import me.pandelis.shush.utils.savePublicKey
import java.security.*
import java.security.interfaces.RSAPublicKey


class UserOnboardingActivity : AppCompatActivity(), View.OnClickListener {

    private var DB: AppDatabase? = null
    private lateinit var mDbWorkerThread: DbWorkerThread
    private val mUiHandler = Handler()
    private lateinit var actionButton: Button
    private lateinit var titleText: TextView
    private lateinit var descriptorText: TextView
    private lateinit var keyTextArea: TextView
    private lateinit var privateKey: PrivateKey
    private lateinit var publicKey: PublicKey
    private lateinit var nameInput: TextView
    private var onboardingStep = 1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_onboarding)

        mDbWorkerThread = DbWorkerThread("dbWorkerThread")
        mDbWorkerThread.start()

        DB = AppDatabase.getInstance(this)

        val mGetStartedButton = findViewById<Button>(R.id.getStartedButton)
        mGetStartedButton.setOnClickListener(this)



    }

    fun generatePrivateKeyActivity() {
        setContentView(R.layout.activity_user_onboarding_generating)

        actionButton = findViewById(R.id.onboardingActionButton)
        titleText = findViewById(R.id.oboardingTitleText)
        descriptorText = findViewById(R.id.onboardingDescriptorText)
        keyTextArea = findViewById(R.id.onboardingKeyTextArea)
        nameInput = findViewById<TextView>(R.id.onboardingNameInput)


        titleText.text = resources.getText(R.string.generating_key_title)
        descriptorText.text =  resources.getText(R.string.generating_key_descriptor)
        actionButton.text = resources.getText(R.string.button_continue)
        actionButton.visibility = View.INVISIBLE
        keyTextArea.visibility = View.INVISIBLE

        actionButton.setOnClickListener(this)


        val keygen = KeyPairGenerator.getInstance("RSA")
        keygen.initialize(1024)
        val keyPair = keygen.generateKeyPair()
        privateKey = keyPair.private as RSAPrivateKey
        publicKey = keyPair.public as RSAPublicKey


        titleText.text = resources.getText(R.string.this_is_your_private_key_title)
        descriptorText.text =  resources.getText(R.string.this_is_your_private_key_descriptor)
        keyTextArea.text = savePrivateKey(privateKey)
        keyTextArea.visibility = View.VISIBLE
        actionButton.visibility = View.VISIBLE

    }

    private fun insertProfileInformation(profile: Profile) {
        val task = Runnable { DB?.profileDao()?.createProfile(profile) }
        mDbWorkerThread.postTask(task)
    }

    fun generatePublicKeyActivity() {

        titleText.text = resources.getText(R.string.this_is_your_public_key_title)
        descriptorText.text =  resources.getText(R.string.this_is_your_public_key_descriptor)
        keyTextArea.text = savePublicKey(publicKey)



    }

    fun askForNameActivity() {

        titleText.text = resources.getText(R.string.enter_your_name_title)
        descriptorText.text =  resources.getText(R.string.enter_your_name_desciptor)
        keyTextArea.visibility = View.INVISIBLE
        nameInput.visibility = View.VISIBLE

        actionButton.text = resources.getText(R.string.button_finish)

    }

    fun finishOnboarding() {
        val profile = Profile(
            publicKey = publicKey.encoded,
            privateKey = privateKey.encoded,
            name = nameInput.text.toString(),
            image = null
        )

        insertProfileInformation(profile)

        ChatListActivity.open(this)
    }

    fun moveToNextView() {
        if(onboardingStep == 1) {
            onboardingStep ++
            generatePublicKeyActivity()
        }
        if(onboardingStep == 2) {
            onboardingStep ++
            askForNameActivity()
        }
        if(onboardingStep == 3) {
            if(nameInput.text.toString() != "" && nameInput.text != null) {
                finishOnboarding()
            }

        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.getStartedButton -> {
                generatePrivateKeyActivity()
            }

            R.id.getStartedButton -> {
                moveToNextView()
            }
        }
    }

    companion object {
        fun open(context: Context) {
            context.startActivity(Intent(context, UserOnboardingActivity::class.java))
        }
    }

}