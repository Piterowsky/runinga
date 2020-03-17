package pl.piterowsky.runinga.database.entity

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import java.util.*

@DatabaseTable(tableName = "workout")
class WorkoutHistoryEntity {

    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true)
    var id: Int? = null

    @DatabaseField
    var time: String? = null

    @DatabaseField
    var workoutModeName: String? = null

    @DatabaseField
    var distance: String? = null

    @DatabaseField
    var date: Date? = Date()

}
