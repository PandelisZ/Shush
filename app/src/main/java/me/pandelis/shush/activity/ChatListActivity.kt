package me.pandelis.shush.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_chat_list.*
import me.pandelis.shush.R
import me.pandelis.shush.adapters.ChatListItemAdapter
import android.widget.Toast
import android.R.attr.data
import android.os.Handler
import me.pandelis.shush.classes.AppDatabase
import me.pandelis.shush.classes.MyProfile
import me.pandelis.shush.classes.ShushAPI
import me.pandelis.shush.entities.MessageEntity
import me.pandelis.shush.models.*
import me.pandelis.shush.services.ShushService
import me.pandelis.shush.utils.DbWorkerThread
import java.util.*


class ChatListActivity: AppCompatActivity() {

    private var DB: AppDatabase? = null
    private var API: ShushService? = null
    private lateinit var mDbWorkerThread: DbWorkerThread
    private val mUiHandler = Handler()
    private lateinit var contacts: List<DbContact>
    private var chatList = emptyList<ChatListItem>()
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list)
        setSupportActionBar(toolbar)

        mDbWorkerThread = DbWorkerThread("dbWorkerThread")
        mDbWorkerThread.start()

        DB = AppDatabase.getInstance(this)
        API = ShushAPI.getInstance()


        recyclerView = findViewById(R.id.chatListRecyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)



        if (intent.extras != null) {
            val successFullUserAdd = intent.extras.getBoolean("CONTACT_ADDED_SUCCESSFULLY")
            if (!successFullUserAdd) {
                Toast.makeText(
                    applicationContext, "Could not find user with that public key",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        fetchContactsFroDB()
        fetchMessagesFromApi()

        fab.setOnClickListener { view ->
            AddContact.open(this)
        }
    }

    public override fun onResume() {
        super.onResume()
        fetchContactsFroDB()
    }

    fun fetchMessagesFromApi() {
        val task = Runnable {
            val downloadedMessages = API?.messages(GetMessage(MyProfile.getInstance(DB!!)!!.publicKey))?.execute()

            val messageResonse = downloadedMessages?.body()

            messageResonse?.forEach { m ->
                var contactId = DB?.contactDao()?.getContactByPublicKey(m.sender)?.id
                if (contactId == null) {
                    val newContact = API?.user(UpdateProfile(name = null, publicKey = m.sender, firebaseId = null))?.execute()
                    if (newContact!!.isSuccessful) {
                        val resContact = newContact.body()!!
                        contactId = DB?.contactDao()?.add(DbContact(resContact.name, m.sender, null))?.toInt()
                    }
                }

                if (contactId !== null) {
                    DB?.messageDao()?.add(MessageEntity(m.payload, m.createdAt, Date(), contactId!!))
                }
                API?.deleteMessage(m.id)?.execute()
            }

            fetchContactsFroDB()

        }
        mDbWorkerThread.postTask(task)
    }

    fun fetchContactsFroDB() {
        val task = Runnable {
            contacts = DB?.contactDao()!!.getContacts()

            mUiHandler.post {
                if (contacts != null) {
                    //initializing the MesasgeList

                    chatList = contacts.map { c ->
                        ChatListItem(
                            c.id,
                            Contact(c.id.toString(), c.name),
                            "2 hours ago",
                            "19:30",
                            600)
                    }

                    //creating recyclerview adapter
                    val adapter = ChatListItemAdapter(this, chatList)
                    //setting adapter to recyclerview
                    recyclerView.adapter = adapter
                }
            }

        }
        mDbWorkerThread.postTask(task)
    }

    // Exit app if back is pressed
    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_message_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.delete_database -> {
                this.deleteDatabase("shush.db")
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        fun open(context: Context) {
            context.startActivity(Intent(context, ChatListActivity::class.java))
        }
    }
}
