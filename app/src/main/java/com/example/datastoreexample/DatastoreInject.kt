package com.example.datastoreexample

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile

class DatastoreInject private constructor(context: Context) {

    val preferenceStore = PreferenceDataStoreFactory.create(
        produceFile = {
            context.preferencesDataStoreFile(USER_PREFERENCES_NAME)
        }
    )

    val userProtoStore = DataStoreFactory.create(
        UserProtoSerializer,
        produceFile = { context.dataStoreFile(USER_PROTO_NAME) }
    )

    companion object {
        @Volatile
        private var instance: DatastoreInject? = null

        @JvmStatic
        fun getInstance(context: Context): DatastoreInject =
            instance ?: synchronized(this) {
                instance ?: DatastoreInject(context).also {
                    instance = it
                }
            }

        private const val USER_PREFERENCES_NAME = "user_preferences"
        private const val USER_PROTO_NAME = "user_proto.pb"
    }
}