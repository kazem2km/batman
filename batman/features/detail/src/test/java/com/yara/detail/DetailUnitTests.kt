package com.yara.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.test.filters.SmallTest
import io.mockk.coEvery
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import com.yara.common.utils.Event
import com.yara.common_test.datasets.UserDataset.FAKE_USERS
import com.yara.common_test.extensions.blockingObserve
import com.yara.detail.domain.GetUserDetailUseCase
import com.yara.model.Search
import com.yara.navigation.NavigationCommand
import com.yara.repository.AppDispatchers
import com.yara.repository.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
@SmallTest
class DetailUnitTests {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var getUserDetailUseCase: GetUserDetailUseCase
    private lateinit var viewModel: DetailViewModel
    private val appDispatchers =
        AppDispatchers(Dispatchers.Unconfined, Dispatchers.Unconfined)

    @Before
    fun setUp() {
        getUserDetailUseCase = mockk()
        viewModel = DetailViewModel(getUserDetailUseCase, appDispatchers)
    }

    @Test
    fun `User's detail requested when Fragment is created`() {
        val observerLoading = mockk<Observer<Resource.Status>>(relaxed = true)
        val observerResult = mockk<Observer<Search>>(relaxed = true)
        val loading = Resource.loading(null)
        val result = Resource.success(FAKE_USERS.first())
        val events = MutableLiveData<Resource<Search>>().apply { value = loading }

        coEvery { getUserDetailUseCase(false, "fake") } returns events

        viewModel.search.observeForever(observerResult)
        viewModel.isLoading.observeForever(observerLoading)
        viewModel.loadDataWhenActivityStarts("fake")

        verify {
            observerLoading.onChanged(Resource.Status.LOADING)
            observerResult.onChanged(null)
        }

        events.value = result

        verify {
            observerLoading.onChanged(Resource.Status.SUCCESS)
            observerResult.onChanged(FAKE_USERS.first())
        }

        confirmVerified(observerResult)
        confirmVerified(observerLoading)
    }

    @Test
    fun `User's detail refreshed when user pulls to refresh`() {
        val observer = mockk<Observer<Search>>(relaxed = true)
        val result = Resource.success(FAKE_USERS.first())
        coEvery { getUserDetailUseCase(any(), "fake") } returns MutableLiveData<Resource<Search>>().apply { value = result }

        viewModel.search.observeForever(observer)
        viewModel.loadDataWhenActivityStarts("fake")
        viewModel.reloadDataWhenUserRefreshes()

        verify {
            observer.onChanged(FAKE_USERS.first())
            observer.onChanged(FAKE_USERS.first())
        }

        confirmVerified(observer)
    }

    @Test
    fun `User clicks on avatar image and go to DetailImageFragment`() {
        val event = Event(NavigationCommand.To(DetailFragmentDirections.actionDetailFragmentToImageDetailFragment(FAKE_USERS.first().avatarUrl)))
        coEvery { getUserDetailUseCase(false, "fake") } returns MutableLiveData<Resource<Search>>().apply { value = Resource.success(FAKE_USERS.first()) }

        viewModel.userClicksOnAvatarImage(FAKE_USERS.first())

        Assert.assertEquals(event.peekContent(), viewModel.navigation.blockingObserve()!!.peekContent())
    }
}