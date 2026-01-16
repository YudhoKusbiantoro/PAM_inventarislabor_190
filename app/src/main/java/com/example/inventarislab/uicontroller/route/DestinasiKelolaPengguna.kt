// uicontroller/route/DestinasiKelolaPengguna.kt
package com.example.inventarislab.uicontroller.route

import com.example.inventarislab.R

object DestinasiKelolaPengguna : DestinasiNavigasi {
    override val route = "kelola_pengguna"
    override val titleRes = R.string.kelola_pengguna
    override val iconRes = R.drawable.kelolapengguna
    const val labIdArg = "labId"
    val routeWithArgs = "$route/{$labIdArg}"
}