package me.pandelis.shush.classes

import me.pandelis.shush.models.Profile


class MyProfile {
    companion object {
        private var INSTANCE: Profile? = null

        fun getInstance(db: AppDatabase): Profile? {
            if (INSTANCE == null) {
                synchronized(Profile::class) {
                    INSTANCE = db?.profileDao()?.getProfile()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}