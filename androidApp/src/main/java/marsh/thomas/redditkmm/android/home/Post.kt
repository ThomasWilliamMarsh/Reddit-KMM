package marsh.thomas.redditkmm.android.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import marsh.thomas.redditkmm.android.theme.RedditTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Post(
    post: HomeViewModel.ViewState.Post,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = onClick,
        backgroundColor = RedditTheme.colors.background
    ) {
        Column {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                if (post.imageUrl.isNotEmpty()) {
                    PostImage(
                        modifier = Modifier.weight(1.5f, fill = true),
                        url = post.url,
                        contentDescription = post.title
                    )
                }

                Text(
                    modifier = Modifier
                        .weight(3f, fill = true)
                        .align(Alignment.CenterVertically),
                    textAlign = TextAlign.Start,
                    style = RedditTheme.typography.headline,
                    text = post.title
                )
                Text(
                    modifier = Modifier
                        .weight(1f, fill = true)
                        .align(Alignment.CenterVertically),
                    textAlign = TextAlign.End,
                    style = RedditTheme.typography.highlight,
                    text = post.ups
                )
            }

            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(RedditTheme.colors.disabled)
            )
        }
    }
}

@Composable
private fun PostImage(
    modifier: Modifier = Modifier,
    url: String,
    contentDescription: String
) {
    Box(
        modifier
            .height(92.dp)
            .clip(RedditTheme.shapes.small)
            .background(RedditTheme.colors.disabled)
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = url,
            contentDescription = contentDescription
        )
    }
}

@Preview
@Composable
fun PostPreview() = RedditTheme {
    Post(
        post = HomeViewModel.ViewState.Post(
            "Here is a sample title",
            imageUrl = "",
            ups = "2321",
            url = "www.test.com"
        ),
        onClick = {}
    )
}