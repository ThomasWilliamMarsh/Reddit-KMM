package marsh.thomas.redditkmm.android.home

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import marsh.thomas.redditkmm.android.R
import marsh.thomas.redditkmm.android.home.HomeViewModel.Screen.PostDetail
import marsh.thomas.redditkmm.android.home.HomeViewModel.Screen.Posts
import marsh.thomas.redditkmm.android.home.HomeViewModel.UiAction.*
import marsh.thomas.redditkmm.android.theme.RedditTheme
import org.koin.androidx.compose.getViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel = getViewModel()) {

    val viewState by viewModel.viewState.collectAsState()

    HomeScreen(
        viewState = viewState,
        onScrolledToEnd = { viewModel.onAction(ScrolledToBottom) },
        onPostClick = { viewModel.onAction(PostTapped(it)) },
        onErrorDisplayed = { viewModel.onAction(ErrorDisplayed) },
        onSearchTermChanged = { viewModel.onAction(SearchTermChanged(it)) },
        onSearchTapped = { viewModel.onAction(SearchTapped) }
    )

    Navigation(viewState.navigationState) {
        viewModel.onAction(HandledNavigation)
    }
}

@Composable
private fun HomeScreen(
    viewState: HomeViewModel.ViewState,
    onPostClick: (url: String) -> Unit,
    onScrolledToEnd: () -> Unit,
    onErrorDisplayed: () -> Unit,
    onSearchTermChanged: (term: String) -> Unit,
    onSearchTapped: () -> Unit
) {

    Scaffold(
        topBar = {
            RedditToolbar(
                searchTerm = viewState.searchTerm,
                onSearchTermChanged = onSearchTermChanged,
                onSearchTapped = onSearchTapped
            )
        },
    ) {
        Box(
            Modifier
                .padding(it)
                .fillMaxSize()
        ) {

            AnimatedVisibility(
                modifier = Modifier.align(Alignment.Center),
                visible = viewState.isLoading
            ) {
                LoadingIndicator(Modifier.align(Alignment.Center))
            }

            PostsList(
                posts = viewState.posts,
                onPostClick = onPostClick,
                onScrolledToEnd = onScrolledToEnd,
                isLoadingMore = viewState.isLoadingMore
            )


            AnimatedVisibility(
                enter = slideInVertically(initialOffsetY = { height -> height }),
                exit = slideOutVertically(targetOffsetY = { height -> height }),
                visible = viewState.showError
            ) {
                ErrorMessage {
                    onErrorDisplayed()
                }
            }
        }
    }
}

@Composable
private fun PostsList(
    posts: List<HomeViewModel.ViewState.Post>,
    onPostClick: (url: String) -> Unit,
    onScrolledToEnd: () -> Unit,
    isLoadingMore: Boolean
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        itemsIndexed(posts) { index, post ->
            Post(post) {
                onPostClick(post.url)
            }

            if (index == posts.size - 1) {
                onScrolledToEnd()
            }
        }

        item {
            LoadingMoreIndicator(show = isLoadingMore)
        }
    }
}


@Composable
private fun LoadingIndicator(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier,
        color = RedditTheme.colors.accent
    )
}

@Composable
private fun LoadingMoreIndicator(show: Boolean) {
    AnimatedVisibility(visible = show) {
        Box(Modifier.fillMaxWidth().padding(8.dp)) {
            LoadingIndicator(Modifier.align(Alignment.Center))
        }
    }
}

@Composable
private fun ErrorMessage(
    onErrorDisplayed: () -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            backgroundColor = RedditTheme.colors.accent
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = stringResource(R.string.generic_error_message),
                style = RedditTheme.typography.highlight,
                color = RedditTheme.colors.background
            )
        }
    }

    LaunchedEffect(Unit) {
        delay(ERROR_DISPLAY_MS)
        onErrorDisplayed()
    }
}

@Composable
private fun Navigation(screen: HomeViewModel.Screen, onHandled: () -> Unit) {
    when (screen) {
        is PostDetail -> {
            val activity = LocalContext.current
            activity.startActivity(Intent(ACTION_VIEW, Uri.parse(screen.url)))
            onHandled()
        }
        Posts -> {
            //Do nothing
        }
    }
}

@Composable
private fun RedditToolbar(
    searchTerm: String,
    onSearchTermChanged: (term: String) -> Unit,
    onSearchTapped: () -> Unit
) {
    TopAppBar(
        backgroundColor = RedditTheme.colors.accent,
        contentColor = RedditTheme.colors.background,
        content = {
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = stringResource(R.string.subreddit_prefix),
                style = RedditTheme.typography.highlight
            )
            TextField(
                value = searchTerm,
                placeholder = {
                    Text(
                        text = stringResource(R.string.subreddit_textfield_hint),
                        style = RedditTheme.typography.highlight,
                        color = RedditTheme.colors.background
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = RedditTheme.colors.background,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    backgroundColor = RedditTheme.colors.accent
                ),
                onValueChange = { onSearchTermChanged(it) },
                textStyle = RedditTheme.typography.highlight,
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearchTapped()
                    }
                ),
            )
        }
    )
}

@Preview
@Composable
fun ToolbarPreview() = RedditTheme {
    RedditToolbar("/r/casualuk", {}, {})
}

@Preview
@Composable
fun ErrorMessagePreview() = RedditTheme {
    ErrorMessage { }
}

private const val ERROR_DISPLAY_MS = 5000L