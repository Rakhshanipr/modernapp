

package com.google.samples.apps.nowinandroid.feature.foryou

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaBackground
import com.google.samples.apps.nowinandroid.core.designsystem.theme.NiaTheme
import com.google.samples.apps.nowinandroid.core.testing.util.DefaultTestDevices
import com.google.samples.apps.nowinandroid.core.testing.util.captureForDevice
import com.google.samples.apps.nowinandroid.core.testing.util.captureMultiDevice
import com.google.samples.apps.nowinandroid.core.ui.NewsFeedUiState
import com.google.samples.apps.nowinandroid.core.ui.NewsFeedUiState.Success
import com.google.samples.apps.nowinandroid.core.ui.UserNewsResourcePreviewParameterProvider
import com.google.samples.apps.nowinandroid.feature.foryou.OnboardingUiState.Loading
import com.google.samples.apps.nowinandroid.feature.foryou.OnboardingUiState.NotShown
import com.google.samples.apps.nowinandroid.feature.foryou.OnboardingUiState.Shown
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.annotation.LooperMode
import java.util.TimeZone

/**
 * Screenshot tests for the [ForYouScreen].
 */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = HiltTestApplication::class)
@LooperMode(LooperMode.Mode.PAUSED)
class ForYouScreenScreenshotTests {

    /**
     * Use a test activity to set the content on.
     */
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val userNewsResources = UserNewsResourcePreviewParameterProvider().values.first()

    @Before
    fun setTimeZone() {
        // Make time zone deterministic in tests
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    }

    @Test
    fun forYouScreenPopulatedFeed() {
        composeTestRule.captureMultiDevice("ForYouScreenPopulatedFeed") {
            NiaTheme {
                ForYouScreen(
                    isSyncing = false,
                    onboardingUiState = NotShown,
                    feedState = Success(
                        feed = userNewsResources,
                    ),
                    onTopicCheckedChanged = { _, _ -> },
                    saveFollowedTopics = {},
                    onNewsResourcesCheckedChanged = { _, _ -> },
                    onNewsResourceViewed = {},
                    onTopicClick = {},
                    deepLinkedUserNewsResource = null,
                    onDeepLinkOpened = {},
                )
            }
        }
    }

    @Test
    fun forYouScreenLoading() {
        composeTestRule.captureMultiDevice("ForYouScreenLoading") {
            NiaTheme {
                ForYouScreen(
                    isSyncing = false,
                    onboardingUiState = Loading,
                    feedState = NewsFeedUiState.Loading,
                    onTopicCheckedChanged = { _, _ -> },
                    saveFollowedTopics = {},
                    onNewsResourcesCheckedChanged = { _, _ -> },
                    onNewsResourceViewed = {},
                    onTopicClick = {},
                    deepLinkedUserNewsResource = null,
                    onDeepLinkOpened = {},
                )
            }
        }
    }

    @Test
    fun forYouScreenTopicSelection() {
        composeTestRule.captureMultiDevice("ForYouScreenTopicSelection") {
            ForYouScreenTopicSelection()
        }
    }

    @Test
    fun forYouScreenTopicSelection_dark() {
        composeTestRule.captureForDevice(
            deviceName = "phone_dark",
            deviceSpec = DefaultTestDevices.PHONE.spec,
            screenshotName = "ForYouScreenTopicSelection",
            darkMode = true,
        ) {
            ForYouScreenTopicSelection()
        }
    }

    @Test
    fun forYouScreenPopulatedAndLoading() {
        composeTestRule.captureMultiDevice("ForYouScreenPopulatedAndLoading") {
            ForYouScreenPopulatedAndLoading()
        }
    }

    @Test
    fun forYouScreenPopulatedAndLoading_dark() {
        composeTestRule.captureForDevice(
            deviceName = "phone_dark",
            deviceSpec = DefaultTestDevices.PHONE.spec,
            screenshotName = "ForYouScreenPopulatedAndLoading",
            darkMode = true,
        ) {
            ForYouScreenPopulatedAndLoading()
        }
    }

    @Composable
    private fun ForYouScreenTopicSelection() {
        NiaTheme {
            NiaBackground {
                ForYouScreen(
                    isSyncing = false,
                    onboardingUiState = Shown(
                        topics = userNewsResources.flatMap { news -> news.followableTopics }
                            .distinctBy { it.topic.id },
                    ),
                    feedState = Success(
                        feed = userNewsResources,
                    ),
                    onTopicCheckedChanged = { _, _ -> },
                    saveFollowedTopics = {},
                    onNewsResourcesCheckedChanged = { _, _ -> },
                    onNewsResourceViewed = {},
                    onTopicClick = {},
                    deepLinkedUserNewsResource = null,
                    onDeepLinkOpened = {},
                )
            }
        }
    }

    @Composable
    private fun ForYouScreenPopulatedAndLoading() {
        NiaTheme {
            NiaBackground {
                NiaTheme {
                    ForYouScreen(
                        isSyncing = true,
                        onboardingUiState = Loading,
                        feedState = Success(
                            feed = userNewsResources,
                        ),
                        onTopicCheckedChanged = { _, _ -> },
                        saveFollowedTopics = {},
                        onNewsResourcesCheckedChanged = { _, _ -> },
                        onNewsResourceViewed = {},
                        onTopicClick = {},
                        deepLinkedUserNewsResource = null,
                        onDeepLinkOpened = {},
                    )
                }
            }
        }
    }
}
