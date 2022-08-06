package marsh.thomas.redditkmm.common.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import marsh.thomas.redditkmm.common.network.AuthApi.Companion.BASE_URL
import marsh.thomas.redditkmm.common.network.entities.AccessTokenResponse

interface AuthApi {
    suspend fun getAccessToken(): AccessTokenResponse

    companion object {
        const val BASE_URL: String = "https://www.reddit.com/api/v1"
    }
}

class AuthApiImpl(private val client: HttpClient) : AuthApi {

    override suspend fun getAccessToken(): AccessTokenResponse {
        return client.post("$BASE_URL/access_token") {
            formData {
                parameter("grant_type", "https://oauth.reddit.com/grants/installed_client")
                parameter("device_id", "this_is_a_test_client")
            }
        }.body()
    }
}