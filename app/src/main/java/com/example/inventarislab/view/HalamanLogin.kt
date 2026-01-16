// view/HalamanLogin.kt
package com.example.inventarislab.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.inventarislab.R
import com.example.inventarislab.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanLogin(
    navController: NavController,
    viewModel: LoginViewModel
) {
    val context = LocalContext.current

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val loginResult by viewModel.loginResult.collectAsState()

    LaunchedEffect(loginResult) {
        if (loginResult?.status == "success") {
            Toast.makeText(context, "Login berhasil!", Toast.LENGTH_SHORT).show()
            navController.navigate("home")
        }
    }

    // Background putih bersih
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ILLUSTRASI BESAR DI ATAS
            Image(
                painter = painterResource(id = R.drawable.kimia2), // Ganti dengan nama gambar Anda
                contentDescription = "Login Illustration",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.7f)
                    .clip(RoundedCornerShape(20.dp))
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Login",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF485C91)
            )

            Text(
                text = "Silakan masukkan username dan password akun Anda.",
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // CARD LOGIN - DENGAN SHADOW & PADDING
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // USERNAME FIELD
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username") },
                        leadingIcon = {
                            Icon(
                                imageVector = androidx.compose.material.icons.Icons.Default.Person,
                                contentDescription = "Username",
                                tint = Color.Gray
                            )
                        },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                        shape = RoundedCornerShape(12.dp)
                    )
                    // PASSWORD FIELD
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        leadingIcon = {
                            Icon(
                                imageVector = androidx.compose.material.icons.Icons.Default.Lock,
                                contentDescription = "Password",
                                tint = Color.Gray
                            )
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // ERROR MESSAGE
                    if (loginResult?.status == "error") {
                        Text(
                            text = loginResult?.message.orEmpty(),
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // LOGIN BUTTON
                    Button(
                        onClick = {
                            // ✅ TRIM INPUT SEBELUM VALIDASI
                            val trimmedUsername = username.trim()
                            val trimmedPassword = password.trim()

                            if (trimmedUsername.isBlank() || trimmedPassword.isBlank()) {
                                Toast.makeText(context, "Semua field harus diisi.", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            viewModel.login(trimmedUsername, trimmedPassword)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF485C91))
                    ) {
                        Text("Login", color = Color.White, fontWeight = FontWeight.Medium)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // LINK SIGN UP
                    Row {
                        Text("Sudah Punya Akun? ", color = Color.Gray)
                        Text(
                            text = "Registrasi",
                            color = Color(0xFF485C91),
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable { navController.navigate("register") }
                        )
                    }
                }
            }
        }
    }
}