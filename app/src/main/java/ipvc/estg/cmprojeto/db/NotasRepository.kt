package ipvc.estg.cmprojeto.db

import androidx.lifecycle.LiveData
import ipvc.estg.cmprojeto.dao.NotasDao
import ipvc.estg.cmprojeto.entidade.Notas

class NotasRepository(private val notasDao: NotasDao) {
    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allCities: LiveData<List<Notas>> = notasDao.getOrdenarNotasid()

    suspend fun insert(notas: Notas) {
        notasDao.insert(notas)
    }
}
