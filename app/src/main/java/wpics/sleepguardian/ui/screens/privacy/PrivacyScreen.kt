package wpics.sleepguardian.ui.screens.privacy

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PrivacyScreen() {
    val scroll = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Privacy by Design", style = MaterialTheme.typography.headlineMedium)

        Text(
            "Sleep Guardian is privacy-first: it does not transmit sensor data to any server. " +
                    "All data remains on-device.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(6.dp))
        Text("What data we collect", style = MaterialTheme.typography.titleMedium)

        Text(
            "• Light sensor (lux)\n" +
                    "• Proximity sensor (Near/Far)\n" +
                    "• Battery charging state + battery percentage\n" +
                    "• Sleep detection events (timestamp + the sensor snapshot used for the decision)",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(6.dp))
        Text("How we use it", style = MaterialTheme.typography.titleMedium)

        Text(
            "We combine the three contextual signals to evaluate a sleep rule:\n" +
                    "isDark && isNear && isCharging.\n\n" +
                    "If the rule becomes true, Sleep Mode is considered active and an event may be stored.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(6.dp))
        Text("Where data is stored", style = MaterialTheme.typography.titleMedium)

        Text(
            "• Room database: stores sleep event history (local only)\n" +
                    "• DataStore: stores app settings and cached last-known sensor values for background checks",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(6.dp))
        Text("Network / sharing", style = MaterialTheme.typography.titleMedium)

        Text(
            "Sleep Guardian does not send data over the network and does not collect location.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(6.dp))
        Text("Permissions", style = MaterialTheme.typography.titleMedium)

        val notifPerm = if (Build.VERSION.SDK_INT >= 33) {
            "• ${Manifest.permission.POST_NOTIFICATIONS} (Android 13+)\n" +
                    "  Used to show a notification when Sleep Mode activates."
        } else {
            "• Notifications: No runtime permission required on this Android version."
        }

        Text(
            notifPerm + "\n\n" +
                    "Sensors (light/proximity) do NOT require runtime permissions.\n" +
                    "Battery status is read via system battery broadcasts/manager (no runtime permission).",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(6.dp))
        Text("Deleting data", style = MaterialTheme.typography.titleMedium)

        Text(
            "You can clear stored sleep history from Settings → “Clear Sleep History”.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}