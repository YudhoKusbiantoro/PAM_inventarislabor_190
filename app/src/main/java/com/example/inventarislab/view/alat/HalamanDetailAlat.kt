// view/HalamanDetailAlat.kt
package com.example.inventarislab.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.inventarislab.R
// ✅ Ganti import ViewModel
import com.example.inventarislab.viewmodel.alat.AlatDetailViewModel
import com.example.inventarislab.viewmodel.alat.AlatDeleteViewModel
import com.example.inventarislab.viewmodel.provider.PenyediaViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanDetailAlat(
    alatId: Int,
    navController: NavHostController,
    onBackClick: () -> Unit
) {
    // ✅ Gunakan ViewModel terpisah
    val detailViewModel: AlatDetailViewModel = viewModel(factory = PenyediaViewModel.Factory)
    val deleteViewModel: AlatDeleteViewModel = viewModel(factory = PenyediaViewModel.Factory)

    val alatState by detailViewModel.alatDetail.collectAsState()
    val deleteResult by deleteViewModel.deleteResult.collectAsState()

    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(alatId) {
        detailViewModel.loadAlatById(alatId)
    }

    LaunchedEffect(deleteResult) {
        if (deleteResult != null) {
            delay(300)
            deleteViewModel.resetDeleteResult()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detail Alat",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2196F3)
                )
            )
        },
        modifier = Modifier.background(Color(0xFFF5F7FA)) // Background abu-abu muda
    ) { padding ->

        if (alatState == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF2196F3))
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // CARD UTAMA - DETAIL ALAT
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // LOGO + NAMA + STATUS DI ATAS
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Logo
                            Image(
                                painter = painterResource(id = R.drawable.logoalat),
                                contentDescription = "Logo Alat",
                                modifier = Modifier.size(64.dp).clip(RoundedCornerShape(12.dp))
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            // Nama & Status
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = alatState!!.nama,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                // Status Badge
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            color = when (alatState!!.kondisi.lowercase()) {
                                                "expired" -> Color(0xFFFFEBEE)
                                                "rusak" -> Color(0xFFFFF3E0)
                                                else -> Color(0xFFE8F5E9)
                                            }
                                        )
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = alatState!!.kondisi.uppercase(),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = when (alatState!!.kondisi.lowercase()) {
                                            "expired" -> Color.Red
                                            "rusak" -> Color(0xFFFF9800)
                                            else -> Color.Green
                                        }
                                    )
                                }
                            }
                        }

                        Divider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = Color(0xFFBDBDBD)
                        )

                        // JUMLAH
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Jumlah", fontWeight = FontWeight.Medium, color = Color.Gray)
                            Text(alatState!!.jumlah.toString(), fontWeight = FontWeight.Bold, color = Color.Black)
                        }

                        // KALIBRASI
                        if (!alatState!!.terakhir_kalibrasi.isNullOrEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Kalibrasi", fontWeight = FontWeight.Medium, color = Color.Gray)
                                Text(
                                    "${alatState!!.terakhir_kalibrasi} (${alatState!!.interval_kalibrasi} ${alatState!!.satuan_interval})",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        }

                        // LABORATORIUM
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Laboratorium", fontWeight = FontWeight.Medium, color = Color.Gray)
                            Text(alatState!!.nama_lab ?: "Tidak diketahui", fontWeight = FontWeight.Bold, color = Color.Black)
                        }

                        // INSTITUSI
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Institusi", fontWeight = FontWeight.Medium, color = Color.Gray)
                            Text(alatState!!.institusi ?: "Tidak diketahui", fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                    }
                }

                // 🔘 TOMBOL EDIT & HAPUS — SAMA PERSIS DENGAN HALAMAN BAHAN
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            navController.navigate("edit_alat/${alatState!!.id}")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                        Spacer(Modifier.width(8.dp))
                        Text("Edit", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    }

                    Button(
                        onClick = { showDeleteDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373)),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Hapus")
                        Spacer(Modifier.width(8.dp))
                        Text("Hapus", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }

    // DIALOG HAPUS
    if (showDeleteDialog && alatState != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Konfirmasi Hapus") },
            text = { Text("Hapus alat ${alatState!!.nama}? Tindakan ini tidak dapat dibatalkan.") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    deleteViewModel.deleteAlat(
                        alatState!!.id,
                        alatState!!.lab_id
                    )
                }) {
                    Text("Hapus", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}