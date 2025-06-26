package com.example.echoes.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.echoes.R

val defaultFontFamily = FontFamily(
    Font(R.font.poppins_bold, FontWeight.Bold),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_light, FontWeight.Light)
)

val defaultTextStyle = TextStyle(
    fontFamily = defaultFontFamily,  // This is your font family (Poppins)
    fontWeight = FontWeight.Normal,  // Default weight
    fontSize = 12.sp           // Default font size
)

@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeActions: ImeAction = ImeAction.Done,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    letterSpacing: TextUnit = 0.sp,
    textAlign: TextAlign = TextAlign.Start,
    singleLine: Boolean = true,
    maxLines: Int = Int.MAX_VALUE
) {
    var isFocused by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = label,
                style = defaultTextStyle.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.code_999999)
                ),
                textAlign = textAlign
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { isFocused = it.isFocused }
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = keyboardType,
            imeAction = imeActions,
        ),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colorResource(id = R.color.code_9),
            unfocusedBorderColor = colorResource(id = R.color.code_9)
        ),
        singleLine = singleLine, // Enable multiline
        maxLines = maxLines, // Allow unlimited lines,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        textStyle = defaultTextStyle.copy(
            letterSpacing = letterSpacing,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color = colorResource(id = R.color.black),
        )
    )
}

@Composable
fun MandatoryTextFieldLabel(label: String) {
    Text(
        text = buildAnnotatedString {
            append(label)
            withStyle(style = SpanStyle(color = Color.Red)) {
                append(" *") // Asterisk for mandatory
            }
        },
        style = defaultTextStyle
    )
}

@Composable
fun FieldLabel(label: String, isMandatory: Boolean = false) {
    Text(
        text = buildAnnotatedString {
            append(label)
            if (isMandatory) {
                withStyle(style = SpanStyle(color = Color.Red)) {
                    append(" *") // Asterisk for mandatory
                }
            }

        },
        style = defaultTextStyle
    )
}

@Composable
fun <T> DropDownView(
    options: List<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit,
    optionLabel: (T) -> String,
    modifier: Modifier = Modifier
) {
    val noRippleInteractionSource = remember { MutableInteractionSource() }
    var expanded by remember {
        mutableStateOf(false)
    }

    BoxWithConstraints(modifier = modifier
        .background(Color.White)
        .clickable(
            indication = null,
            interactionSource = noRippleInteractionSource
        ) { expanded = true }
        .border(
            border = BorderStroke(
                1.dp,
                color = colorResource(id = R.color.code_9)
            ),
            shape = RoundedCornerShape(14.dp)
        )
        .background(Color.White))
    {
        val boxWidth = maxWidth
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.White),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,

            ) {
            Text(
                text = optionLabel(selectedOption), style = defaultTextStyle.copy(
                    fontSize = 16.sp,
                    color = if (optionLabel(selectedOption).contains("Select")) colorResource(id = R.color.code_999999) else colorResource(
                        id = R.color.black
                    ),
                    fontWeight = FontWeight.Normal

                )
            )
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = "Dropdown",
                tint = colorResource(id = R.color.code_666666),
                modifier = Modifier.size(24.dp)
            )
        }


        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }, modifier = Modifier
                .width(boxWidth)
                .border(
                    border = BorderStroke(
                        1.dp,
                        color = colorResource(id = R.color.code_E5E5E5)
                    ),
                    shape = RectangleShape
                )
                .background(Color.White)
            // .crop()
        ) {
            options.forEach { option ->
                val isSelected = option == selectedOption
                DropdownMenuItem(text = {
                    Text(
                        text = optionLabel(option), style = defaultTextStyle.copy(
                            fontSize = 14.sp,
                            color = if (isSelected) Color.White else colorResource(id = R.color.code_666666),
                            fontWeight = FontWeight.Normal

                        )
                    )
                }, onClick = {
                    onOptionSelected(option)
                    expanded = false
                }, modifier = Modifier.background(
                    if (isSelected) colorResource(id = R.color.code_7C4DFF) else Color.Transparent
                )
                )
            }

        }
    }
}


@Composable
fun CustomPopup(message: String, onDismiss: () -> Unit = { }, modifier: Modifier = Modifier) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false,
        )
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp) //outer padding
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        colorResource(id = R.color.white),
                    )
                    .padding(bottom = 16.dp)
            ) {
                // Badge icon
                Icon(
                    imageVector = Icons.Default.ThumbUp,
                    modifier = Modifier
                        .padding(16.dp)
                        .size(50.dp),
                    contentDescription = "Success"
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = message,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    style = defaultTextStyle.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp
                    ),
                    color = colorResource(id = R.color.code_666666)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    modifier = Modifier.clickable(
                        indication = null, // Removes ripple
                        interactionSource = remember { MutableInteractionSource() } // Required when removing indication
                    ) {
                        onDismiss()
                    },
                    style = defaultTextStyle.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    ),
                    color = colorResource(id = R.color.black),
                    text = "GOT IT"
                )
            }
        }
    }
}