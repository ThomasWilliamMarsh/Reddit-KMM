package marsh.thomas.redditkmm.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import marsh.thomas.redditkmm.android.home.HomeScreen
import marsh.thomas.redditkmm.android.theme.RedditTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RedditTheme {
                HomeScreen()
            }
        }
    }
}
