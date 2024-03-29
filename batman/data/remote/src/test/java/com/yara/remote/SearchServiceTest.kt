package com.yara.remote

import com.yara.remote.base.BaseTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.HttpException
import java.net.HttpURLConnection

class SearchServiceTest: BaseTest() {

    @Test
    fun `search top users by name`() {
        mockHttpResponse(mockServer, "search_users.json", HttpURLConnection.HTTP_OK)
        runBlocking {
            val users = batmanService.fetchBatmansAsync().await()
            assertEquals(1, users.items.size)
            assertEquals("6847959", users.items.first().id)
            assertEquals("PhilippeBoisney", users.items.first().login)
            assertEquals("https://avatars0.githubusercontent.com/u/6847959?v=4", users.items.first().avatarUrl)
        }
    }

    @Test(expected = HttpException::class)
    fun `search top users by name and fail`() {
        mockHttpResponse(mockServer,"search_users.json", HttpURLConnection.HTTP_FORBIDDEN)
        runBlocking {
            batmanService.fetchBatmansAsync().await()
        }
    }

    // ---

    @Test
    fun `fetch user's detail`() {
        mockHttpResponse(mockServer,"user_detail.json", HttpURLConnection.HTTP_OK)
        runBlocking {
            val user = batmanService.fetchBatmanDetailsAsync("PhilippeBoisney").await()
            assertEquals("6847959", user.id)
            assertEquals("PhilippeBoisney", user.login)
            assertEquals("https://avatars0.githubusercontent.com/u/6847959?v=4", user.avatarUrl)
            assertEquals("Boisney Philippe", user.name)
            assertEquals("https://medium.com/@Phil_Boisney", user.blog)
            assertEquals("CookMinute", user.company)
        }
    }

    @Test(expected = HttpException::class)
    fun `fetch user's detail and fail`() {
        mockHttpResponse(mockServer,"user_detail.json", HttpURLConnection.HTTP_FORBIDDEN)
        runBlocking {
            batmanService.fetchBatmanDetailsAsync("PhilippeBoisney").await()
        }
    }
}