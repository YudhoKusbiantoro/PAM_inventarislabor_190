// view/HalamanEditBahan.kt
package com.example.inventarislab.view

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
// ✅ Ganti import ViewModel
import com.example.inventarislab.viewmodel.bahan.BahanDetailViewModel
import com.example.inventarislab.viewmodel.bahan.BahanUpdateViewModel
import com.example.inventarislab.viewmodel.provider.PenyediaViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanEditBahan(
    bahanId: Int,
    navController: NavHostController,
    onBackClick: () -> Unit
) {
    // ✅ Gunakan ViewModel terpisah
    val detailViewModel: BahanDetailViewModel = viewModel(factory = PenyediaViewModel.Factory)
    val updateViewModel: BahanUpdateViewModel = viewModel(factory = PenyediaViewModel.Factory)

    val bahanState by detailViewModel.bahanDetail.collectAsState()
    val updateResult by updateViewModel.updateResult.collectAsState()

    var isUpdating by remember { mutableStateOf(false) }

    var nama by remember { mutableStateOf("") }
    var volume by remember { mutableStateOf("") }
    var expired by remember { mutableStateOf("") }
    var kondisi by remember { mutableStateOf("") }
    var kondisiExpanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    // Load data awal
    LaunchedEffect(bahanId) {
        detailViewModel.loadBahanById(bahanId)
    }

    // Isi form dengan data awal
    LaunchedEffect(bahanState) {
        bahanState?.let { b ->
            nama = b.nama
            volume = b.volume
            expired = b.expired
            kondisi = b.kondisi
        }
    }

    LaunchedEffect(updateResult) {
        val result = updateResult
        if (result != null) {
            isUpdating = false
            if (result.status == "success") {
                Toast.makeText(context, "Bahan berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                onBackClick()
            } else {
                Toast.makeText(
                    context,
                    result.message ?: "Gagal memperbarui bahan",
                    Toast.LENGTH_SHORT
                ).show()
                updateViewModel.resetUpdateResult()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Bahan") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2196F3),
                    scrolledContainerColor = Color(0xFF2196F3),
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = nama,
                onValueChange = { nama = it },
                label = { Text("Nama Bahan") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = volume,
                onValueChange = { volume = it },
                label = { Text("Volume") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = expired,
                onValueChange = { /* readOnly */ },
                label = { Text("Expired") },
                trailingIcon = {
                    IconButton(onClick = {
                        DatePickerDialog(
                            context,
                            { _, y, m, d ->
                                expired = "%04d-%02d-%02d".format(y, m + 1, d)
                            },
                            year, month, day
                        ).show()
                    }) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = "Pilih Tanggal")
                    }
                },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = kondisiExpanded,
                onExpandedChange = { kondisiExpanded = it }
            ) {
                OutlinedTextField(
                    value = kondisi,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Kondisi") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = kondisiExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = kondisiExpanded,
                    onDismissRequest = { kondisiExpanded = false }
                ) {
                    listOf("Baik", "Rusak", "Expired").forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                kondisi = item
                                kondisiExpanded = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Button(
                onClick = {
                    if (nama.isBlank() || volume.isBlank() || expired.isBlank() || kondisi.isBlank()) {
                        Toast.makeText(context, "Semua field harus diisi.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    val labId = bahanState?.lab_id ?: 1
                    isUpdating = true
                    updateViewModel.updateBahan(bahanId, nama, volume, expired, kondisi, labId)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isUpdating && nama.isNotBlank() && volume.isNotBlank() && expired.isNotBlank() && kondisi.isNotBlank()
            ) {
                if (isUpdating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                } else {
                    Text("Simpan Perubahan")
                }
            }
        }
    }
}