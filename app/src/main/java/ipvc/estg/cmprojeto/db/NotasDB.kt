package ipvc.estg.cmprojeto.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import ipvc.estg.cmprojeto.dao.NotasDao
import ipvc.estg.cmprojeto.entidade.Notas
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Database(entities = arrayOf(Notas::class), version = 8, exportSchema = false)
public abstract class NotasDB : RoomDatabase() {

    abstract fun notasDao(): NotasDao

    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var notasDao = database.notasDao()

                    // Delete all content here.
                   // notasDao.deleteAll()

                    // Add sample cities.
                   /* var notas = Notas(1, "Avenida do Atlântico", "Trânsito entre as 17h e as 18h.")
                    notasDao.insert(notas)
                    notas = Notas(2, "Ponte Eiffel", "Buraco na via sentido Viana-Darque ")
                    notasDao.insert(notas)
                    notas = Notas(3, "Avenida dos Combatentes", "Atenção às obras no passeio!")
                    notasDao.insert(notas)
                    */
                }
            }
        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: NotasDB? = null

        fun getDatabase(context: Context, scope: CoroutineScope): NotasDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotasDB::class.java,
                    "notas_database"
                )
                    //estratégia de destruição
                    //.fallbackToDestructiveMigration()
                    .addCallback(WordDatabaseCallback(scope))
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}
