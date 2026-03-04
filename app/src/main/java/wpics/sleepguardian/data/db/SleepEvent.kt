package wpics.sleepguardian.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sleep_events")
data class SleepEvent(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestampMs: Long,
    val activated: Boolean,          // true = Sleep Mode ON, false = OFF
    val reason: String,              // e.g. "Rule satisfied" / "Rule no longer satisfied"
    val lux: Double?,                // snapshot
    val proximityNear: Boolean?,     // snapshot
    val charging: Boolean?           // snapshot
)