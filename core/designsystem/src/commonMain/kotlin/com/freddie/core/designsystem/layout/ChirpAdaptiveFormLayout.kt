package com.freddie.core.designsystem.layout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.freddie.core.designsystem.brand.ChirpBrandLogo
import com.freddie.core.designsystem.theme.ChirpTheme
import com.freddie.core.designsystem.theme.extended
import com.freddie.core.presentation.util.DeviceConfiguration
import com.freddie.core.presentation.util.currentDeviceConfiguration

/**
 * WindowSizeClass 기반 적응형 레이아웃: 같은 콘텐츠(logo/form 슬롯)를 받아
 * 기기 구성(DeviceConfiguration)별로 아예 다른 레이아웃 트리를 구성한다 — 크기 값 분기가 아닌 구조 분기.
 * https://developer.android.com/develop/ui/compose/layouts/adaptive/use-window-size-classes
 */
@Composable
fun ChirpAdaptiveFormLayout(
    modifier: Modifier = Modifier,
    headerText: String,
    errorText: String? = null,
    logo: @Composable () -> Unit,
    formContent: @Composable ColumnScope.() -> Unit,
) {
    val configuration = currentDeviceConfiguration()
    val headerColor = if (configuration == DeviceConfiguration.MOBILE_LANDSCAPE) {
        MaterialTheme.colorScheme.onBackground
    } else {
        MaterialTheme.colorScheme.extended.textPrimary
    }

    when (configuration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> {
            ChirpSurface(
                // consumeWindowInsets: 해당 인셋을 "소비했다"고 표시해, 자식 트리의
                // inset-aware API(imePadding, safeDrawingPadding 등)가 같은 인셋을 중복 적용하지 않게 한다.
                // https://developer.android.com/develop/ui/compose/layouts/insets
                modifier = modifier
                    .consumeWindowInsets(WindowInsets.navigationBars)
                    .consumeWindowInsets(WindowInsets.displayCutout),
                header = {
                    Spacer(modifier = Modifier.height(32.dp))
                    logo()
                    Spacer(modifier = Modifier.height(32.dp))
                }
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                AuthHeaderSection(
                    headerText = headerText,
                    headerColor = headerColor,
                    errorText = errorText
                )
                Spacer(modifier = Modifier.height(24.dp))
                formContent()
            }
        }
        DeviceConfiguration.MOBILE_LANDSCAPE -> {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(WindowInsets.displayCutout)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    logo()
                    AuthHeaderSection(
                        headerText = headerText,
                        headerColor = headerColor,
                        errorText = errorText
                    )
                }
                ChirpSurface(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    formContent()
                }
            }
        }
        DeviceConfiguration.TABLET_PORTRAIT,
        DeviceConfiguration.TABLET_LANDSCAPE,
        DeviceConfiguration.DESKTOP -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(top = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                logo()
                Column(
                    modifier = Modifier
                        .widthIn(max = 480.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(32.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AuthHeaderSection(
                        headerText = headerText,
                        headerColor = headerColor,
                        errorText = errorText
                    )
                    formContent()
                }
            }
        }
    }
}

@Composable
fun ColumnScope.AuthHeaderSection(
    headerText: String,
    headerColor: Color,
    errorText: String? = null,
) {
    Text(
        text = headerText,
        style = MaterialTheme.typography.titleLarge,
        color = headerColor,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
    // AnimatedVisibility: visible이 false로 바뀌어도 exit 애니메이션이 끝날 때까지
    // content가 composition에 남는다 — 그래서 람다 안에서 null 재확인이 필요하다.
    // https://developer.android.com/develop/ui/compose/animation/composables-modifiers#animatedvisibility
    AnimatedVisibility(
        visible = errorText != null
    ) {
        if (errorText != null) {
            Text(
                text = errorText,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
@Preview
fun ChirpAdaptiveFormLayoutLightPreview() {
    ChirpTheme {
        ChirpAdaptiveFormLayout(
            headerText = "Welcome to Chirp!",
            errorText = "Login failed",
            logo = { ChirpBrandLogo() },
            formContent = {
                Text(
                    text = "Sample form title",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Sample form title 2",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        )
    }
}

@Composable
@Preview
fun ChirpAdaptiveFormLayoutDarkPreview() {
    ChirpTheme(darkTheme = true) {
        ChirpAdaptiveFormLayout(
            headerText = "Welcome to Chirp!",
            errorText = "Login failed",
            logo = { ChirpBrandLogo() },
            formContent = {
                Text(
                    text = "Sample form title",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Sample form title 2",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        )
    }
}