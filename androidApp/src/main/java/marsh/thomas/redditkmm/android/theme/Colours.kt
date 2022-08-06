package marsh.thomas.redditkmm.android.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Stable
data class RedditColours(
    val background: Color,
    val accent: Color,
    val textMain: Color,
    val disabled: Color
)

@Composable
fun defaultColours() = RedditColours(
    background = Color.White,
    accent = Color(0xFFFF4500),
    textMain = Color.Black,
    disabled = Color.LightGray
)

val LocalRedditColours =
    staticCompositionLocalOf<RedditColours> { throw Exception("Colours not defined!") }