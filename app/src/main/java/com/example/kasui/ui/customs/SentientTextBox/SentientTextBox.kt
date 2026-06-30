package com.example.kasui.ui.customs.SentientTextBox

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kasui.ui.UncutSans
import com.example.kasui.ui.surfaceColor
import com.example.kasui.ui.surfaceHighColor
import com.example.kasui.ui.surfaceMidColor
import com.example.kasui.ui.variantColor
import com.example.kasui.ui.variantHighColor


class SentientTextBoxStateShape(

) : SentientTextBoxImpl {
    @Composable
    override fun sentientTextBoxAsState(
        indentWidth: Dp,
        indentHeight: Dp,
        indentPaddingFromStart: Dp,
        indentCornerRadius: Dp,
        cornerRadius: Dp,
        showIndent: Boolean
    ): State<Shape> {


        val animatedIndentWidth = animateDpAsState(targetValue = indentWidth)
        val animateIndentHeight = animateDpAsState(
            targetValue = if (showIndent) {
                indentHeight
            } else {
                0.dp
            }
        )

        val initShape = remember {
            SentientTextBoxShape(
                indentWidth = indentWidth,
                indentHeight = indentHeight,
                indentSpaceFromStart = indentPaddingFromStart,
                indentCornerRadius = indentCornerRadius,
                cornerRadius = cornerRadius
            )
        }

        return produceState(
            initialValue = initShape,
            key1 = animatedIndentWidth.value,
            key2 = animateIndentHeight.value
        ) {
            this.value = this.value.copy(
                indentWidth = animatedIndentWidth.value,
                indentHeight = animateIndentHeight.value
            )
        }
    }

}


@Composable
fun SentientTextBox(
    modifier: Modifier = Modifier,
    textFieldValue: String,
    labelText: String,
    labelTextSize: Int = 10,
    textFieldSize: Int = 20,
    indentWidth: Dp = 0.dp,
    indentHeight: Dp = 10.dp,
    indentPaddingFromStart: Dp = 40.dp,
    indentCornerRadius: Dp = 10.dp,
    boxCornerRadius: Dp = 60.dp,
    textFieldWidth: Dp = TextFieldDefaults.MinWidth,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    boxColor: Color = surfaceHighColor,
    textBoxColor: Color = variantHighColor,
    textColor: Color = surfaceColor,
    labelTextColor: Color = variantColor,
    singleLine: Boolean = false,
    autoWidth: Boolean = true,
    onValueChanged: (String) -> Unit

) {

    val density = LocalDensity.current

    var autoIndentWidth by remember(indentWidth) {
        mutableStateOf(indentWidth)
    }

    val animatedIndentWidth = animateDpAsState(targetValue = autoIndentWidth)

    var showTopLabel by remember {
        mutableStateOf(
            textFieldValue.isNotBlank()
        )
    }

    val stateIndentHeight by remember(showTopLabel) {
        mutableStateOf(
            if (showTopLabel) {
                indentHeight
            } else {
                0.dp
            }
        )
    }
    val animatedIndentHeight = animateDpAsState(targetValue = stateIndentHeight)


    val shape = SentientTextBoxStateShape().sentientTextBoxAsState(
        indentWidth = animatedIndentWidth.value,
        indentHeight = indentHeight,
        indentPaddingFromStart = indentPaddingFromStart,
        indentCornerRadius = indentCornerRadius,
        cornerRadius = boxCornerRadius,
        showIndent = showTopLabel
    )

    // Main Box
    Box(
        modifier = modifier
            .background(boxColor, shape.value)
            .animateContentSize()
    ) {
        Column {
            Box(
                modifier
                    .height(animatedIndentHeight.value)
                    .width(animatedIndentWidth.value + indentPaddingFromStart)
                    .padding(start = indentPaddingFromStart + indentCornerRadius),
                contentAlignment = Alignment.Center
            ) {

                val labelVisibilityColor by remember(showTopLabel, labelTextColor) {
                    mutableStateOf(
                        if (showTopLabel) {
                            labelTextColor
                        } else {
                            boxColor
                        }
                    )
                }

                val animatedLabelTextColor = animateColorAsState(targetValue = labelVisibilityColor)


                Text(
                    text = labelText,
                    color = animatedLabelTextColor.value,
                    fontSize = labelTextSize.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = UncutSans
                )

                MeasureUnconstrainedViewWidth(
                    viewToMeasure = {
                        Text(
                            text = labelText,
                            color = animatedLabelTextColor.value,
                            fontSize = labelTextSize.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = UncutSans
                        )
                    }
                ) { measuredWidth ->
                    autoIndentWidth = measuredWidth + 12.dp

                }


            }

            Box(
                modifier = Modifier
                    .padding(vertical = 6.dp, horizontal = 6.dp)
            ) {
                TextField(
                    value = textFieldValue,
                    onValueChange = {
                        onValueChanged(it)
                        showTopLabel = it.isNotBlank()
                    },
                    modifier = Modifier
                        .width(textFieldWidth),
                    visualTransformation = visualTransformation,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor,
                        unfocusedContainerColor = textBoxColor,
                        focusedContainerColor = textBoxColor,
                        disabledIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent
                    ),
                    textStyle = TextStyle(
                        fontFamily = UncutSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = textFieldSize.sp,
                        color = textColor
                    ),
                    shape = RoundedCornerShape(36.dp),
                    maxLines = if (singleLine) {
                        1
                    } else {
                        50
                    },
                    placeholder = {
                        Text(
                            text = labelText,
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp,
                            color = labelTextColor
                        )
                    }
                )
            }
        }

    }


}

@Composable
private fun MeasureUnconstrainedViewWidth(
    viewToMeasure: @Composable () -> Unit,
    content: @Composable (measuredWidth: Dp) -> Unit,
) {
    SubcomposeLayout { constraints ->
        val measuredList = subcompose("viewToMeasure", viewToMeasure)

        if (measuredList.isNotEmpty()) {
            val measuredWidth = measuredList.first().measure(Constraints()).width.toDp()

            val contentPlaceableList = subcompose("content") {
                content(measuredWidth)
            }

        }
        layout(0, 0) {}

    }
}

@Preview
@Composable
fun PreviewSentientTextBoxShape() {

    var textValue by remember {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        SentientTextBox(
            modifier = Modifier,
            textFieldValue = textValue,
            onValueChanged = {
                textValue = it
            },
            labelText = "Enter Date",
            indentWidth = 70.dp
        )


    }
}