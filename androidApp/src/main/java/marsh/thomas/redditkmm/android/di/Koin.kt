package marsh.thomas.redditkmm.android.di

import marsh.thomas.redditkmm.android.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun appModule() = module {
    viewModel { HomeViewModel(get(), get()) }
}