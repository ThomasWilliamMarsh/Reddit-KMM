package marsh.thomas.redditkmm.common.repository

import kotlinx.coroutines.flow.*
import marsh.thomas.redditkmm.common.network.RedditApi

data class Post(
    val id: String,
    val title: String,
    val imageUrl: String,
    val url: String,
    val ups: Int,
)

data class PageInfo(val before: String?, val after: String?) {
    val hasNext get() = after != null
    val hasPrevious get() = before != null
}

data class PostPage(
    val posts: List<Post>,
    val pageInfo: PageInfo
)

interface PostRepository {

    val posts: Flow<List<Post>>

    var subreddit: String

    suspend fun fetchPage()
}

class PostRepositoryImpl(private val api: RedditApi) : PostRepository {

    private val _posts = MutableStateFlow(emptyList<Post>())
    override val posts: Flow<List<Post>> get() = _posts.asStateFlow()

    private var nextPage: String? = null

    override var subreddit: String = "casualuk"
        set(value) {
            nextPage = null
            _posts.update { emptyList() }
            field = value
        }

    override suspend fun fetchPage() {
        val page = api.fetchPosts(subreddit, nextPage)
        nextPage = page.pageInfo.after

        _posts.update { it + page.posts }
    }
}


/**
 * Fake repository to be used for tests
 */
class FakePostsRepository : PostRepository {

    private val queue = ArrayDeque<Result<List<Post>>>()

    private val stream = MutableStateFlow<List<Post>?>(null)

    override val posts: Flow<List<Post>> = stream.filterNotNull()

    override var subreddit: String = "/r/casualuk"

    fun queueSuccess(posts: List<Post>) {
        queue.add(Result.success(posts))
    }

    fun queueFailure(throwable: Throwable) {
        queue.add(Result.failure(throwable))
    }

    override suspend fun fetchPage() {
        val result = queue.removeFirst()
        if (result.isSuccess) {
            val value = result.getOrNull()
            stream.tryEmit(value.orEmpty())
        } else {
            throw result.exceptionOrNull()!!
        }
    }
}

/**
 * Used for testing purposes inside both shared and Android module
 */
fun createPage(
    pageInfo: PageInfo = createPageInfo(),
    posts: List<Post>
) = PostPage(
    posts = posts,
    pageInfo = pageInfo
)

private fun createPageInfo(
    before: String? = "before",
    after: String? = "after"
) = PageInfo(
    before = before,
    after = after
)

fun createPost(
    id: String = "id",
    title: String = "title",
    imageUrl: String = "imageUrl",
    url: String = "url",
    ups: Int = 1234
): Post = Post(
    id = id,
    title = title,
    imageUrl = imageUrl,
    url = url,
    ups = ups
)
