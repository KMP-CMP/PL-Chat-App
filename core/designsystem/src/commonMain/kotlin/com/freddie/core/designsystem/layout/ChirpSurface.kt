package com.freddie.core.designsystem.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import chirp.core.designsystem.generated.resources.Res
import chirp.core.designsystem.generated.resources.logo_chirp
import com.freddie.core.designsystem.theme.ChirpTheme
import org.jetbrains.compose.resources.vectorResource

/**
 * 슬롯 람다에 ColumnScope 리시버를 붙이면(@Composable ColumnScope.() -> Unit)
 * 호출부가 슬롯 안에서 weight, align 같은 Column 전용 Modifier를 그대로 쓸 수 있다.
 * https://kotlinlang.org/docs/lambdas.html#function-literals-with-receiver
 */
@Composable
fun ChirpSurface(
    modifier: Modifier = Modifier,
    header: @Composable ColumnScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            header()
            // Surface는 color에 매칭되는 contentColor(LocalContentColor)를 자동으로 내려주고
            // shape로 내부 콘텐츠를 클리핑한다 — 내부 Text/Icon 색을 일일이 지정하지 않아도 되는 이유.
            // https://developer.android.com/develop/ui/compose/designsystems/material3
            Surface(
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    content()
                }
            }
        }
    }
}

@Composable
@Preview
fun ChirpSurfacePreview() {
    ChirpTheme {
        ChirpSurface(
            modifier = Modifier.fillMaxSize(),
            header = {
                Icon(
                    imageVector = vectorResource(Res.drawable.logo_chirp),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(vertical = 32.dp)
                )
            }
        ) {
            Text(
                text = "Welcome to Chirp!",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(vertical = 40.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}