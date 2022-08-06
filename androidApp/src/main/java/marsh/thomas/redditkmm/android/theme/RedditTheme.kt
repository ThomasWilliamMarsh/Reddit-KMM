package marsh.thomas.redditkmm.android.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider


@Composable
fun RedditTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalRedditColours provides defaultColours(),
        LocalRedditTypography provides defaultTypography(),
        LocalRedditShapes provides defaultShapes(),
        content = content
    )
}

object RedditTheme {

    val colors: RedditColours
        @Composable
        get() = LocalRedditColours.current

    val typography: RedditTypography
        @Composable
        get() = LocalRedditTypography.current

    val shapes: RedditShapes
        @Composable
        get() = LocalRedditShapes.current
}