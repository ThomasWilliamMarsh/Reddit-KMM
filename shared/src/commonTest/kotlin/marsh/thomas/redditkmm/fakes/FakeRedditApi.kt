package marsh.thomas.redditkmm.fakes

import marsh.thomas.redditkmm.common.network.RedditApi
import marsh.thomas.redditkmm.common.repository.PostPage

internal class FakeRedditApi : RedditApi {

    private var posts: Result<PostPage> = Result.failure(Throwable())

    fun queuePostsFailure(throwable: Throwable) {
        this.posts = Result.failure(throwable)
    }

    fun queuePosts(posts: PostPage) {
        this.posts = Result.success(posts)
    }

    override suspend fun fetchPosts(after: String?): PostPage {
        if (posts.isSuccess) {
            return posts.getOrThrow()
        } else {
            throw posts.exceptionOrNull()!!
        }
    }
}