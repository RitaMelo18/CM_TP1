package ipvc.estg.cmprojeto.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ipvc.estg.cmprojeto.entidade.Notas

@Dao
interface NotasDao {

    //GetOrdenarNotasID
    @Query("SELECT * from notas_table order by id DESC")
    fun getOrdenarNotasid(): LiveData<List<Notas>>

    //Inserir Notas
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(notas: Notas)

    //Apagar Notas
    @Query(value = "DELETE FROM notas_table")
    suspend fun deleteAll()

    //Atualizar Notas
    @Update
    suspend fun editNote(notas : Notas)

    //Apagar Notas pelo id
    @Query("DELETE FROM notas_table where id ==:id")
    suspend fun deleteNoteById(id: Int?)

}