//Gia Huy Phạm
//Ryan Zhang

package wpics.sleepguardian.data.db

import android.content.Context
import androidx.room.Room

object DbProvider {
    @Volatile private var INSTANCE: AppDatabase? = null

    fun get(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "sleepguardian.db"
            ).build().also { INSTANCE = it }
        }
    }
}