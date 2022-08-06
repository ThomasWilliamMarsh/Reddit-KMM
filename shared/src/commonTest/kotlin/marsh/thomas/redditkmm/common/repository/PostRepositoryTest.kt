package marsh.thomas.redditkmm.common.repository

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import marsh.thomas.redditkmm.fakes.FakeError
import marsh.thomas.redditkmm.fakes.FakeRedditApi
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PostRepositoryTest {

    private lateinit var sut: PostRepositoryImpl

    private val api = FakeRedditApi()

    @BeforeTest
    fun setUp() {
        sut = PostRepositoryImpl(api)
    }

    @Test
    fun throwsErrorOnFailedFetch() {
        runBlocking {
            api.queuePostsFailure(FakeError("Error!"))

            assertFailsWith<FakeError> {
                sut.fetchPage()
            }
        }
    }

    @Test
    fun returnsDataOnSuccessfulFetch() = runBlocking {
        val post = createPost()
        val page = createPage(posts = listOf(post))

        api.queuePosts(page)

        sut.fetchPage()

        assertEquals(sut.posts.first(), listOf(post))
    }

    @Test
    fun returnsDataOnNextPage() = runBlocking {
        val page1Post = createPost(id = "page 1")
        val page2Post = createPost(id = "page 2")

        val page1 = createPage(posts = listOf(page1Post))
        val page2 = createPage(posts = listOf(page2Post))

        api.queuePosts(page1)
        sut.fetchPage()

        api.queuePosts(page2)
        sut.fetchPage()

        assertEquals(sut.posts.first(), listOf(page1Post, page2Post))
    }
}