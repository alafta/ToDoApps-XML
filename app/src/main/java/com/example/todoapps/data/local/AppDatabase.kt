package com.example.todoapps.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.todoapps.data.model.ToDoItem

@Database(entities = [ToDoItem::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getToDoDao(): ToDoDao

    companion object{
        @Volatile
        private var instance: AppDatabase? = null
        private var LOCK = Any()

        operator fun invoke(context: Context) = instance ?:
        synchronized(LOCK) {
            instance ?:
            createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "todo_database"
            ).build()
    }
}

//companion object {
//    @Volatile
//    private var INSTANCE: AppDatabase? = null
//
//    fun getDatabase(context: Context): AppDatabase {
//        return INSTANCE ?: synchronized(this) {
//            val instance = Room.databaseBuilder(
//                context.applicationContext,
//                AppDatabase::class.java,
//                "todo_Database"
//            ).build()
//            INSTANCE = instance
//            instance
//        }
//    }
//}