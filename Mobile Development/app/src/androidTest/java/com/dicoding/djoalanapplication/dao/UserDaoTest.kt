package com.dicoding.djoalanapplication.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.dicoding.djoalanapplication.DummyData
import com.dicoding.djoalanapplication.data.AppDatabase
import com.dicoding.djoalanapplication.data.user.UserDao
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

class UserDaoTest {

    @get:Rule
    var instantTaskExec = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var dao: UserDao

    // dummy loginData
    private val userEntities = DummyData.generateUserEntities()

    @Before
    fun initDB() {
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java).build()
        dao = database.userDao()
    }

    @After
    fun closeDB() = database.close()

    @Test
    fun saveUser() = runBlocking {
        dao.insertUser(userEntities)
        val actualData = dao.getUser()
        assertEquals(userEntities.email, actualData.email)
    }

    @Test
    fun deleteUser() = runBlocking {
        dao.insertUser(userEntities)
        dao.deleteAll()
        val actualData = dao.getUser()
        assertNull(actualData)
    }
}