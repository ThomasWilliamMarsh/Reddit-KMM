package marsh.thomas.redditkmm.android.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Stable
data class RedditTypography(
    val headline: TextStyle,
    val subHeadline: TextStyle,
    val highlight: TextStyle
)

@Composable
fun defaultTypography() = RedditTypography(
    headline = TextStyle(
        fontSize = 16.sp,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold
    ),
    subHeadline = TextStyle(
        fontSize = 10.sp,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold
    ),
    highlight = TextStyle(
        fontSize = 18.sp,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold
    )
)

val LocalRedditTypography =
    staticCompositionLocalOf<RedditTypography> { throw Exception("Typography not defined!") }