package me.pandelis.shush.activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import me.pandelis.shush.R
import me.pandelis.shush.classes.AppDatabase
import me.pandelis.shush.classes.ShushAPI
import me.pandelis.shush.models.DbContact
import me.pandelis.shush.models.UpdateProfile
import me.pandelis.shush.models.UserProfileResponse
import me.pandelis.shush.services.ShushService
import me.pandelis.shush.utils.DbWorkerThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddContact : AppCompatActivity(), View.OnClickListener {

    private lateinit var publicKeyInput: EditText
    private var API: ShushService? = null
    private var DB: AppDatabase? = null
    private lateinit var mDbWorkerThread: DbWorkerThread
    private val mUiHandler = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)


        publicKeyInput = findViewById(R.id.add_contact_input)

        val submitButton = findViewById<Button>(R.id.add_contact_add_button)
        submitButton.setOnClickListener(this)

        // add back arrow to toolbar
        if (supportActionBar != null){
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }

        mDbWorkerThread = DbWorkerThread("dbWorkerThread")
        mDbWorkerThread.start()

        DB = AppDatabase.getInstance(this)
        API = ShushAPI.getInstance()
    }

    override  fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.itemId === android.R.id.home) {
            finish() // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item)
    }

    fun addUserIfExists() {

        val publicKey = publicKeyInput.text.toString()

        if(publicKey != null && publicKey != "") {

            API?.user(UpdateProfile(name = null, publicKey = publicKey))?.enqueue(object: Callback<UserProfileResponse> {
                override fun onFailure(call: Call<UserProfileResponse>?, t: Throwable?) {
                    handleProfileRetrieval(false, null)
                }

                override fun onResponse(call: Call<UserProfileResponse>?, response: Response<UserProfileResponse>?) {
                    if (response!!.isSuccessful) {
                        handleProfileRetrieval(true, response.body())
                    } else {
                        handleProfileRetrieval(false, null)
                    }
                }
            })
        }

    }

    fun handleProfileRetrieval(successful: Boolean, profile: UserProfileResponse?) {
        val i = Intent(this, ChatListActivity::class.java)
        if (successful) {

            if(profile?.name != null && profile?.id != null ) {
                i.putExtra("CONTACT_ADDED_SUCCESSFULLY", true)

                val contact = DbContact(profile.name, profile.id, null)

                val task = Runnable {
                    DB?.contactDao()?.add(contact)
                }
                mDbWorkerThread.postTask(task)
            } else {
                i.putExtra("CONTACT_ADDED_SUCCESSFULLY", false)
            }
        } else {
            i.putExtra("CONTACT_ADDED_SUCCESSFULLY", false)
        }

        this.startActivity(i)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.add_contact_add_button -> {
                addUserIfExists()
            }
        }
    }



    companion object {
        fun open(context: Context) {
            context.startActivity(Intent(context, AddContact::class.java))
        }
    }
}
