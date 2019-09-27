package com.yara.reepository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.*
import com.yara.common_test.rules.CoroutinesMainDispatcherRule
import com.yara.repository.BatmanRepository
import com.yara.repository.BatmanRepositoryImpl
import com.yara.local.dao.SearchDao
import com.yara.model.ApiResult
import com.yara.model.Search
import com.yara.remote.BatmanDatasource
import com.yara.reepository.utils.FakeData
import com.yara.repository.utils.Resource
import kotlinx.coroutines.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
class SearchRepositoryTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesMainDispatcherRule = CoroutinesMainDispatcherRule()

    // FOR DATA
    private lateinit var observer: Observer<Resource<List<Search>>>
    private lateinit var observerSearch: Observer<Resource<Search>>
    private lateinit var batmanRepository: BatmanRepository
    private val userService = mockk<BatmanDatasource>()
    private val userDao = mockk<SearchDao>(relaxed = true)

    @Before
    fun setUp() {
        observer = mockk(relaxed = true)
        observerSearch = mockk(relaxed = true)
        batmanRepository = BatmanRepositoryImpl(userService, userDao)
    }

    @Test
    fun `Get top users from server when no internet is available`() {
        val exception = Exception("Internet")
        every { userService.fetchBatmansAsync() } throws exception
        coEvery { userDao.getTopUsers() } returns listOf()

        runBlocking {
            batmanRepository.getBatmansWithCache().observeForever(observer)
        }

        verifyOrder {
            observer.onChanged(Resource.loading(null)) // Init loading with no value
            observer.onChanged(Resource.loading(listOf())) // Then trying to load from db (fast temp loading) before load from remote source
            observer.onChanged(Resource.error(exception, listOf())) // Retrofit 403 error
        }
        confirmVerified(observer)
    }


    @Test
    fun `Get top users from network`() {
        val fakeUsers = FakeData.createFakeUsers(5)
        every { userService.fetchBatmansAsync() } returns GlobalScope.async {
            ApiResult(
                fakeUsers.size,
                fakeUsers
            )
        }
        coEvery { userDao.getTopUsers() } returns listOf() andThen { fakeUsers }

        runBlocking {
            batmanRepository.getBatmansWithCache().observeForever(observer)
        }

        verifyOrder {
            observer.onChanged(Resource.loading(null)) // Loading from remote source
            observer.onChanged(Resource.loading(listOf())) // Then trying to load from db (fast temp loading) before load from remote source
            observer.onChanged(Resource.success(fakeUsers)) // Success
        }

        coVerify(exactly = 1) {
            userDao.save(fakeUsers)
        }

        confirmVerified(observer)
    }

    @Test
    fun `Get top users from db`() {
        val fakeUsers = FakeData.createFakeUsers(5)
        every { userService.fetchBatmansAsync() } returns GlobalScope.async {
            ApiResult(
                fakeUsers.size,
                fakeUsers
            )
        }
        coEvery { userDao.getTopUsers() } returns fakeUsers

        runBlocking {
            batmanRepository.getBatmansWithCache().observeForever(observer)
        }

        verifyOrder {
            observer.onChanged(Resource.loading(null)) // Loading from remote source
            observer.onChanged(Resource.success(fakeUsers)) // Success
        }

        confirmVerified(observer)
    }

    @Test
    fun `Get user's detail from network`() {
        val fakeUser = FakeData.createFakeUser("fake")
        every { userService.fetchBatmanDetailsAsync("fake_login") } returns GlobalScope.async { fakeUser }
        coEvery { userDao.getUser("fake_login") } returns fakeUser

        runBlocking {
            batmanRepository.getBatmanDetailWithCache(id = "fake_login").observeForever(observerSearch)
        }

        verify {
            observerSearch.onChanged(Resource.loading(null)) // Loading from remote source
            observerSearch.onChanged(Resource.loading(fakeUser)) // Then trying to load from db (fast temp loading) before load from remote source
            observerSearch.onChanged(Resource.success(fakeUser)) // Success
        }

        coVerify(exactly = 1) {
            userDao.save(fakeUser)
        }

        confirmVerified(observerSearch)
    }

    @Test
    fun `Get user's detail from db`() {
        val fakeUser = FakeData.createFakeUser("fake")

        every { userService.fetchBatmanDetailsAsync("fake_login") } returns GlobalScope.async { fakeUser }
        coEvery { userDao.getUser("fake_login") } returns fakeUser.apply { lastRefreshed = Date() }

        runBlocking {
            batmanRepository.getBatmanDetailWithCache(id = "fake_login").observeForever(observerSearch)
        }

        verify {
            observerSearch.onChanged(Resource.loading(null)) // Loading from remote source
            observerSearch.onChanged(Resource.success(fakeUser)) // Success
        }

        confirmVerified(observerSearch)
    }

}