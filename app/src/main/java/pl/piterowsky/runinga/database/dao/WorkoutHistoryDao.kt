package pl.piterowsky.runinga.database.dao

import android.content.Context
import com.j256.ormlite.dao.Dao
import pl.piterowsky.runinga.database.DatabaseHelper
import pl.piterowsky.runinga.database.entity.WorkoutHistoryEntity

class WorkoutHistoryDao(context: Context) {

    companion object {
        lateinit var dao: Dao<WorkoutHistoryEntity, Int>
    }

    init {
        dao = DatabaseHelper(context).getDao(WorkoutHistoryEntity::class.java)
    }

    fun add(table: WorkoutHistoryEntity) = dao.createOrUpdate(table)

    fun queryForAll() = dao.queryForAll()

    fun getDao() = dao
}
