package marsh.thomas.redditkmm.common.network.entities

import kotlinx.serialization.Serializable

@Serializable
data class AccessTokenResponse(val access_token: String)