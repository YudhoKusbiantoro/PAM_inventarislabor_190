// view/bahan/HalamanBahan.kt
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
import com.example.inventarislab.modeldata.Bahan
import com.example.inventarislab.viewmodel.bahan.*
import com.example.inventarislab.viewmodel.provider.PenyediaViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanBahan(
    labId: Int,
    navController: NavHostController,
    onBackClick: () -> Unit
) {
    val listViewModel: BahanListViewModel =
        viewModel(factory = PenyediaViewModel.Factory)
    val deleteViewModel: BahanDeleteViewModel =
        viewModel(factory = PenyediaViewModel.Factory)
    val createViewModel: BahanCreateViewModel =
        viewModel(factory = PenyediaViewModel.Factory)
    val updateViewModel: BahanUpdateViewModel =
        viewModel(factory = PenyediaViewModel.Factory)

    val bahan by listViewModel.bahan.collectAsState()
    val notification by listViewModel.notification.collectAsState()
    val deleteResult by deleteViewModel.deleteResult.collectAsState()

    var showEditDialog by remember { mutableStateOf<Bahan?>(null) }
    var showDeleteDialog by remember { mutableStateOf<Bahan?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Semua") }

    LaunchedEffect(labId) {
        listViewModel.loadBahanByLabId(labId)
    }

    LaunchedEffect(deleteResult) {
        if (deleteResult != null) {
            listViewModel.loadBahanByLabId(labId)
            deleteViewModel.resetDeleteResult()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Daftar Bahan",
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
                onClick = { navController.navigate("tambah_bahan/$labId") },
                containerColor = Color(0xFF2196F3),
                content = {
                    Icon(Icons.Default.Add, contentDescription = "Tambah Bahan", tint = Color.White)
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
            // NOTIFIKASI CARD - PUTIH & TIMBUL DENGAN LOGO & SUSUNAN 2x2
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

                    val n = notification ?: BahanListViewModel.Notification(0, 0, 0, 0)

                    // ROW UNTUK 2 KOLOM
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // KOLOM KIRI
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Baris 1: Total Bahan
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.all),
                                    contentDescription = "Total Bahan",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Total Bahan : ${n.total}")
                            }

                            // Baris 2: Hampir Expired
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.hampirexpired),
                                    contentDescription = "Hampir Expired",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Hampir expired : ${n.hampirExpired}")
                            }
                        }

                        // KOLOM KANAN
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Baris 3: Expired
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.expired),
                                    contentDescription = "Expired",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Expired : ${n.expired}")
                            }

                            // Baris 4: Kondisi Rusak
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
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
                label = { Text("Search") },
                modifier = Modifier.fillMaxWidth()
            )

            // FILTER BUTTONS
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
                    selected = selectedFilter == "Hampir Expired",
                    onClick = { selectedFilter = "Hampir Expired" },
                    label = { Text("Hampir Expired") },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = if (selectedFilter == "Hampir Expired") Color(0xFFFFF3E0) else Color(0xFFF5F5F5),
                        labelColor = if (selectedFilter == "Hampir Expired") Color(0xFFFF6F00) else Color.Gray
                    )
                )

                FilterChip(
                    selected = selectedFilter == "Expired",
                    onClick = { selectedFilter = "Expired" },
                    label = { Text("Expired") },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = if (selectedFilter == "Expired") Color(0xFFFFEBEE) else Color(0xFFF5F5F5),
                        labelColor = if (selectedFilter == "Expired") Color.Red else Color.Gray
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

            // LIST BAHAN
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    bahan.filter { item ->
                        val matchesSearch =
                            item.nama.contains(searchQuery, ignoreCase = true)

                        val matchesFilter = when (selectedFilter) {
                            "Semua" -> true

                            "Hampir Expired" ->
                                isHampirExpired(item.expired)

                            "Expired" -> {
                                try {
                                    val formatter = SimpleDateFormat(
                                        "yyyy-MM-dd",
                                        Locale.getDefault()
                                    )
                                    val expiredDate =
                                        formatter.parse(item.expired) ?: return@filter false

                                    val today = Calendar.getInstance()
                                    val expiredCal = Calendar.getInstance().apply {
                                        time = expiredDate
                                    }

                                    !expiredCal.after(today)
                                } catch (e: Exception) {
                                    false
                                }
                            }

                            "Rusak" -> item.kondisi == "Rusak"

                            else -> true
                        }

                        matchesSearch && matchesFilter
                    }
                ) { bahanItem ->
                    BahanCard(
                        bahan = bahanItem,
                        onClick = {
                            navController.navigate("detail_bahan/${bahanItem.id}")
                        },
                        onDelete = {
                            deleteViewModel.deleteBahan(
                                bahanItem.id,
                                bahanItem.lab_id
                            )
                        }
                    )
                }
            }
        }
    }

    // DIALOGS
    if (showEditDialog != null) {
        EditBahanDialog(
            bahan = showEditDialog!!,
            onConfirm = { nama, volume, expired, kondisi ->
                updateViewModel.updateBahan(
                    id = showEditDialog!!.id,
                    nama = nama,
                    volume = volume,
                    expired = expired,
                    kondisi = kondisi,
                    labId = labId
                )
                showEditDialog = null
            },
            onDismiss = { showEditDialog = null }
        )
    }

    if (showDeleteDialog != null) {
        ConfirmDeleteBahanDialog(
            itemName = showDeleteDialog!!.nama,
            onConfirm = {
                deleteViewModel.deleteBahan(showDeleteDialog!!.id, labId)
                showDeleteDialog = null
            },
            onDismiss = { showDeleteDialog = null }
        )
    }

    if (showAddDialog) {
        AddBahanDialog(
            onConfirm = { nama, volume, expired, kondisi ->
                createViewModel.createBahan(
                    nama = nama,
                    volume = volume,
                    expired = expired,
                    kondisi = kondisi,
                    labId = labId
                )
                showAddDialog = false
            },
            onDismiss = { showAddDialog = false }
        )
    }
}

