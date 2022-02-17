package com.dicoding.tourismapp.core.data.source.remote

import android.util.Log
import com.dicoding.tourismapp.core.data.source.remote.network.ApiResponse
import com.dicoding.tourismapp.core.data.source.remote.network.ApiService
import com.dicoding.tourismapp.core.data.source.remote.response.TourismResponse
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val apiService: ApiService) {
    suspend fun getAllTourism(): Flow<ApiResponse<List<TourismResponse>>> = flow {
        coroutineScope {

            try {
                val response = apiService.getList()
                val dataArray = response.places
                val newArray = withContext(Dispatchers.IO) {
                    dataArray.map {
                        fetchDetailTourism(it)
                    }.awaitAll()
                }
                if (newArray.isNotEmpty()) {
                    emit(ApiResponse.Success(newArray))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
                Log.e("RemoteDataSource", e.message.toString())
            }
        }
    }.flowOn(Dispatchers.IO)

    fun CoroutineScope.fetchDetailTourism(tourism: TourismResponse): Deferred<TourismResponse> =
        async(Dispatchers.IO) {
            return@async tourism.copy(name = tourism.name + " Naufal")
        }
}

