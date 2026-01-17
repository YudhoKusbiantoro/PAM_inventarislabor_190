// viewmodel/provider/PenyediaViewModel.kt
package com.example.inventarislab.viewmodel.provider

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.inventarislab.repositori.AplikasiInventarisLab
import com.example.inventarislab.viewmodel.LoginViewModel
import com.example.inventarislab.viewmodel.RegisterViewModel
import com.example.inventarislab.viewmodel.alat.*
import com.example.inventarislab.viewmodel.bahan.*

fun CreationExtras.aplikasiInventarisLab(): AplikasiInventarisLab =
    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as com.example.inventarislab.repositori.AplikasiInventarisLab

object PenyediaViewModel {
    val Factory = viewModelFactory {
        initializer {
            LoginViewModel(
                aplikasiInventarisLab().container.repositoryInventaris
            )
        }
        initializer {
            RegisterViewModel(
                aplikasiInventarisLab().container.repositoryInventaris
            )
        }
        // ✅ ViewModel Bahan Terpisah
        initializer {
            BahanListViewModel(
                aplikasiInventarisLab().container
                    .repositoryInventaris
            )
        }
        initializer {
            BahanDetailViewModel(
                aplikasiInventarisLab().container
                    .repositoryInventaris
            )
        }
        initializer {
            BahanCreateViewModel(
                aplikasiInventarisLab().container
                    .repositoryInventaris
            )
        }
        initializer {
            BahanUpdateViewModel(
                aplikasiInventarisLab().container
                    .repositoryInventaris
            )
        }
        initializer {
            BahanDeleteViewModel(
                aplikasiInventarisLab().container
                    .repositoryInventaris
            )
        }

        // ✅ ViewModel Alat Terpisah
        initializer {
            AlatListViewModel(
                aplikasiInventarisLab().container
                    .repositoryInventaris
            )
        }
        initializer {
            AlatDetailViewModel(
                aplikasiInventarisLab().container
                    .repositoryInventaris
            )
        }
        initializer {
            AlatCreateViewModel(
                aplikasiInventarisLab().container
                    .repositoryInventaris
            )
        }
        initializer {
            AlatUpdateViewModel(
                aplikasiInventarisLab().container
                    .repositoryInventaris
            )
        }
        initializer {
            AlatDeleteViewModel(
                aplikasiInventarisLab().container
                    .repositoryInventaris
            )
        }

    }
}