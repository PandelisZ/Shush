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
import android.widget.TextView
import me.pandelis.shush.R
import me.pandelis.shush.classes.AppDatabase
import me.pandelis.shush.classes.MyProfile
import me.pandelis.shush.classes.ShushAPI
import me.pandelis.shush.models.DbContact
import me.pandelis.shush.models.UpdateProfile
import me.pandelis.shush.models.UserProfileResponse
import me.pandelis.shush.services.ShushService
import me.pandelis.shush.utils.DbWorkerThread
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewKeyActivity : AppCompatActivity() {

    private lateinit var publicKeyInput: TextView
    private var DB: AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_key)


        publicKeyInput = findViewById(R.id.viewPublicKeyTextView)

        // add back arrow to toolbar
        if (supportActionBar != null){
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }

        DB = AppDatabase.getInstance(this)

        publicKeyInput.text = MyProfile.getInstance(DB!!)!!.publicKey

    }

    override  fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.itemId === android.R.id.home) {
            finish() // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item)
    }


    companion object {
        fun open(context: Context) {
            context.startActivity(Intent(context, ViewKeyActivity::class.java))
        }
    }
}
