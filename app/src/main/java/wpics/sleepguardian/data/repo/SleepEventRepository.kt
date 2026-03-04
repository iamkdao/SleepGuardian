package wpics.sleepguardian.data.repo

import android.content.Context
import wpics.sleepguardian.data.db.DbProvider
import wpics.sleepguardian.data.db.SleepEvent
import kotlinx.coroutines.flow.Flow

class SleepEventRepository(context: Context) {
    private val dao = DbProvider.get(context).sleepEventDao()

    fun observeRecent(limit: Int = 50): Flow<List<SleepEvent>> = dao.observeRecent(limit)

    suspend fun insert(event: SleepEvent) = dao.insert(event)

    suspend fun clearAll() = dao.clearAll()
}