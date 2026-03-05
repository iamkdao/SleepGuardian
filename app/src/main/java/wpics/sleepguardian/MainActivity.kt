package wpics.sleepguardian

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import wpics.sleepguardian.ui.SleepGuardianApp
import wpics.sleepguardian.worker.WorkerScheduler

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WorkerScheduler.schedule(this)

        setContent {
            SleepGuardianApp()
        }
    }
}