package edu.ucne.registrotecnicos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import kotlinx.coroutines.launch

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
                        TechnicianScreen()
                    }
                }

            }
        }
    }

    @Composable
    fun TechnicianScreen(
    ){
        var name by remember { mutableStateOf("") }
        var salary by remember { mutableStateOf("") }
        var errorMessage: String? by remember { mutableStateOf(null) }

        Scaffold { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(8.dp)
            ) {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        OutlinedTextField(
                            label = { Text(text = "Nombre")},
                            value = name,
                            onValueChange = { name = it},
                            modifier =  Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            label = { Text(text = "Sueldo") },
                            value = salary,
                            onValueChange = { salary = it },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer( modifier = Modifier.padding(2.dp))
                        errorMessage?.let {
                            Text(text = it, color = Color.Red)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedButton(
                                onClick = {
                                    name = ""
                                    salary = ""
                                    errorMessage = ""
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "new button"
                                )
                                Text(text = "Nuevo")
                            }
                            val scope = rememberCoroutineScope()
                            OutlinedButton(
                                onClick = {
                                    if(name.isBlank())
                                        errorMessage = "Nombre Vacio"

                                    scope.launch {
                                        saveTechnician(
                                            TechnicianEntity(
                                                name = name,
                                                salary = salary.toDouble()
                                            )
                                        )
                                        name = ""
                                        salary = ""
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "save button"
                                )
                                Text(text = "Guardar")
                            }
                        }
                    }
                }

                val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
                val technicianList by technicianDb.technicianDao().getAll()
                    .collectAsStateWithLifecycle(
                        initialValue = emptyList(),
                        lifecycleOwner = lifecycleOwner,
                        minActiveState = Lifecycle.State.STARTED
                    )
                TechnicianListScreen(technicianList)
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
                style = MaterialTheme.typography.bodyMedium
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



