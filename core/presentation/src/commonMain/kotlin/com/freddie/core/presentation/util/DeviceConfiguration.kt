package com.freddie.core.presentation.util

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowSizeClass.Companion.HEIGHT_DP_EXPANDED_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.HEIGHT_DP_MEDIUM_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND

// 기기 종류나 회전 상태가 아니라 현재 앱 창의 가용 크기로 레이아웃을 결정하려고 이 API를 사용합니다.
// 창 크기 변화를 다시 반영하므로 분할 화면, 폴더블, 데스크톱 리사이즈에서도 같은 분기가 동작합니다.
// https://developer.android.com/develop/adaptive-apps/guides/get-started-with-adaptive-apps
@Composable
fun currentDeviceConfiguration(): DeviceConfiguration {
    val windowSize = currentWindowAdaptiveInfo().windowSizeClass
    return DeviceConfiguration.fromWindowSizeClass(windowSize)
}

// Material breakpoint를 앱이 사용하는 5개 레이아웃 상태로 변환해 화면마다 dp 비교를 반복하지 않도록 합니다.
// 분류 기준을 한곳에서 일관되게 관리할 수 있으며, 값은 실제 기기 종류가 아닌 현재 창의 UI 구성입니다.
// https://developer.android.com/reference/androidx/window/core/layout/WindowSizeClass
enum class DeviceConfiguration {
    MOBILE_PORTRAIT,
    MOBILE_LANDSCAPE,
    TABLET_PORTRAIT,
    TABLET_LANDSCAPE,
    DESKTOP;

    companion object {
        fun fromWindowSizeClass(windowSizeClass: WindowSizeClass): DeviceConfiguration {
            return with(windowSizeClass) {
                when {
                    minWidthDp < WIDTH_DP_MEDIUM_LOWER_BOUND &&
                            minHeightDp >= HEIGHT_DP_MEDIUM_LOWER_BOUND
                        -> MOBILE_PORTRAIT

                    minWidthDp >= WIDTH_DP_EXPANDED_LOWER_BOUND &&
                            minHeightDp < HEIGHT_DP_MEDIUM_LOWER_BOUND
                        -> MOBILE_LANDSCAPE

                    minWidthDp in WIDTH_DP_MEDIUM_LOWER_BOUND..WIDTH_DP_EXPANDED_LOWER_BOUND &&
                            minHeightDp >= HEIGHT_DP_EXPANDED_LOWER_BOUND
                        -> TABLET_PORTRAIT

                    minWidthDp >= WIDTH_DP_EXPANDED_LOWER_BOUND &&
                            minHeightDp in HEIGHT_DP_MEDIUM_LOWER_BOUND..HEIGHT_DP_EXPANDED_LOWER_BOUND
                        -> TABLET_LANDSCAPE

                    else -> DESKTOP
                }
            }
        }
    }
}
