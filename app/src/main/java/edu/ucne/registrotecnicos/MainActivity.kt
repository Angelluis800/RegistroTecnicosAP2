package edu.ucne.registrotecnicos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Upsert
import edu.ucne.registrotecnicos.ui.theme.RegistroTecnicosTheme
import kotlinx.coroutines.flow.Flow

class MainActivity : ComponentActivity() {
    private lateinit var technicianDb: TechnicianDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        technicianDb = Room.databaseBuilder(
            applicationContext,
            TechnicianDb::class.java,
            "TechnicianDb"
        ).fallbackToDestructiveMigration()
            .build()

        setContent {
            RegistroTecnicosTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        //Aqui va el TechnicianScreen
                    }
                }

            }
        }
    }

    @Composable
    fun TechnicianListScreen(techList: List<TechnicianEntity>){
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text("Lista de Técnicos")

            LazyColumn(
                modifier =  Modifier.fillMaxSize()
            ) {
                items(techList){
                    TechnicianRow(it)
                }
            }
        }
    }

    @Composable
    private fun TechnicianRow(it: TechnicianEntity) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(modifier = Modifier.weight(1f), text = it.technicianId.toString())
            Text(
                modifier = Modifier.weight(2f),
                text = it.name,
                style = MaterialTheme.typography.headlineLarge
            )
            Text(modifier = Modifier.weight(2f), text =  it.salary.toString())
        }
        HorizontalDivider()
    }


    private suspend fun saveTechnician(technician: TechnicianEntity) {
        technicianDb.technicianDao().save(technician)
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

    @Database(
        entities = [
            TechnicianEntity::class
        ],
        version = 1,
        exportSchema = false
    )abstract class TechnicianDb : RoomDatabase() {
        abstract fun technicianDao(): TechnicianDao
    }

}



