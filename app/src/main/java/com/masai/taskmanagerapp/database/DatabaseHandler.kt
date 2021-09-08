package com.masai.taskmanagerapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.masai.taskmanagerapp.models.Task

class DatabaseHandler(val context: Context) :
    SQLiteOpenHelper(context, "tasksdb", null, 1) {

    companion object {
        val DB_NAME = "tasksdb"
        val DB_VERSION = 1

        val TABLE_NAME = "tasks"
        val ID = "id"
        val TITLE = "title"
        val DESC = "desc"
    }

    override fun onCreate(db: SQLiteDatabase?) {

        val CREATE_TABLE_QUERY = "CREATE TABLE " +
                "$TABLE_NAME(" +
                "$ID INTEGER PRIMARY KEY, " +
                "$TITLE TEXT, " +
                "$DESC TEXT)"

        db?.execSQL(CREATE_TABLE_QUERY)

    }

    fun insertTask(title: String, desc: String) {
        val db = writableDatabase

        val values = ContentValues()
        values.put(TITLE, title)
        values.put(DESC, desc)

        val idValue = db.insert(TABLE_NAME, null, values)

        if (idValue.toInt() != -1) {
            Toast.makeText(context, "Successfully inserted the row", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Failed to insert data", Toast.LENGTH_SHORT).show()
        }
    }

    fun getTasks(): MutableList<Task> {
        val taskList = mutableListOf<Task>()

        val db = readableDatabase
        val query = "select * from $TABLE_NAME"

        val cursor = db?.rawQuery(query, null)

        if (cursor != null) {
            cursor.moveToFirst()
            do {
                val id = cursor.getInt(cursor.getColumnIndex(ID))
                val title = cursor.getString(cursor.getColumnIndex(TITLE))
                val desc = cursor.getString(cursor.getColumnIndex(DESC))

                val task = Task()
                task.id = id
                task.title = title
                task.desc = desc

                taskList.add(task)

            } while (cursor.moveToNext())

        }
        return taskList
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}