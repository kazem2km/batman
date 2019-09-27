package com.yara.local

import com.yara.common_test.datasets.UserDataset.DATE_REFRESH
import com.yara.common_test.datasets.UserDataset.FAKE_USERS
import com.yara.local.base.BaseTest
import com.yara.model.Search
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class SearchDaoTest: BaseTest() {

    override fun setUp(){
        super.setUp()
        fillDatabase()
    }

    @Test
    fun getTopUsersFromDb() = runBlocking {
        val users = database.userDao().getTopUsers()
        assertEquals(3, users.size)
        compareTwoUsers(FAKE_USERS.first(), users.first())
    }

    @Test
    fun getUser() = runBlocking {
        val user = database.userDao().getUser(FAKE_USERS.first().login)
        compareTwoUsers(FAKE_USERS.first(), user)
    }

    @Test
    fun saveUser_DateMustChange() = runBlocking {
            database.userDao().save(FAKE_USERS.first())
            val user = database.userDao().getUser(FAKE_USERS.first().login)
            assertNotEquals(DATE_REFRESH, user.lastRefreshed)
    }

    @Test
    fun saveUsers_DateMustChange() = runBlocking {
        database.userDao().save(FAKE_USERS)
        val users = database.userDao().getTopUsers()
        assertNotEquals(DATE_REFRESH, users.first().lastRefreshed)
    }

    // ---

    private fun compareTwoUsers(search: Search, searchToTest: Search){
        assertEquals(search.id, searchToTest.id)
        assertEquals(search.name, searchToTest.name)
        assertEquals(search.login, searchToTest.login)
        assertEquals(search.avatarUrl, searchToTest.avatarUrl)
        assertEquals(search.company, searchToTest.company)
        assertEquals(search.blog, searchToTest.blog)
        assertEquals(search.lastRefreshed, searchToTest.lastRefreshed)
    }

    private fun fillDatabase() {
        runBlocking {
            database.userDao().save(FAKE_USERS)
        }
    }
}