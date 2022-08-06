package marsh.thomas.redditkmm.common.di

import com.russhwolf.settings.Settings
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import marsh.thomas.redditkmm.common.network.AuthApi
import marsh.thomas.redditkmm.common.network.AuthApiImpl
import marsh.thomas.redditkmm.common.network.RedditApi
import marsh.thomas.redditkmm.common.network.RedditApiImpl
import marsh.thomas.redditkmm.common.platformModule
import marsh.thomas.redditkmm.common.repository.AuthRepository
import marsh.thomas.redditkmm.common.repository.AuthRepositoryImpl
import marsh.thomas.redditkmm.common.repository.PostRepository
import marsh.thomas.redditkmm.common.repository.PostRepositoryImpl
import marsh.thomas.redditkmm.common.settings.AuthSettings
import marsh.thomas.redditkmm.common.settings.AuthSettingsImpl
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}): KoinApplication {
    return startKoin {
        appDeclaration()
        modules(settingsModule(), networkModule(), platformModule())
    }
}

fun settingsModule() = module {
    single { Settings() }
    single<AuthSettings> { AuthSettingsImpl(get()) }
}

fun networkModule() = module {
    single {
        Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
        }
    }
    single(named("authenticated")) { createAuthenticatedHttpClient(get(), get(), get(), get()) }
    single(named("unauthenticated")) { createUnauthenticatedClient(get(), get()) }
    single<RedditApi> { RedditApiImpl(get(qualifier = named("authenticated"))) }
    single<AuthApi> { AuthApiImpl(get(qualifier = named("unauthenticated")))}
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<PostRepository> { PostRepositoryImpl(get()) }
}

private fun createAuthenticatedHttpClient(
    authRepository: AuthRepository,
    httpClientEngine: HttpClientEngine,
    authSettings: AuthSettings,
    json: Json
) =
    HttpClient(httpClientEngine) {
        install(ContentNegotiation) {
            json(json)
        }
        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens(accessToken = authSettings.accessToken, "")
                }

                refreshTokens {
                    authRepository.fetchAccessToken()
                    BearerTokens(accessToken = authSettings.accessToken, "")
                }
            }
        }
    }

private fun createUnauthenticatedClient(httpClientEngine: HttpClientEngine, json: Json) =
    HttpClient(httpClientEngine) {
        install(ContentNegotiation) {
            json(json)
        }
        install(UserAgent) {
            agent = "android/1.0/kmm-reddit/ktor-client"
        }
        install(Auth) {
            basic {
                credentials {
                    BasicAuthCredentials(username = "3W-h4rgSsX_rdd9vv2Ju6A", password = "")
                }
            }
        }
    }