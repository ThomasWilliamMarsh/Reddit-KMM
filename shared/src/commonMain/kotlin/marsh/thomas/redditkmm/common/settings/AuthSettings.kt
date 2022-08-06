package marsh.thomas.redditkmm.common.settings

import com.russhwolf.settings.Settings

interface AuthSettings {
    var accessToken: String
}

class AuthSettingsImpl(private val settings: Settings) : AuthSettings {

    override var accessToken: String
        set(value) = settings.putString(KEY_ACCESS_TOKEN, value)
        get() = settings.getString(KEY_ACCESS_TOKEN)

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
    }
}