package marsh.thomas.redditkmm.android.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Stable
data class RedditShapes(
    val small: Shape,
    val large: Shape,
)

@Composable
fun defaultShapes() = RedditShapes(
    small = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(16.dp)
)

val LocalRedditShapes =
    staticCompositionLocalOf<RedditShapes> { throw Exception("Shapes not defined!") }