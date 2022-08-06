package marsh.thomas.redditkmm.common.repository

import marsh.thomas.redditkmm.common.network.AuthApi
import marsh.thomas.redditkmm.common.network.RedditApi
import marsh.thomas.redditkmm.common.settings.AuthSettings

interface AuthRepository {
    suspend fun fetchAccessToken()
}

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val settings: AuthSettings
) : AuthRepository {

    override suspend fun fetchAccessToken() {
        settings.accessToken = try {
            api.getAccessToken().access_token
        } catch (throwable: Throwable) {
            ""
        }
    }
}