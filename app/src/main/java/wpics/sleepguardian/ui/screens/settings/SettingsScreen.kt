package wpics.sleepguardian.ui.screens.settings

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import wpics.sleepguardian.viewmodel.DashboardViewModel
import wpics.sleepguardian.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    settingsVm: SettingsViewModel = viewModel(),
    dashboardVm: DashboardViewModel = viewModel()
) {
    val darkTheme by settingsVm.darkThemeEnabled.collectAsState()
    var confirmClear by remember { mutableStateOf(false) }

    // Epic F2: Android 13+ notification permission
    val notifLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { /* optional: show toast/snackbar */ }
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)

        // ---- Dark Mode toggle ----
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(Modifier.weight(1f)) {
                Text("Dark Mode", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(2.dp))
                Text("Switch to dark mode.", style = MaterialTheme.typography.bodyMedium)
            }
            Switch(
                checked = darkTheme,
                onCheckedChange = { settingsVm.setDarkTheme(it) }
            )
        }

        // ---- Notification permission (Android 13+) ----
        if (Build.VERSION.SDK_INT >= 33) {
            Button(onClick = { notifLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) }) {
                Text("Enable Notifications")
            }
        } else {
            Text(
                "Notifications are enabled by default on this Android version.",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // ---- Clear history ----
        Button(onClick = { confirmClear = true }) {
            Text("Clear Sleep History")
        }

        if (confirmClear) {
            AlertDialog(
                onDismissRequest = { confirmClear = false },
                title = { Text("Clear history?") },
                text = { Text("This deletes all stored sleep detection events from the local database.") },
                confirmButton = {
                    TextButton(onClick = {
                        dashboardVm.clearHistory()
                        confirmClear = false
                    }) { Text("Clear") }
                },
                dismissButton = {
                    TextButton(onClick = { confirmClear = false }) { Text("Cancel") }
                }
            )
        }
    }
}