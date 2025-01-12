package edu.ucne.registrotecnicos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.registrotecnicos.ui.theme.RegistroTecnicosTheme
import kotlinx.coroutines.flow.Flow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegistroTecnicosTheme {

            }
        }
    }
}

@Entity(tableName = "Technicians")
data class TechnicianEntity(
    @PrimaryKey
    val technicianId: Int? = null,
    val name: String = "",
    val salary: Double = 0.0
)

@Dao
interface TechnicianDao{
    @Upsert()
    suspend fun save(technician: TechnicianEntity)

    @Query(
        """
        SELECT * 
        FROM Technicians
        WHERE technicianId =:id  
        LIMIT 1
        """
    )
    suspend fun find(id: Int): TechnicianEntity?

    @Delete
    suspend fun delete(technician: TechnicianEntity)

    @Query("SELECT * FROM Technicians")
    fun getAll(): Flow<List<TechnicianEntity>>
}

