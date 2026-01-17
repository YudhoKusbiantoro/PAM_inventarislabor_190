// view/HalamanTambahAlat.kt
package com.example.inventarislab.view

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.inventarislab.viewmodel.alat.AlatCreateViewModel
import com.example.inventarislab.viewmodel.provider.PenyediaViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanTambahAlat(
    labId: Int,
    navController: NavHostController,
    onBackClick: () -> Unit
) {
    // ✅ Gunakan ViewModel terpisah
    val viewModel: AlatCreateViewModel = viewModel(factory = PenyediaViewModel.Factory)

    val createResult by viewModel.createResult.collectAsState()
    var isCreating by remember { mutableStateOf(false) }

    var nama by remember { mutableStateOf("") }
    var jumlah by remember { mutableStateOf("") }
    var terakhirKalibrasi by remember { mutableStateOf("") }
    var intervalKalibrasi by remember { mutableStateOf("") }
    var satuanInterval by remember { mutableStateOf("") }
    var kondisi by remember { mutableStateOf("") }

    var satuanExpanded by remember { mutableStateOf(false) }
    var kondisiExpanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    LaunchedEffect(createResult) {
        val result = createResult
        if (result != null) {
            isCreating = false
            if (result.status == "success") {
                onBackClick()
            } else {
                Toast.makeText(
                    context,
                    result.message ?: "Gagal menambahkan alat",
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.resetCreateResult()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tambah Alat") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = nama,
                onValueChange = { nama = it },
                label = { Text("Nama Alat") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = jumlah,
                onValueChange = { jumlah = it },
                label = { Text("Jumlah") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = terakhirKalibrasi,
                onValueChange = { /* readOnly */ },
                label = { Text("Terakhir Kalibrasi (YYYY-MM-DD)") },
                trailingIcon = {
                    IconButton(onClick = {
                        DatePickerDialog(
                            context,
                            { _, y, m, d ->
                                terakhirKalibrasi = "%04d-%02d-%02d".format(y, m + 1, d)
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

            OutlinedTextField(
                value = intervalKalibrasi,
                onValueChange = { intervalKalibrasi = it },
                label = { Text("Interval Kalibrasi") },
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = satuanExpanded,
                onExpandedChange = { satuanExpanded = it }
            ) {
                OutlinedTextField(
                    value = if (satuanInterval.isEmpty()) "Pilih Interval" else satuanInterval,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Satuan Interval") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = satuanExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = satuanExpanded,
                    onDismissRequest = { satuanExpanded = false }
                ) {
                    listOf("Hari", "Minggu", "Bulan", "Tahun").forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                satuanInterval = item
                                satuanExpanded = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = kondisiExpanded,
                onExpandedChange = { kondisiExpanded = it }
            ) {
                OutlinedTextField(
                    value = if (kondisi.isEmpty()) "Pilih Kondisi" else kondisi,
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
                    listOf("Baik", "Rusak").forEach { item ->
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
                    if (nama.isNotEmpty() && jumlah.isNotEmpty() &&
                        terakhirKalibrasi.isNotEmpty() && intervalKalibrasi.isNotEmpty() &&
                        satuanInterval.isNotEmpty() && kondisi.isNotEmpty()) {
                        isCreating = true
                        viewModel.createAlat(
                            nama = nama,
                            jumlah = jumlah,
                            terakhirKalibrasi = terakhirKalibrasi,
                            intervalKalibrasi = intervalKalibrasi,
                            satuanInterval = satuanInterval,
                            kondisi = kondisi,
                            labId = labId
                        )
                    } else {
                        Toast.makeText(context, "Semua field harus diisi.", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isCreating
            ) {
                if (isCreating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                } else {
                    Text("Simpan Alat")
                }
            }
        }
    }
}