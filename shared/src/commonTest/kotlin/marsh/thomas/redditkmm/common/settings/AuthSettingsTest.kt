package marsh.thomas.redditkmm.common.settings

import com.russhwolf.settings.MockSettings
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthSettingsTest {

    private lateinit var sut: AuthSettingsImpl

    @BeforeTest
    fun setUp() {
        sut = AuthSettingsImpl(MockSettings())
    }

    @Test
    fun setsUserToken() {
        val accessToken = "access_token"

        sut.accessToken = accessToken

        assertEquals(sut.accessToken, accessToken)
    }
}