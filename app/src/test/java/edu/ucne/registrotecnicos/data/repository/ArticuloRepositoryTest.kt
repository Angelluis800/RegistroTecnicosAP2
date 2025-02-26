package edu.ucne.registrotecnicos.data.repository

import android.util.Log
import app.cash.turbine.test
import com.google.common.truth.Truth
import edu.ucne.registrotecnicos.data.local.dao.ArticuloDao
import edu.ucne.registrotecnicos.data.local.entity.ArticuloEntity
import edu.ucne.registrotecnicos.data.remote.RemoteDataSource
import edu.ucne.registrotecnicos.data.remote.Resource
import edu.ucne.registrotecnicos.data.remote.dto.ArticuloDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class ArticuloRepositoryTest {

    @Test
    fun addArticulo() = runTest {
        // Given
        val articulo = ArticuloDto(
            itemId = 1,
            description = "Articulo 1",
            cost = 10.0,
            revenue = 20.0,
            price = 30.0
        )

        val articuloRemoteDataSource = mockk<RemoteDataSource>()
        val articuloDao = mockk<ArticuloDao>()
        val repository = ArticuloRepository(articuloDao, articuloRemoteDataSource)

        coEvery { articuloRemoteDataSource.saveArticulo(any()) } returns Response.success(articulo)

        // When
        repository.save(articulo)

        // Then
        coVerify { articuloRemoteDataSource.saveArticulo(articulo) }
    }

    @Test
    fun returnArticulo() = runTest {
        //Given
        val articulo = ArticuloDto(
            itemId = 1,
            description = "Tayota",
            cost = 10.0,
            revenue = 20.0,
            price = 30.0
        )

        val articuloRemoteDataSource = mockk<RemoteDataSource>()
        val articuloDao = mockk<ArticuloDao>()
        val repository = ArticuloRepository(articuloDao, articuloRemoteDataSource)

        coEvery { articuloRemoteDataSource.getArticulo(articulo.itemId) } returns Response.success(articulo)

        //When
        repository.find(articulo.itemId)

        //Then
        coVerify { articuloRemoteDataSource.getArticulo(articulo.itemId) }
    }

    @Test
    fun deleteArticulo() = runTest {
        //Given
        val articulo = ArticuloDto(
            itemId = 1,
            description = "Tayota",
            cost = 10.0,
            revenue = 20.0,
            price = 30.0
        )

        val articuloRemoteDataSource = mockk<RemoteDataSource>()
        val artculoDao = mockk<ArticuloDao>()
        val repository = ArticuloRepository(artculoDao, articuloRemoteDataSource)

        coEvery { articuloRemoteDataSource.deleteArticulo(articulo.itemId) } returns Response.success(
            Unit
        )

        //When
        repository.delete(articulo.itemId)

        //Then
        coVerify { articuloRemoteDataSource.deleteArticulo(articulo.itemId) }
    }
    @Test
    fun updateArticulo() = runTest {
        //Given
        val articulo = ArticuloDto(
            itemId = 1,
            description = "Coca Cola",
            cost = 10.0,
            revenue = 20.0,
            price = 30.0
        )

        val articuloRemoteDataSource = mockk<RemoteDataSource>()
        val articuloDao = mockk<ArticuloDao>()
        val repository = ArticuloRepository(articuloDao, articuloRemoteDataSource)

        coEvery {
            articuloRemoteDataSource.updateArticulo(
                articulo.itemId, articulo
            )
        } returns Response.success(articulo)

        //When
        repository.update(articulo.itemId, articulo)

        //Then
        coVerify { articuloRemoteDataSource.updateArticulo(articulo.itemId, articulo) }
    }

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
    }
    @Test
    fun returnFlowArticulo() = runTest {
        // Given
        val articulos = listOf(
            ArticuloEntity(
                1,
                "Articulo 2",
                500.0,
                100.0,
                600.0
            )
        )

        val articuloRemoteDataSource = mockk<RemoteDataSource>()
        val articuloDao = mockk<ArticuloDao>()
        val repository = ArticuloRepository(articuloDao, articuloRemoteDataSource)

        coEvery { articuloDao.getAll() } returns flowOf(articulos)

        //When
        repository.getAllArticulos().test {
            //Then
            Truth.assertThat(awaitItem() is Resource.Loading).isTrue()

            Truth.assertThat(awaitItem().data).isEqualTo(articulos)

            cancelAndIgnoreRemainingEvents()
        }
    }


}