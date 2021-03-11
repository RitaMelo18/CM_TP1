package ipvc.estg.cmprojeto.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ipvc.estg.cmprojeto.entidade.Notas

@Dao
interface NotasDao {

    @Query("SELECT * from notas_table order by id DESC")
    fun getOrdenarNotasid(): LiveData<List<Notas>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(notas: Notas)

    @Query(value = "DELETE FROM notas_table")
    suspend fun deleteAll()
}