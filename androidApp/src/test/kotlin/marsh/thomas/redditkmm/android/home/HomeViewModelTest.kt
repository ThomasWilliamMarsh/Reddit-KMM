package marsh.thomas.redditkmm.android.home

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import marsh.thomas.redditkmm.android.home.HomeViewModel.Screen.PostDetail
import marsh.thomas.redditkmm.android.home.HomeViewModel.Screen.Posts
import marsh.thomas.redditkmm.android.home.HomeViewModel.UiAction.*
import marsh.thomas.redditkmm.common.repository.FakePostsRepository
import marsh.thomas.redditkmm.common.repository.createPost
import marsh.thomas.redditkmm.common.util.FakeValidUrlUseCase
import kotlin.test.*

internal class HomeViewModelTest {

    private val postsRepository = FakePostsRepository()
    private val validUrl = FakeValidUrlUseCase()

    @BeforeTest
    fun `set up`() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @AfterTest
    fun `tear down`() {
        Dispatchers.resetMain()
    }

    @Test
    @Ignore
    fun `Update view state when we tap a post`() = runBlocking {
        val url = "post url"

        val sut = HomeViewModel(postsRepository, validUrl)
        sut.viewState.test {
            assertEquals(awaitItem().navigationState, Posts)
            sut.onAction(PostTapped(url))
            assertEquals(awaitItem().navigationState, PostDetail(url))
        }
    }

    @Test
    @Ignore
    fun `Update view state when we handled navigation`() = runBlocking {
        val url = "post url"

        val sut = HomeViewModel(postsRepository, validUrl)
        sut.viewState.test {
            assertEquals(awaitItem().navigationState, Posts)
            sut.onAction(PostTapped(url))
            assertEquals(awaitItem().navigationState, PostDetail(url))
            sut.onAction(HandledNavigation)
            assertEquals(awaitItem().navigationState, Posts)
        }
    }

    @Test
    fun `Update view state when we have displayed error`() = runBlocking {
        postsRepository.queueFailure(Throwable("Oh no"))

        val sut = HomeViewModel(postsRepository, validUrl)
        sut.viewState.test {
            assertEquals(HomeViewModel.ViewState(isLoading = false, showError = true), awaitItem())
            sut.onAction(ErrorDisplayed)
            assertEquals(HomeViewModel.ViewState(isLoading = false, showError = false), awaitItem())
        }
    }

    @Test
    fun `Update view state when we fail to fetch posts`() = runBlocking {
        postsRepository.queueFailure(Throwable("Oh no"))

        val sut = HomeViewModel(postsRepository, validUrl)
        sut.viewState.test {
            assertEquals(HomeViewModel.ViewState(isLoading = false, showError = true), awaitItem())
        }
    }

    @Test
    fun `Update view state when we fetch posts with valid url`() = runBlocking {
        val post = createPost()
        postsRepository.queueSuccess(listOf(post))
        validUrl.setValid(true)

        val sut = HomeViewModel(postsRepository, validUrl)
        sut.viewState.test {
            assertEquals(
                HomeViewModel.ViewState(
                    isLoading = false,
                    posts = listOf(
                        HomeViewModel.ViewState.Post(
                            title = post.title,
                            imageUrl = post.imageUrl,
                            url = post.url,
                            ups = post.ups.toString()
                        )
                    )
                ), awaitItem()
            )
        }
    }

    @Test
    fun `Update view state when we fetch posts with invalid url`() = runBlocking {
        val post = createPost()
        postsRepository.queueSuccess(listOf(post))
        validUrl.setValid(false)

        val sut = HomeViewModel(postsRepository, validUrl)
        sut.viewState.test {
            assertEquals(
                HomeViewModel.ViewState(
                    isLoading = false,
                    posts = listOf(
                        HomeViewModel.ViewState.Post(
                            title = post.title,
                            imageUrl = "",
                            url = post.url,
                            ups = post.ups.toString()
                        )
                    )
                ), awaitItem()
            )
        }
    }

    @Test
    fun `Update view state when scroll to bottom`() = runBlocking {
        val post = createPost()
        val post2 = createPost(id = "post 2", title = "post 2")

        postsRepository.queueSuccess(listOf(post))
        validUrl.setValid(false)

        val sut = HomeViewModel(postsRepository, validUrl)
        sut.viewState.test {
            assertEquals(
                HomeViewModel.ViewState(
                    isLoading = false,
                    posts = listOf(
                        HomeViewModel.ViewState.Post(
                            title = post.title,
                            imageUrl = "",
                            url = post.url,
                            ups = post.ups.toString()
                        )
                    )
                ), awaitItem()
            )

            postsRepository.queueSuccess(listOf(post2))
            sut.onAction(ScrolledToBottom)

            assertEquals(
                HomeViewModel.ViewState(
                    isLoading = false,
                    posts = listOf(
                        HomeViewModel.ViewState.Post(
                            title = post2.title,
                            imageUrl = "",
                            url = post2.url,
                            ups = post2.ups.toString()
                        )
                    )
                ), awaitItem()
            )
        }
    }
}