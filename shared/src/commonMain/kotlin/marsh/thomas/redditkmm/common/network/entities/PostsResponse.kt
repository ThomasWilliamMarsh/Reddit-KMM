package marsh.thomas.redditkmm.common.network.entities

import kotlinx.serialization.Serializable
import marsh.thomas.redditkmm.common.repository.PageInfo
import marsh.thomas.redditkmm.common.repository.Post
import marsh.thomas.redditkmm.common.repository.PostPage

@Serializable
data class PostsResponse(
    val data: DataResponse
)

@Serializable
data class DataResponse(
    val after: String?,
    val before: String?,
    val children: List<ChildrenResponse>
)

@Serializable
data class ChildrenResponse(
    val data: ChildDataResponse,
    val kind: String
)

@Serializable
data class ChildDataResponse(
    val author: String,
    val id: String,
    val thumbnail: String?,
    val thumbnail_height: Int?,
    val thumbnail_width: Int?,
    val title: String,
    val url: String?,
    val ups: Int,
)

fun PostsResponse.toDomain(): PostPage {
    return PostPage(
        pageInfo = PageInfo(
            before = data.before,
            after = data.after
        ),
        posts = data.children.map {
            Post(
                id = it.data.id,
                title = it.data.title,
                imageUrl = it.data.thumbnail.orEmpty(),
                url = it.data.url.orEmpty(),
                ups = it.data.ups,
            )
        }
    )
}