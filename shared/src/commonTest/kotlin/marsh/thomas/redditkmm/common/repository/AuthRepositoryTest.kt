package marsh.thomas.redditkmm.common.repository

import com.russhwolf.settings.MockSettings
import kotlinx.coroutines.runBlocking
import marsh.thomas.redditkmm.common.network.entities.AccessTokenResponse
import marsh.thomas.redditkmm.common.settings.AuthSettingsImpl
import marsh.thomas.redditkmm.fakes.FakeAuthApi
import marsh.thomas.redditkmm.fakes.FakeError
import marsh.thomas.redditkmm.fakes.FakeRedditApi
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthRepositoryTest {

    private lateinit var sut: AuthRepositoryImpl

    private val api = FakeAuthApi()
    private val settings = AuthSettingsImpl(MockSettings())

    @BeforeTest
    fun setUp() {
        sut = AuthRepositoryImpl(api, settings)
    }

    @Test
    fun savesEmptyStringOnFailedFetch() = runBlocking {
        api.queueAccessTokenFailure(FakeError("Error!"))

        assertEquals(settings.accessToken, "")
    }

    @Test
    fun savesAccessTokenOnSucessfulFetch() = runBlocking {
        val token = AccessTokenResponse(access_token = "token")

        api.queueAccessToken(token)

        sut.fetchAccessToken()

        assertEquals(settings.accessToken, token.access_token)
    }
}