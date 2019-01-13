package me.pandelis.shush

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_message_list.*
import me.pandelis.shush.adapters.ChatListItemAdapter
import me.pandelis.shush.models.ChatListItem

class ChatListActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_list)
        setSupportActionBar(toolbar)

        var chatList: ArrayList<ChatListItem>
        val recyclerView: RecyclerView

        chatList = ArrayList()

        recyclerView = findViewById(R.id.messageListRecyclerView) as RecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.setLayoutManager(LinearLayoutManager(this))

        //initializing the MesasgeList
        chatList = ArrayList<ChatListItem>()

        //adding some items to our list
        chatList.add(
            ChatListItem(
                1,
                "Pandelis Zembashis",
                "2 hours ago",
                "19:30",
                600)
        )

        //creating recyclerview adapter
        val adapter = ChatListItemAdapter(this, chatList)
        //setting adapter to recyclerview
        recyclerView.adapter = adapter


        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
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
            else -> super.onOptionsItemSelected(item)
        }
    }
}
