package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.PupSoundEffects
import com.example.ui.theme.PawDarkText

@Composable
fun ParentalSettingsDialog(
    isOpen: Boolean,
    cadetName: String,
    isSoundEnabled: Boolean,
    onSaveName: (String) -> Unit,
    onToggleSound: (Boolean) -> Unit,
    onResetProgress: () -> Unit,
    onDismiss: () -> Unit
) {
    if (!isOpen) return

    var tempName by remember { mutableStateOf(cadetName) }
    var showConfirmReset by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Parental Settings ⚙️",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Cadet Profile Name:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PawDarkText
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = tempName,
                    onValueChange = { tempName = it },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("cadet_name_input")
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Sound Effects & Cheering",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Switch(
                        checked = isSoundEnabled,
                        onCheckedChange = { onToggleSound(it) },
                        modifier = Modifier.testTag("sound_settings_switch")
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (!showConfirmReset) {
                    PupBouncyButton(
                        text = "Reset All Stars & Badges ⚠️",
                        onClick = { showConfirmReset = true },
                        color = Color(0xFFE57373),
                        fontSize = 13,
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "trigger_reset_button"
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Are you sure? This cannot be undone.",
                            color = Color.Red,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextButton(
                                onClick = {
                                    onResetProgress()
                                    showConfirmReset = false
                                },
                                modifier = Modifier.weight(1f).testTag("confirm_reset_button")
                            ) {
                                Text("Yes, Reset", color = Color.Red, fontWeight = FontWeight.Bold)
                            }
                            TextButton(
                                onClick = { showConfirmReset = false },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Cancel")
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSaveName(tempName)
                    onDismiss()
                },
                modifier = Modifier.testTag("save_settings_button")
            ) {
                Text("Save & Close", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
