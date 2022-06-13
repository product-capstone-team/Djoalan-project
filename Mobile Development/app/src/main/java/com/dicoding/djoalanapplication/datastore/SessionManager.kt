package com.dicoding.djoalanapplication.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(private val dataStore: DataStore<Preferences>){

    private val LOGIN_STATE = booleanPreferencesKey("user_state")
    private val BUSINESS_ACCOUNT = booleanPreferencesKey("account_type")
    private val NAV_STATE = booleanPreferencesKey("nav_state")
    private val STORE_ID = stringPreferencesKey("store_id")

    fun getUserLoginState(): Flow<Boolean> {
        val data = dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { pref ->
            pref[LOGIN_STATE] ?: false
        }
        return data
    }

    fun getUserAccType(): Flow<Boolean> {
        val accType = dataStore.data
            .catch { exc ->
                if (exc is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exc
                }
            }
            .map { pref ->
                pref[BUSINESS_ACCOUNT] ?: false
            }
        return accType
    }

    fun getNavigationState(): Flow<Boolean> {
        val navState = dataStore.data
            .catch { e ->
                if (e is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw e
                }
            }
            .map { pref ->
                pref[NAV_STATE] ?: false
            }
        return navState
    }

    fun getStoreId(): Flow<String> {
        val storeId = dataStore.data
            .catch { e ->
                if (e is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw e
                }
            }
            .map { pref ->
                pref[STORE_ID] ?: ""
            }
        return storeId
    }

    suspend fun saveLoginState(state: Boolean, isBusinessAcc: Boolean) {
        dataStore.edit { pref ->
            pref[LOGIN_STATE] = state
            pref[BUSINESS_ACCOUNT] = isBusinessAcc
        }

    }

    suspend fun deleteSession() {
        dataStore.edit { pref ->
            pref[LOGIN_STATE] = false
            pref[BUSINESS_ACCOUNT] = false
            pref[STORE_ID] = ""
        }
    }

    suspend fun changeNavigationState(isBottomLevel: Boolean) {
        dataStore.edit { pref ->
            pref[NAV_STATE] = isBottomLevel
        }
    }

    suspend fun saveStoreId(storeId: String) {
        dataStore.edit { pref ->
            pref[STORE_ID] = storeId
        }
    }

//    companion object {
//        private var INSTANCE: SessionManager? = null
//
//        fun getInstance(dataStore: DataStore<Preferences>): SessionManager {
//            return INSTANCE ?: synchronized(this) {
//                val instance = SessionManager(dataStore)
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
}