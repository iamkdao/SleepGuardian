package wpics.sleepguardian.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SleepEventDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(event: SleepEvent)

    @Query("SELECT * FROM sleep_events ORDER BY timestampMs DESC LIMIT :limit")
    fun observeRecent(limit: Int = 50): Flow<List<SleepEvent>>

    @Query("DELETE FROM sleep_events")
    suspend fun clearAll()
}