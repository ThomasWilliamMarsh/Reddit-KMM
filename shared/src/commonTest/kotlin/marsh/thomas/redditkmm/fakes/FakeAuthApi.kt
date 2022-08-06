package marsh.thomas.redditkmm.fakes

import marsh.thomas.redditkmm.common.network.AuthApi
import marsh.thomas.redditkmm.common.network.entities.AccessTokenResponse

internal class FakeAuthApi : AuthApi {

    private var accessToken: Result<AccessTokenResponse> =
        Result.success(AccessTokenResponse(""))

    fun queueAccessTokenFailure(throwable: Throwable) {
        this.accessToken = Result.failure(throwable)
    }

    fun queueAccessToken(token: AccessTokenResponse) {
        this.accessToken = Result.success(token)
    }

    override suspend fun getAccessToken(): AccessTokenResponse {
        if (accessToken.isSuccess) {
            return accessToken.getOrThrow()
        } else {
            throw accessToken.exceptionOrNull()!!
        }
    }
}