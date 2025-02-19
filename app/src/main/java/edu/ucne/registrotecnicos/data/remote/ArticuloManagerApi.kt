package edu.ucne.registrotecnicos.data.remote

import edu.ucne.registrotecnicos.data.remote.dto.ArticuloDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.Response

interface ArticuloManagerApi {

    @GET("api/Items")
    suspend fun getArticulos(): Response<List<ArticuloDto>>

    @GET("api/Items/{id}")
    suspend fun getArticulo(@Path("id") id: Int): Response<ArticuloDto>

    @POST("api/Items")
    suspend fun saveArticulo(@Body articuloDto: ArticuloDto): Response<ArticuloDto>

    @PUT("api/Items/{id}")
    suspend fun updateArticulo(
        @Path("id") id: Int,
        @Body articuloDto: ArticuloDto
    ): Response<ArticuloDto>

    @DELETE("api/Items/{id}")
    suspend fun deleteArticulo(@Path("id") id: Int): Response<Unit>
}