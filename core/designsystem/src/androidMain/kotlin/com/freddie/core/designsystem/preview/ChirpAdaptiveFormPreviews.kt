package com.freddie.core.designsystem.preview

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.freddie.core.designsystem.brand.ChirpBrandLogo
import com.freddie.core.designsystem.layout.ChirpAdaptiveFormLayout
import com.freddie.core.designsystem.theme.ChirpTheme

/**
 * @PreviewScreenSizes/@PreviewLightDark 멀티프리뷰: 프리뷰 하나로 폰·폴더블·태블릿 × 라이트/다크
 * 조합을 한 번에 렌더링한다. androidx.compose.ui.tooling 전용 애노테이션이라 commonMain의
 * CMP @Preview로는 못 쓰고, 이 파일처럼 androidMain에 둬야 한다.
 * https://developer.android.com/develop/ui/compose/tooling/previews
 */
@Composable
@PreviewLightDark
@PreviewScreenSizes
fun ChirpAdaptiveFormLayoutLightPreview() {
    ChirpTheme {
        ChirpAdaptiveFormLayout(
            headerText = "Welcome to Chirp!",
            errorText = "Login failed!",
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