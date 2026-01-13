// uicontroller/PetaNavigasi.kt
package com.example.inventarislab.uicontroller

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.inventarislab.view.HalamanAlat
import com.example.inventarislab.view.HalamanDetailAlat
import com.example.inventarislab.view.HalamanDetailBahan
import com.example.inventarislab.view.HalamanEditAlat
import com.example.inventarislab.view.HalamanEditBahan
import com.example.inventarislab.view.bahan.HalamanBahan
import com.example.inventarislab.view.HalamanHome
import com.example.inventarislab.view.HalamanKelolaPengguna
import com.example.inventarislab.view.HalamanLogin
import com.example.inventarislab.view.HalamanRegister
import com.example.inventarislab.view.HalamanTambahAlat
import com.example.inventarislab.view.bahan.HalamanTambahBahan
import com.example.inventarislab.view.HalamanWelcome
import com.example.inventarislab.view.route.DestinasiAlatDetail
import com.example.inventarislab.view.route.DestinasiAlatEdit
import com.example.inventarislab.view.route.DestinasiAlatEntry
import com.example.inventarislab.view.route.DestinasiBahanDetail
import com.example.inventarislab.view.route.DestinasiBahanEdit
import com.example.inventarislab.viewmodel.LoginViewModel
import com.example.inventarislab.view.route.DestinasiBahanEntry
import com.example.inventarislab.viewmodel.provider.PenyediaViewModel

@Composable
fun PetaNavigasi(navController: NavHostController) {
    val loginViewModel: LoginViewModel = viewModel(factory = PenyediaViewModel.Factory)

    NavHost(
        navController = navController,
        startDestination = "welcome"
    ) {
        composable("welcome") {
            HalamanWelcome(navController)
        }

        composable("login") {
            HalamanLogin(navController, loginViewModel)
        }

        composable("register") {
            HalamanRegister(navController)
        }

        composable("home") {
            val currentUser = loginViewModel.currentUser.collectAsState()
            HalamanHome(
                viewModel = loginViewModel,
                onBahanClick = {
                    val labId = currentUser.value?.lab_id?.takeIf { it > 0 } ?: 1
                    navController.navigate("${DestinasiBahanEntry.route}/$labId")
                },
                onPeralatanClick = {
                    val labId = currentUser.value?.lab_id?.takeIf { it > 0 } ?: 1
                    navController.navigate("${DestinasiAlatEntry.route}/$labId") // ✅ Gunakan destinasi
                },
                onLogoutClick = {
                    loginViewModel.logout()
                    navController.navigate("welcome")
                },
                onKelolaPenggunaClick = {
                    val labId = currentUser.value?.lab_id?.takeIf { it > 0 } ?: 1
                    navController.navigate("kelola_pengguna/$labId")
                }
            )
        }

        // ✅ DESTINASI BAHAN ENTRY (KONSISTEN STRING)
        composable(
            DestinasiBahanEntry.routeWithArgs,
            arguments = listOf(
                navArgument(DestinasiBahanEntry.labIdArg) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val labIdStr = backStackEntry.arguments?.getString(DestinasiBahanEntry.labIdArg) ?: "1"
            val labId = labIdStr.toIntOrNull() ?: 1
            HalamanBahan(
                labId = labId,
                navController = navController,
                onBackClick = { navController.popBackStack() }
            )
        }

        // ✅ DESTINASI BAHAN DETAIL (KONSISTEN STRING)
        composable(
            DestinasiBahanDetail.routeWithArgs,
            arguments = listOf(
                navArgument(DestinasiBahanDetail.bahanIdArg) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val idStr = backStackEntry.arguments?.getString(DestinasiBahanDetail.bahanIdArg) ?: "1"
            val id = idStr.toIntOrNull() ?: 1
            HalamanDetailBahan(
                bahanId = id,
                navController = navController,
                onBackClick = { navController.popBackStack() }
            )
        }

        // ✅ DESTINASI BAHAN EDIT (KONSISTEN STRING)
        composable(
            DestinasiBahanEdit.routeWithArgs,
            arguments = listOf(
                navArgument(DestinasiBahanEdit.bahanIdArg) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val idStr = backStackEntry.arguments?.getString(DestinasiBahanEdit.bahanIdArg) ?: "1"
            val id = idStr.toIntOrNull() ?: 1
            HalamanEditBahan(
                bahanId = id,
                navController = navController,
                onBackClick = { navController.popBackStack() }
            )
        }

        // TAMBAH BAHAN — JUGA PAKAI DESTINASI (BUAT KONSISTEN)
        composable(
            "tambah_bahan/{labId}",
            arguments = listOf(
                navArgument("labId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val labIdStr = backStackEntry.arguments?.getString("labId") ?: "1"
            val labId = labIdStr.toIntOrNull() ?: 1
            HalamanTambahBahan(
                labId = labId,
                navController = navController,
                onBackClick = { navController.popBackStack() }
            )
        }

        // kelola pengguna
        composable(
            "kelola_pengguna/{labId}",
            arguments = listOf(
                navArgument("labId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val labIdStr = backStackEntry.arguments?.getString("labId") ?: "1"
            val labId = labIdStr.toIntOrNull() ?: 1
            HalamanKelolaPengguna(
                labId = labId,
                onBackClick = { navController.popBackStack() }
            )
        }

        // ALAT — SUDAH DIPERBAIKI
        composable(
            DestinasiAlatEntry.routeWithArgs,
            arguments = listOf(
                navArgument(DestinasiAlatEntry.labIdArg) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val labIdStr = backStackEntry.arguments?.getString(DestinasiAlatEntry.labIdArg) ?: "1"
            val labId = labIdStr.toIntOrNull() ?: 1
            HalamanAlat(
                labId = labId,
                navController = navController,
                onBackClick = { navController.popBackStack() } // ✅ Ditambahkan
            )
        }

        composable(
            DestinasiAlatDetail.routeWithArgs,
            arguments = listOf(
                navArgument(DestinasiAlatDetail.alatIdArg) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val idStr = backStackEntry.arguments?.getString(DestinasiAlatDetail.alatIdArg) ?: "1"
            val id = idStr.toIntOrNull() ?: 1
            HalamanDetailAlat(
                alatId = id,
                navController = navController,
                onBackClick = { navController.popBackStack() } // ✅ Ditambahkan
            )
        }

        composable(
            DestinasiAlatEdit.routeWithArgs,
            arguments = listOf(
                navArgument(DestinasiAlatEdit.alatIdArg) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val idStr = backStackEntry.arguments?.getString(DestinasiAlatEdit.alatIdArg) ?: "1"
            val id = idStr.toIntOrNull() ?: 1
            HalamanEditAlat(
                alatId = id,
                navController = navController,
                onBackClick = { navController.popBackStack() } // ✅ Ditambahkan
            )
        }
        // TAMBAH ALAT
        composable(
            "tambah_alat/{labId}",
            arguments = listOf(
                navArgument("labId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val labIdStr = backStackEntry.arguments?.getString("labId") ?: "1"
            val labId = labIdStr.toIntOrNull() ?: 1
            HalamanTambahAlat(
                labId = labId,
                navController = navController,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}