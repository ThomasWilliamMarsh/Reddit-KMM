package marsh.thomas.redditkmm.common.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import marsh.thomas.redditkmm.common.network.RedditApi.Companion.BASE_URL
import marsh.thomas.redditkmm.common.network.entities.PostsResponse
import marsh.thomas.redditkmm.common.network.entities.toDomain
import marsh.thomas.redditkmm.common.repository.PostPage

interface RedditApi {
    suspend fun fetchPosts(after: String? = null): PostPage

    companion object {
        const val BASE_URL: String = "https://oauth.reddit.com"
    }
}

class RedditApiImpl(private val client: HttpClient) : RedditApi {

    override suspend fun fetchPosts(after: String?): PostPage {
        return client.get("$BASE_URL/r/casualuk/best.json?after=$after")
            .body<PostsResponse>()
            .toDomain()
    }
}