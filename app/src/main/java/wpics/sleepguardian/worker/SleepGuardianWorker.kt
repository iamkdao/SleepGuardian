package wpics.sleepguardian.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import wpics.sleepguardian.data.prefs.SensorCacheDataStore
import wpics.sleepguardian.data.repo.SleepEventRepository
import wpics.sleepguardian.data.db.SleepEvent
import wpics.sleepguardian.domain.SleepDetectionUseCase
import wpics.sleepguardian.domain.models.ContextSnapshot
import kotlinx.coroutines.flow.first
import wpics.sleepguardian.worker.notifications.SleepNotification

class SleepGuardianWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    private val cache = SensorCacheDataStore(appContext)
    private val repo = SleepEventRepository(appContext)
    private val useCase = SleepDetectionUseCase(darkLuxThreshold = 10.0)

    override suspend fun doWork(): Result {
        // F3: use cached values ONLY (no sensor listeners here)
        val cached = cache.cacheFlow.first()

        val snapshot = ContextSnapshot(
            lux = cached.lastLux,
            proximityNear = cached.lastProximityNear,
            charging = cached.lastCharging
        )
        val eval = useCase.evaluate(snapshot)

        // We only log on activation transitions in background.
        // To do that, we compare against last known "sleepActive" stored in DataStore.
        val stateStore = WorkerStateDataStore(applicationContext)
        val prevActive = stateStore.sleepModeActiveFlow.first()
        val nowActive = eval.ruleResult

        if (prevActive != nowActive) {
            // Update stored state first
            stateStore.setSleepModeActive(nowActive)

            // F1: log event to Room
            repo.insert(
                SleepEvent(
                    timestampMs = System.currentTimeMillis(),
                    activated = nowActive,
                    reason = if (nowActive) "Worker activation (cached rule satisfied)" else "Worker deactivation",
                    lux = snapshot.lux,
                    proximityNear = snapshot.proximityNear,
                    charging = snapshot.charging
                )
            )

            // F2: notify only on activation
            if (nowActive) {
                SleepNotification.showActivation(applicationContext)
            }
        }

        return Result.success()
    }
}