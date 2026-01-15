// view/HalamanAlat.kt
package com.example.inventarislab.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
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
import com.example.inventarislab.modeldata.Alat
import com.example.inventarislab.viewmodel.AlatViewModel
import com.example.inventarislab.viewmodel.provider.PenyediaViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanAlat(
    labId: Int,
    navController: NavHostController,
    onBackClick: () -> Unit
) {
    val viewModel: AlatViewModel = viewModel(factory = PenyediaViewModel.Factory)
    val alat by viewModel.alat.collectAsState()
    val notification by viewModel.notification.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    var selectedFilter by remember { mutableStateOf("Semua") }

    LaunchedEffect(labId) {
        viewModel.loadAlatByLabId(labId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Daftar Alat",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Kimia Laboratorium",
                            fontSize = 16.sp,
                            color = Color(0xFFBDBDBD)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2196F3)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("tambah_alat/$labId")
                },
                containerColor = Color(0xFF2196F3),
                content = {
                    Icon(Icons.Default.Add, contentDescription = "Tambah Alat", tint = Color.White)
                }
            )
        },
        modifier = Modifier.background(Color(0xFFF5F7FA))
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // NOTIFIKASI CARD - SUSUNAN 2 KOLOM (KIRI: 2 BARIS, KANAN: 1 BARIS)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Notifikasi",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    val n = notification ?: AlatViewModel.Notification(0, 0, 0)

                    // ROW UNTUK 2 KOLOM
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // KOLOM KIRI: 2 BARIS
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Baris 1: Total Alat
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = R.drawable.all),
                                    contentDescription = "Total Alat",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Total Alat : ${n.total}")
                            }

                            // Baris 2: Perlu Kalibrasi
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = R.drawable.hampirexpired),
                                    contentDescription = "Perlu Kalibrasi",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Perlu Kalibrasi : ${n.expired}")
                            }
                        }

                        // KOLOM KANAN: 1 BARIS (centered vertically)
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = R.drawable.rusak),
                                    contentDescription = "Kondisi Rusak",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Kondisi Rusak : ${n.rusak}")
                            }
                        }
                    }
                }
            }

            // SEARCH BAR
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Cari Alat") },
                modifier = Modifier.fillMaxWidth()
            )
            // FILTER BUTTONS - HANYA UI, BELUM ADA LOGIKA
            // FILTER BUTTONS — DISAMAKAN DENGAN HALAMAN BAHAN
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedFilter == "Semua",
                    onClick = { selectedFilter = "Semua" },
                    label = { Text("Semua") },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = if (selectedFilter == "Semua") Color(0xFFE8F5E9) else Color(0xFFF5F5F5),
                        labelColor = if (selectedFilter == "Semua") Color(0xFF2E7D32) else Color.Gray
                    )
                )

                FilterChip(
                    selected = selectedFilter == "Perlu Kalibrasi",
                    onClick = { selectedFilter = "Perlu Kalibrasi" },
                    label = { Text("Perlu Kalibrasi") },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = if (selectedFilter == "Perlu Kalibrasi") Color(0xFFFFEBEE) else Color(0xFFF5F5F5),
                        labelColor = if (selectedFilter == "Perlu Kalibrasi") Color.Red else Color.Gray
                    )
                )

                FilterChip(
                    selected = selectedFilter == "Rusak",
                    onClick = { selectedFilter = "Rusak" },
                    label = { Text("Rusak") },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = if (selectedFilter == "Rusak") Color(0xFFFFF3E0) else Color(0xFFF5F5F5),
                        labelColor = if (selectedFilter == "Rusak") Color(0xFFFF9800) else Color.Gray
                    )
                )
            }

            // LIST ALAT - TANPA FILTER DULU
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(alat.filter { item ->
                    val matchesSearch = item.nama.contains(searchQuery, ignoreCase = true)

                    val matchesFilter = when (selectedFilter) {
                        "Semua" -> true
                        "Perlu Kalibrasi" -> {
                            try {
                                val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                val lastCalibration = formatter.parse(item.terakhir_kalibrasi)
                                val today = Date()

                                val intervalMonths = try {
                                    item.interval_kalibrasi.toInt()
                                } catch (e: NumberFormatException) {
                                    0
                                }

                                val calendar = Calendar.getInstance()
                                calendar.time = lastCalibration
                                calendar.add(Calendar.MONTH, intervalMonths)
                                val nextCalibration = calendar.time

                                today.after(nextCalibration)
                            } catch (e: Exception) {
                                false
                            }
                        }
                        "Rusak" -> item.kondisi == "Rusak"
                        else -> true
                    }

                    matchesSearch && matchesFilter
                }) { alatItem ->
                    AlatCard(
                        alat = alatItem,
                        onClick = {
                            navController.navigate("detail_alat/${alatItem.id}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AlatCard(
    alat: Alat,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.weight(1f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logoalat),
                    contentDescription = "Logo Alat",
                    modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = alat.nama,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.Black
                    )
                    Text(
                        text = "Jumlah: ${alat.jumlah}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    if (!alat.terakhir_kalibrasi.isNullOrEmpty()) {
                        Text(
                            text = "Kalibrasi: ${alat.terakhir_kalibrasi} (${alat.interval_kalibrasi} ${alat.satuan_interval})",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.DarkGray
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        color = when (alat.kondisi) {
                            "Expired" -> Color(0xFFFFEBEE)
                            "Rusak" -> Color(0xFFFFF3E0)
                            else -> Color(0xFFE8F5E9)
                        }
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = alat.kondisi.uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = when (alat.kondisi) {
                        "Expired" -> Color.Red
                        "Rusak" -> Color(0xFFFF9800)
                        else -> Color.Green
                    }
                )
            }
        }
    }
}