@Composable
fun BahanCard(
    bahan: Bahan,
    onClick: () -> Unit,
    onDelete: () -> Unit,
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
            // KIRI: Logo + Nama + Detail
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.weight(1f)
            ) {
                // Logo (dari drawable)
                Image(
                    painter = painterResource(id = R.drawable.logobahan),
                    contentDescription = "Logo Bahan",
                    modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = bahan.nama,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.Black
                    )
                    Text(
                        text = "Volume: ${bahan.volume}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Text(
                        text = "Expired: ${bahan.expired}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray
                    )
                }
            }

            // KANAN: Status dalam kotak berwarna
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        color = when (bahan.kondisi) {
                            "Expired" -> Color(0xFFFFEBEE) // Merah muda
                            "Rusak" -> Color(0xFFFFF3E0)   // Kuning muda
                            else -> Color(0xFFE8F5E9)      // Hijau muda
                        }
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = bahan.kondisi.uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = when (bahan.kondisi) {
                        "Expired" -> Color.Red
                        "Rusak" -> Color(0xFFFF9800)
                        else -> Color.Green
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBahanDialog(
    bahan: Bahan,
    onConfirm: (String, String, String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var nama by remember { mutableStateOf(bahan.nama) }
    var volume by remember { mutableStateOf(bahan.volume) }
    var expired by remember { mutableStateOf(bahan.expired) }
    var kondisi by remember { mutableStateOf(bahan.kondisi) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Bahan") },
        text = {
            Column {
                OutlinedTextField(
                    value = nama,
                    onValueChange = { nama = it },
                    label = { Text("Nama") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = volume,
                    onValueChange = { volume = it },
                    label = { Text("Volume") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = expired,
                    onValueChange = { expired = it },
                    label = { Text("Expired") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = kondisi,
                    onValueChange = { kondisi = it },
                    label = { Text("Kondisi") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm(nama, volume, expired, kondisi)
            }) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

@Composable
fun ConfirmDeleteBahanDialog(
    itemName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Konfirmasi Hapus") },
        text = { Text("Hapus bahan $itemName? Tindakan ini tidak dapat dibatalkan.") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Hapus", color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBahanDialog(
    onConfirm: (String, String, String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var nama by remember { mutableStateOf("") }
    var volume by remember { mutableStateOf("") }
    var expired by remember { mutableStateOf("") }
    var kondisi by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tambah Bahan") },
        text = {
            Column {
                OutlinedTextField(
                    value = nama,
                    onValueChange = { nama = it },
                    label = { Text("Nama") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = volume,
                    onValueChange = { volume = it },
                    label = { Text("Volume") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = expired,
                    onValueChange = { expired = it },
                    label = { Text("Expired (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = kondisi,
                    onValueChange = { kondisi = it },
                    label = { Text("Kondisi (Baik/Rusak/Expired)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (nama.isNotEmpty() && volume.isNotEmpty() && expired.isNotEmpty() && kondisi.isNotEmpty()) {
                    onConfirm(nama, volume, expired, kondisi)
                }
            }) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

/* =========================
   HELPER (SDK 24 SAFE)
   ========================= */

fun isHampirExpired(expiredDate: String): Boolean {
    return try {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val expired = formatter.parse(expiredDate) ?: return false

        val today = Calendar.getInstance()
        val expiredCal = Calendar.getInstance().apply {
            time = expired
        }

        val diffMillis =
            expiredCal.timeInMillis - today.timeInMillis
        val diffDays =
            (diffMillis / (1000 * 60 * 60 * 24)).toInt()

        diffDays in 1..7
    } catch (e: Exception) {
        false
    }
}