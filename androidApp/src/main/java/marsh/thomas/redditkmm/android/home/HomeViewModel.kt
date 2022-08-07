package marsh.thomas.redditkmm.android.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import marsh.thomas.redditkmm.common.repository.Post
import marsh.thomas.redditkmm.common.repository.PostRepository
import marsh.thomas.redditkmm.common.util.ValidUrlUseCase

class HomeViewModel(
    private val postsRepository: PostRepository,
    private val validUrl: ValidUrlUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow(ViewState())
    val viewState = _viewState.asStateFlow()

    init {
        postsRepository.posts
            .onStart { fetchPage() }
            .onEach {
                print(it)
                onPage(it)
            }
            .catch { onError() }
            .launchIn(viewModelScope)
    }

    fun onAction(action: UiAction) {
        when (action) {
            is UiAction.PostTapped -> _viewState.update {
                it.copy(navigationState = Screen.PostDetail(action.url))
            }
            is UiAction.SearchTermChanged -> _viewState.update {
                it.copy(searchTerm = action.term)
            }
            UiAction.ScrolledToBottom -> fetchPage()
            UiAction.HandledNavigation -> _viewState.update { it.copy(navigationState = Screen.Posts) }
            UiAction.ErrorDisplayed -> _viewState.update { it.copy(showError = false) }
            UiAction.SearchTapped -> {
                postsRepository.subreddit = _viewState.value.searchTerm
                fetchPage()
            }
        }
    }

    private fun fetchPage() = viewModelScope.launch {
        try {
            onLoading()
            postsRepository.fetchPage()
        } catch (throwable: Throwable) {
            onError()
        }
    }

    private fun onPage(data: List<Post>) {
        _viewState.update { viewState ->
            viewState.copy(
                posts = data.toViewModel(),
                isLoading = false,
                isLoadingMore = false
            )
        }
    }

    private fun onLoading() {
        _viewState.update {
            val isInitialPage = it.posts.isEmpty()
            it.copy(isLoading = isInitialPage, isLoadingMore = !isInitialPage, showError = false)
        }
    }

    private fun onError() {
        _viewState.update {
            it.copy(isLoading = false, isLoadingMore = false, showError = true)
        }
    }

    private fun List<Post>.toViewModel(): List<ViewState.Post> {
        return map {
            ViewState.Post(
                title = it.title,
                imageUrl = if (validUrl(it.imageUrl)) it.imageUrl else "",
                url = it.url,
                ups = it.ups.toString(),
            )
        }
    }

    sealed class Screen {
        object Posts : Screen()
        data class PostDetail(val url: String) : Screen()
    }

    data class ViewState(
        val isLoading: Boolean = true,
        val isLoadingMore: Boolean = false,
        val showError: Boolean = false,
        val posts: List<Post> = emptyList(),
        val searchTerm: String = "",
        val navigationState: Screen = Screen.Posts
    ) {
        data class Post(val title: String, val imageUrl: String, val url: String, val ups: String)
    }

    sealed class UiAction {
        data class PostTapped(val url: String) : UiAction()
        data class SearchTermChanged(val term: String) : UiAction()
        object SearchTapped : UiAction()
        object HandledNavigation : UiAction()
        object ErrorDisplayed : UiAction()
        object ScrolledToBottom : UiAction()
    }
}