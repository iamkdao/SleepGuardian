package wpics.sleepguardian.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import wpics.sleepguardian.viewmodel.SettingsViewModel
import androidx.compose.material3.Button
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import wpics.sleepguardian.viewmodel.DashboardViewModel

@Composable
fun SettingsScreen(settingsVm: SettingsViewModel = viewModel()) {
    val darkTheme by settingsVm.darkThemeEnabled.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(Modifier.weight(1f)) {
                Text("Dark Mode", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(2.dp))
                Text("Switch to dark mode.",
                    style = MaterialTheme.typography.bodyMedium
                )
                val dashboardVm: DashboardViewModel = viewModel()
                var confirm by remember { mutableStateOf(false) }

                Button(onClick = { confirm = true }) {
                    Text("Clear Sleep History")
                }

                if (confirm) {
                    AlertDialog(
                        onDismissRequest = { confirm = false },
                        title = { Text("Clear history?") },
                        text = { Text("This deletes all stored sleep detection events from the local database.") },
                        confirmButton = {
                            TextButton(onClick = {
                                dashboardVm.clearHistory()
                                confirm = false
                            }) { Text("Clear") }
                        },
                        dismissButton = {
                            TextButton(onClick = { confirm = false }) { Text("Cancel") }
                        }
                    )
                }
            }
            Switch(
                checked = darkTheme,
                onCheckedChange = { settingsVm.setDarkTheme(it) }
            )
        }
    }
}