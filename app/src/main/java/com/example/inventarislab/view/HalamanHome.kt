// view/HalamanHome.kt
package com.example.inventarislab.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inventarislab.R
import com.example.inventarislab.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanHome(
    viewModel: LoginViewModel,
    onBahanClick: (Int) -> Unit,
    onPeralatanClick: (Int) -> Unit,
    onLogoutClick: () -> Unit,
    onKelolaPenggunaClick: (Int) -> Unit
) {
    val currentUser = viewModel.currentUser.collectAsState()
    val userName = currentUser.value?.nama ?: "User"
    val userRole = currentUser.value?.role ?: "user"
    val institusi = currentUser.value?.institusi ?: "Instansi"
    val namaLab = currentUser.value?.nama_lab ?: "Laboratorium"
    val labId = currentUser.value?.lab_id?.takeIf { it > 0 } ?: 1

    Scaffold(
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // === JUDUL DASHBOARD ===
            Text(
                text = "Dashboard",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF485C91),
                modifier = Modifier.padding(top = 24.dp)
            )

            // === HEADER: HALLO NAMA ===
            Text(
                text = "Hallo $userName!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF485C91),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // === WELCOME CARD DENGAN ILLUSTRASI LAB ===
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4F8)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .weight(1f)
                    ) {
                        Text(
                            text = "Welcome!",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF485C91)
                        )
                        Text(
                            text = "Anda Berada $namaLab",
                            fontSize = 14.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Text(
                            text = institusi,
                            fontSize = 12.sp,
                            color = Color.DarkGray,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Image(
                        painter = painterResource(id = R.drawable.kimia2),
                        contentDescription = "Lab Illustration",
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(topStart = 20.dp, bottomEnd = 20.dp))
                    )
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            // === CARD BAHAN ===
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2196F3)),
                onClick = { onBahanClick(labId) }
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Bahan",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Image(
                        painter = painterResource(id = R.drawable.bahan),
                        contentDescription = "Bahan",
                        modifier = Modifier.size(50.dp)
                    )
                }
            }

            // === CARD PERALATAN ===
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2196F3)),
                onClick = { onPeralatanClick(labId) }
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Peralatan",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Image(
                        painter = painterResource(id = R.drawable.alat),
                        contentDescription = "Peralatan",
                        modifier = Modifier.size(50.dp)
                    )
                }
            }

            // === CARD KELOLA PENGGUNA (HANYA ADMIN) ===
            if (userRole == "admin") {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    shape = RoundedCornerShape(18.dp),
                    elevation = CardDefaults.cardElevation(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50)),
                    onClick = { onKelolaPenggunaClick(labId) }
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Kelola Pengguna",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Image(
                            painter = painterResource(id = R.drawable.kelolapengguna),
                            contentDescription = "Kelola Pengguna",
                            modifier = Modifier.size(50.dp)
                        )
                    }
                }
            }

            // === CARD LOGOUT ===
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE57373)),
                onClick = onLogoutClick
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Logout",
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Image(
                        painter = painterResource(id = R.drawable.logout),
                        contentDescription = "Logout",
                        modifier = Modifier.size(50.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}