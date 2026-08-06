package com.example.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonGreen

@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F2027),
                        Color(0xFF203A43),
                        Color(0xFF0F172A)
                    )
                )
            )
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // App Brand Header
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(NeonGreen, ElectricBlue)
                        )
                    )
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color(0xFF121212)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = "MusicStream Logo",
                        tint = NeonGreen,
                        modifier = Modifier.size(44.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "MusicStream",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = "Stream millions of songs in Ultra HD",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 4.dp, bottom = 28.dp)
            )

            // Auth Method Card Container
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(NeonGreen.copy(alpha = 0.5f), ElectricBlue.copy(alpha = 0.5f))
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B).copy(alpha = 0.9f))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Auth Tab Switcher
                    TabRow(
                        selectedTabIndex = if (state.activeTab == AuthTab.EMAIL) 0 else 1,
                        containerColor = Color(0xFF0F172A),
                        contentColor = NeonGreen,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[if (state.activeTab == AuthTab.EMAIL) 0 else 1]),
                                color = NeonGreen
                            )
                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .fillMaxWidth()
                    ) {
                        Tab(
                            selected = state.activeTab == AuthTab.EMAIL,
                            onClick = { viewModel.setTab(AuthTab.EMAIL) },
                            modifier = Modifier.testTag("tab_email")
                        ) {
                            Text(
                                text = "Email Login",
                                modifier = Modifier.padding(vertical = 12.dp),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (state.activeTab == AuthTab.EMAIL) NeonGreen else Color.Gray
                            )
                        }
                        Tab(
                            selected = state.activeTab == AuthTab.PHONE,
                            onClick = { viewModel.setTab(AuthTab.PHONE) },
                            modifier = Modifier.testTag("tab_phone")
                        ) {
                            Text(
                                text = "Mobile OTP",
                                modifier = Modifier.padding(vertical = 12.dp),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (state.activeTab == AuthTab.PHONE) NeonGreen else Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    if (state.activeTab == AuthTab.EMAIL) {
                        // Email Auth Form
                        if (state.isSignUpMode) {
                            OutlinedTextField(
                                value = state.nameInput,
                                onValueChange = viewModel::onNameChanged,
                                label = { Text("Full Name") },
                                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = NeonGreen) },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_name"),
                                colors = inputFieldColors()
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        OutlinedTextField(
                            value = state.emailInput,
                            onValueChange = viewModel::onEmailChanged,
                            label = { Text("Email Address") },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = NeonGreen) },
                            isError = state.emailError != null,
                            supportingText = {
                                state.emailError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_email"),
                            colors = inputFieldColors()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = state.passwordInput,
                            onValueChange = viewModel::onPasswordChanged,
                            label = { Text("Password") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = NeonGreen) },
                            visualTransformation = PasswordVisualTransformation(),
                            isError = state.passwordError != null,
                            supportingText = {
                                state.passwordError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_password"),
                            colors = inputFieldColors()
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                viewModel.submitEmailAuth()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("btn_email_submit"),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = if (state.isSignUpMode) "Create Free Account" else "Sign In",
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        TextButton(
                            onClick = viewModel::toggleSignUpMode,
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .testTag("btn_toggle_signup")
                        ) {
                            Text(
                                text = if (state.isSignUpMode) "Already have an account? Log In" else "New to MusicStream? Sign Up",
                                color = ElectricBlue,
                                fontSize = 13.sp
                            )
                        }
                    } else {
                        // Phone OTP Flow
                        if (state.phoneStep == PhoneStep.ENTER_PHONE) {
                            OutlinedTextField(
                                value = state.phoneInput,
                                onValueChange = viewModel::onPhoneChanged,
                                label = { Text("10-Digit Mobile Number") },
                                prefix = { Text("+91  ", color = Color.White, fontWeight = FontWeight.Bold) },
                                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = NeonGreen) },
                                isError = state.phoneError != null,
                                supportingText = {
                                    state.phoneError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_phone"),
                                colors = inputFieldColors()
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = viewModel::sendOtp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("btn_send_otp"),
                                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Send OTP",
                                    color = Color.Black,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        } else {
                            Text(
                                text = "OTP code sent to +91 ${state.phoneInput}",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 14.sp,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            OutlinedTextField(
                                value = state.otpInput,
                                onValueChange = viewModel::onOtpChanged,
                                label = { Text("Enter 4-Digit OTP") },
                                leadingIcon = { Icon(Icons.Default.Security, contentDescription = null, tint = ElectricBlue) },
                                isError = state.otpError != null,
                                supportingText = {
                                    state.otpError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_otp"),
                                colors = inputFieldColors()
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = viewModel::verifyOtp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("btn_verify_otp"),
                                colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Verify OTP & Continue",
                                    color = Color.Black,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Guest / Quick Access Button
            OutlinedButton(
                onClick = {
                    viewModel.loginAsDemoUser()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("btn_guest_login"),
                shape = RoundedCornerShape(12.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.horizontalGradient(listOf(NeonGreen, ElectricBlue)))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = NeonGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Quick Demo Access (Skip)",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun inputFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = NeonGreen,
    unfocusedBorderColor = Color(0xFF334155),
    focusedLabelColor = NeonGreen,
    unfocusedLabelColor = Color.Gray,
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    cursorColor = NeonGreen
)
