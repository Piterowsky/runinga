package pl.piterowsky.runinga.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import pl.piterowsky.runinga.database.entity.WorkoutHistoryEntity

class DatabaseHelper(context: Context) : OrmLiteSqliteOpenHelper(context, "runingaV2.db", null, 2) {

    override fun onCreate(database: SQLiteDatabase?, connectionSource: ConnectionSource?) {
        TableUtils.createTableIfNotExists(connectionSource, WorkoutHistoryEntity::class.java)
    }

    override fun onUpgrade(database: SQLiteDatabase?, connectionSource: ConnectionSource?, oldVersion: Int, newVersion: Int) {
        TableUtils.dropTable<WorkoutHistoryEntity, Any>(connectionSource, WorkoutHistoryEntity::class.java, true)
        onCreate(database, connectionSource)
    }
}
