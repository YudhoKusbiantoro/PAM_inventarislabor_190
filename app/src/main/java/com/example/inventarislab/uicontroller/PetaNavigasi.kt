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
import com.example.inventarislab.uicontroller.route.DestinasiHome
import com.example.inventarislab.uicontroller.route.DestinasiKelolaPengguna
import com.example.inventarislab.uicontroller.route.DestinasiLogin
import com.example.inventarislab.uicontroller.route.DestinasiRegister
import com.example.inventarislab.uicontroller.route.DestinasiWelcome
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
import com.example.inventarislab.view.route.DestinasiBahanDetail
import com.example.inventarislab.view.route.DestinasiBahanEdit
import com.example.inventarislab.view.route.DestinasiDaftarAlat
import com.example.inventarislab.view.route.DestinasiDaftarBahan
import com.example.inventarislab.view.route.DestinasiTambahAlat
import com.example.inventarislab.viewmodel.LoginViewModel
import com.example.inventarislab.view.route.DestinasiTambahBahan
import com.example.inventarislab.viewmodel.RegisterViewModel
import com.example.inventarislab.viewmodel.provider.PenyediaViewModel

@Composable
fun PetaNavigasi(navController: NavHostController) {
    val loginViewModel: LoginViewModel = viewModel(factory = PenyediaViewModel.Factory)

    NavHost(
        navController = navController,
        startDestination = DestinasiWelcome.route
    ) {
        composable(DestinasiWelcome.route) {
            HalamanWelcome(navController)
        }

        composable(DestinasiLogin.route) {
            HalamanLogin(navController, loginViewModel)
        }

        composable(DestinasiRegister.route) {
            val registerViewModel: RegisterViewModel = viewModel(factory = PenyediaViewModel.Factory)
            HalamanRegister(navController, registerViewModel)
        }

        composable(DestinasiHome.route) {
            val currentUser = loginViewModel.currentUser.collectAsState()
            HalamanHome(
                viewModel = loginViewModel,
                onBahanClick = { labId ->
                    navController.navigate("${DestinasiDaftarBahan.route}/$labId")
                },
                onPeralatanClick = { labId ->
                    navController.navigate("${DestinasiDaftarAlat.route}/$labId")
                },
                onLogoutClick = {
                    loginViewModel.logout()
                    navController.navigate(DestinasiWelcome.route)
                },
                onKelolaPenggunaClick = { labId ->
                    navController.navigate("${DestinasiKelolaPengguna.route}/$labId")
                }
            )
        }
        composable(
            DestinasiDaftarBahan.routeWithArgs,
            arguments = listOf(
                navArgument(DestinasiDaftarBahan.labIdArg) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val labIdStr = backStackEntry.arguments?.getString(DestinasiDaftarBahan.labIdArg) ?: "1"
            val labId = labIdStr.toIntOrNull() ?: 1
            HalamanBahan(
                labId = labId,
                navController = navController,
                onBackClick = { navController.popBackStack() }
            )
        }

        // DESTINASI TAMBAH BAHAN
        composable(
            DestinasiTambahBahan.routeWithArgs,
            arguments = listOf(
                navArgument(DestinasiTambahBahan.labIdArg) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val labIdStr = backStackEntry.arguments?.getString(DestinasiTambahBahan.labIdArg) ?: "1"
            val labId = labIdStr.toIntOrNull() ?: 1
            HalamanTambahBahan(
                labId = labId,
                navController = navController,
                onBackClick = { navController.popBackStack() }
            )
        }

        // DESTINASI DETAIL BAHAN
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


        // DESTINASI EDIT BAHAN
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

        // DESTINASI DAFTAR ALAT
        composable(
            DestinasiDaftarAlat.routeWithArgs,
            arguments = listOf(
                navArgument(DestinasiDaftarAlat.labIdArg) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val labIdStr = backStackEntry.arguments?.getString(DestinasiDaftarAlat.labIdArg) ?: "1"
            val labId = labIdStr.toIntOrNull() ?: 1
            HalamanAlat(
                labId = labId,
                navController = navController,
                onBackClick = { navController.popBackStack() }
            )
        }

        // DESTINASI DETAIL ALAT
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
                onBackClick = { navController.popBackStack() }
            )
        }

        // DESTINASI EDIT ALAT
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
                onBackClick = { navController.popBackStack() }
            )
        }

        // DESTINASI TAMBAH ALAT
        composable(
            DestinasiTambahAlat.routeWithArgs,
            arguments = listOf(
                navArgument(DestinasiTambahAlat.labIdArg) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val labIdStr = backStackEntry.arguments?.getString(DestinasiTambahAlat.labIdArg) ?: "1"
            val labId = labIdStr.toIntOrNull() ?: 1
            HalamanTambahAlat(
                labId = labId,
                navController = navController,
                onBackClick = { navController.popBackStack() }
            )
        }
        // DESTINASI KELOLA PENGGUNA
        composable(
            DestinasiKelolaPengguna.routeWithArgs,
            arguments = listOf(
                navArgument(DestinasiKelolaPengguna.labIdArg) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val labIdStr = backStackEntry.arguments?.getString(DestinasiKelolaPengguna.labIdArg) ?: "1"
            val labId = labIdStr.toIntOrNull() ?: 1
            HalamanKelolaPengguna(
                labId = labId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}