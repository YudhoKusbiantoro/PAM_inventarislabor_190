package com.example.inventarislab.repositori

import com.example.inventarislab.apiservice.ApiService
import com.example.inventarislab.modeldata.*
import retrofit2.HttpException
import java.io.IOException

// === INTERFACE REPOSITORY ===
interface RepositoryInventaris {
    // Auth & Lab
    suspend fun login(username: String, password: String): ResponseData<User>
    suspend fun register(requestBody: Map<String, String>): ResponseData<User>
    suspend fun getLabs(): ResponseData<List<Lab>>

    // BAHAN
    suspend fun getBahanByLabId(labId: Int): ResponseData<List<Bahan>>
    suspend fun getBahanById(id: Int): ResponseData<Bahan>
    suspend fun createBahan(bahan: Map<String, String>): ResponseData<Bahan>
    suspend fun updateBahan(bahan: Map<String, String>): ResponseData<Bahan>
    suspend fun deleteBahan(params: Map<String, String>): ResponseData<String>

    // ALAT
    suspend fun getAlatByLabId(labId: Int): ResponseData<List<Alat>>
    suspend fun getAlatById(id: Int): ResponseData<Alat>
    suspend fun createAlat(alat: Map<String, String>): ResponseData<Alat>
    suspend fun updateAlat(alat: Map<String, String>): ResponseData<Alat>
    suspend fun deleteAlat(params: Map<String, String>): ResponseData<String>
}

// === IMPLEMENTASI REPOSITORY ===
class JaringanRepositoryInventaris(
    private val apiService: ApiService
) : RepositoryInventaris {

    override suspend fun login(username: String, password: String): ResponseData<User> {
        return safeApiCall {
            apiService.login(mapOf("username" to username, "password" to password))
        }
    }

    override suspend fun register(requestBody: Map<String, String>): ResponseData<User> {
        return apiService.register(requestBody)
    }

    override suspend fun getLabs(): ResponseData<List<Lab>> {
        return apiService.getLabs()
    }

    // === BAHAN ===
    override suspend fun getBahanByLabId(labId: Int): ResponseData<List<Bahan>> {
        return safeApiCall { apiService.getBahanByLabId(mapOf("lab_id" to labId.toString())) }
    }

    override suspend fun getBahanById(id: Int): ResponseData<Bahan> {
        return safeApiCall { apiService.getBahanById(mapOf("id" to id.toString())) }
    }

    override suspend fun createBahan(bahan: Map<String, String>): ResponseData<Bahan> {
        return safeApiCall { apiService.createBahan(bahan) }
    }

    override suspend fun updateBahan(bahan: Map<String, String>): ResponseData<Bahan> {
        return safeApiCall { apiService.updateBahan(bahan) }
    }

    override suspend fun deleteBahan(params: Map<String, String>): ResponseData<String> {
        return safeApiCall { apiService.deleteBahan(params) }
    }

    // === ALAT ===
    override suspend fun getAlatByLabId(labId: Int): ResponseData<List<Alat>> {
        return safeApiCall { apiService.getAlatByLabId(mapOf("lab_id" to labId.toString())) }
    }

    override suspend fun getAlatById(id: Int): ResponseData<Alat> {
        return safeApiCall { apiService.getAlatById(mapOf("id" to id.toString())) }
    }

    override suspend fun createAlat(alat: Map<String, String>): ResponseData<Alat> {
        return safeApiCall { apiService.createAlat(alat) }
    }

    override suspend fun updateAlat(alat: Map<String, String>): ResponseData<Alat> {
        return safeApiCall { apiService.updateAlat(alat) }
    }

    override suspend fun deleteAlat(params: Map<String, String>): ResponseData<String> {
        return safeApiCall { apiService.deleteAlat(params) }
    }

    // Helper untuk penanganan error umum
    private suspend fun <T> safeApiCall(apiCall: suspend () -> ResponseData<T>): ResponseData<T> {
        return try {
            apiCall()
        } catch (e: IOException) {
            // Gunakan status "error" atau "failed" — sesuaikan dengan backend
            ResponseData(
                status = "error",  // atau "failed"
                message = "Network Error",
                data = null
            )
        } catch (e: HttpException) {
            ResponseData(
                status = "error",
                message = "Username atau password salah.",
                data = null
            )
        } catch (e: Exception) {
            ResponseData(
                status = "error",
                message = "Unexpected Error: ${e.message}",
                data = null
            )
        }
    }
}