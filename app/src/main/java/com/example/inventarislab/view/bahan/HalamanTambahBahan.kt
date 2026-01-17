// view/bahan/HalamanTambahBahan.kt
package com.example.inventarislab.view.bahan

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
// ✅ Ganti import ViewModel
import com.example.inventarislab.viewmodel.bahan.BahanCreateViewModel
import com.example.inventarislab.viewmodel.provider.PenyediaViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanTambahBahan(
    labId: Int,
    navController: NavHostController,
    onBackClick: () -> Unit
) {
    // ✅ Gunakan ViewModel terpisah
    val viewModel: BahanCreateViewModel = viewModel(factory = PenyediaViewModel.Factory)
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

    // ✅ Handle hasil operasi
    val createResult by viewModel.createResult.collectAsState()

    LaunchedEffect(createResult) {
        val result = createResult
        if (result != null) {
            if (result.status == "success") {
                onBackClick()
            } else {
                Toast.makeText(
                    context,
                    result.message ?: "Bahan sudah ada di laboratorium ini.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            viewModel.resetCreateResult()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tambah Bahan") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
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
                    viewModel.createBahan(nama, volume, expired, kondisi, labId)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = nama.isNotBlank() && volume.isNotBlank() && expired.isNotBlank() && kondisi.isNotBlank()
            ) {
                Text("Simpan")
            }
        }
    }
}