package com.example.inventarislab.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.inventarislab.R

@Composable
fun HalamanWelcome(navController: NavController) {

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // === BACKGROUND LENGKUNG BIRU ===
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(580.dp)
                    .background(
                        color = colorResource(id = R.color.biru),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(
                            bottomStart = 250.dp,
                            bottomEnd = 250.dp
                        )
                    )
                    .align(Alignment.TopCenter)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // === LOGO ===
                Image(
                    painter = painterResource(id = R.drawable.logo1), // Sesuaikan nama drawable Anda
                    contentDescription = "Logo Laboratorium",
                    modifier = Modifier.size(200.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Welcome",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "Aplikasi Pencatatan Peralatan dan Bahan Kimia Laboratorium",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(180.dp))

                Button(
                    onClick = { navController.navigate("login") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Login")
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = { navController.navigate("register") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                        contentColor = colorResource(id = R.color.biru),
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        color = colorResource(id = R.color.biru),
                    )
                ) {
                    Text("Register")
                }
            }
        }
    }
}