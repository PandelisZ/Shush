package me.pandelis.shush.activity

import android.arch.persistence.room.Room
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import me.pandelis.shush.R
import me.pandelis.shush.classes.AppDatabase
import me.pandelis.shush.classes.ShushAPI
import me.pandelis.shush.models.GetMessage
import me.pandelis.shush.models.MessageResponse
import me.pandelis.shush.models.Profile
import me.pandelis.shush.services.ShushService
import me.pandelis.shush.utils.DbWorkerThread


class MainActivity() : AppCompatActivity() {

    private var DB: AppDatabase? = null
    private var mDbWorkerThread = DbWorkerThread("dbWorkerThread")
    private val mUiHandler = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

//        this.deleteDatabase("shush.db")

        mDbWorkerThread.start()

        DB = AppDatabase.getInstance(this)

        fetchProfileFromDb()
    }

    private fun fetchProfileFromDb() {
        val task = Runnable {
            val profile = DB?.profileDao()?.getProfile()

            mUiHandler.post {
                if (profile == null) {
                    UserOnboardingActivity.open(this)
                } else {
                    ChatListActivity.open(this)
                }
            }

        }
        mDbWorkerThread.postTask(task)
    }

    companion object {
        fun open(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }
}