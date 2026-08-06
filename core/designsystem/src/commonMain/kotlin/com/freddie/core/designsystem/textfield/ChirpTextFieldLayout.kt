package com.freddie.core.designsystem.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.freddie.core.designsystem.theme.extended

/**
 * 슬롯 API 패턴: 라벨·테두리·supportingText 같은 공통 UI는 이 레이아웃이 담당하고,
 * 실제 입력 필드는 textField 슬롯(@Composable 람다)으로 주입받아 일반/비밀번호 필드가 공유한다.
 * https://developer.android.com/develop/ui/compose/layouts/basics
 */
@Composable
fun ChirpTextFieldLayout(
    title: String? = null,
    isError: Boolean = false,
    supportingText: String? = null,
    enabled: Boolean = true,
    onFocusChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    textField: @Composable (Modifier, MutableInteractionSource) -> Unit
) {
    // InteractionSource를 부모에서 생성해 슬롯의 텍스트필드에 주입하면,
    // 자식의 포커스 상태를 여기서 State로 구독해 배경/테두리 스타일링에 쓸 수 있다.
    // https://developer.android.com/develop/ui/compose/touch-input/handling-interactions
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val isFocused by interactionSource.collectIsFocusedAsState()

    LaunchedEffect(isFocused) {
        onFocusChanged(isFocused)
    }

    val textFieldStyleModifier = Modifier
        .fillMaxWidth()
        .background(
            color = when {
                isFocused -> MaterialTheme.colorScheme.primary.copy(
                    alpha = 0.05f
                )
                enabled -> MaterialTheme.colorScheme.surface
                else -> MaterialTheme.colorScheme.extended.secondaryFill
            },
            shape = RoundedCornerShape(8.dp)
        )
        .border(
            width = 1.dp,
            color = when {
                isError -> MaterialTheme.colorScheme.error
                isFocused -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.outline
            },
            shape = RoundedCornerShape(8.dp)
        )
        .padding(12.dp)

    Column(
        modifier = modifier
    ) {
        if(title != null) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.extended.textSecondary
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        textField(textFieldStyleModifier, interactionSource)

        if(supportingText != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = supportingText,
                color = if(isError) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.extended.textTertiary
                },
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}