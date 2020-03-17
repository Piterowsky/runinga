package pl.piterowsky.runinga.activity

import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import pl.piterowsky.runinga.R
import pl.piterowsky.runinga.database.dao.WorkoutHistoryDao
import pl.piterowsky.runinga.database.entity.WorkoutHistoryEntity
import java.lang.Exception
import java.text.SimpleDateFormat


class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        addWorkoutsToTable()
    }

    private fun addWorkoutsToTable() {
        val table: TableLayout = findViewById(R.id.history_table_layout)

        try {
            val dao = WorkoutHistoryDao(this)
            val allWorkouts = dao.queryForAll()
            allWorkouts.sortBy { workoutHistoryEntity -> workoutHistoryEntity.date }
            allWorkouts.reverse()
            allWorkouts.forEach { workout -> addRowToTable(workout, table) }
        } catch (e: Exception) {
            Log.e("DATABASE", "Error while reading from database")
        }
    }

    private fun addRowToTable(workout: WorkoutHistoryEntity, tl: TableLayout) {
        val tr = createRow(workout)

        tl.addView(
            tr,
            TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT)
        )
    }

    private fun createRow(workout: WorkoutHistoryEntity): TableRow {
        val pattern = "dd-MM-yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern)
        val workoutDate = simpleDateFormat.format(workout.date!!)

        val date = createCell(workoutDate)
        val distance = createCell(workout.distance!!)
        val time = createCell(workout.time!!)
        val mode = createCell(workout.workoutModeName!!)

        val tr = TableRow(this)

        tr.addView(date)
        tr.addView(time)
        tr.addView(distance)
        tr.addView(mode)

        tr.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )

        return tr
    }

    private fun createCell(text: String): TextView {
        val cell = TextView(this)
        cell.text = text
        cell.textAlignment = Layout.Alignment.ALIGN_CENTER.ordinal
        cell.setPadding(12, 12, 12, 12)
        cell.background = resources.getDrawable(R.drawable.border)

        cell.layoutParams =
            TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f)

        return cell
    }
}


