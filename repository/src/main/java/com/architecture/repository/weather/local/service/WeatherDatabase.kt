package com.architecture.repository.weather.local.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.architecture.repository.weather.local.model.WeatherEntity
import com.architecture.repository.weather.local.model.WeatherItemEntity
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory


@Database(
    entities = [WeatherEntity::class, WeatherItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class WeatherDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "CleanDB.db"
        fun buildDatabase(context: Context, password: String): WeatherDatabase {
            val passphrase: ByteArray = SQLiteDatabase.getBytes(password.toCharArray())
            val factory = SupportFactory(passphrase)
            return Room
                .databaseBuilder<WeatherDatabase>(context, WeatherDatabase::class.java, DB_NAME)
                .openHelperFactory(factory)
                .build()
        }

    }

    // DAO
    abstract fun weatherDao(): WeatherDao
}