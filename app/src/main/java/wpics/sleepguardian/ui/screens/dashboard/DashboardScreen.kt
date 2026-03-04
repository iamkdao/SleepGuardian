package wpics.sleepguardian.ui.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import wpics.sleepguardian.data.db.SleepEvent
import wpics.sleepguardian.viewmodel.DashboardViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(vm: DashboardViewModel = viewModel()) {
    val state by vm.ui.collectAsState()
    val events by vm.recentEvents.collectAsState()
    val fmt = remember { SimpleDateFormat("MMM d, h:mm a", Locale.US) }

    DisposableEffect(Unit) {
        vm.startObservers()
        onDispose { vm.stopObservers() }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Dashboard", style = MaterialTheme.typography.headlineMedium)

        // Status block
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Sleep Mode", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(6.dp))
                Text(
                    if (state.sleepModeActive) "ACTIVE" else "Inactive",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(8.dp))
                Text("Light (lux): ${state.lux?.let { "%.1f".format(it) } ?: "Unavailable"}")
                Text("Proximity: ${when (state.proximityNear) { true -> "Near"; false -> "Far"; null -> "Unavailable" }}")
                Text("Battery: ${state.batteryPercent?.let { "$it%" } ?: "Unknown"}")
                Text("Charging: ${when (state.charging) { true -> "Yes"; false -> "No"; null -> "Unknown" }}")
            }
        }

        Text("Recent sleep detection events", style = MaterialTheme.typography.titleMedium)

        if (events.isEmpty()) {
            Text("No events yet. Trigger Sleep Mode to create history.", style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(events) { e ->
                    EventRow(e, fmt)
                }
            }
        }
    }
}

@Composable
private fun EventRow(e: SleepEvent, fmt: SimpleDateFormat) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            val ts = fmt.format(Date(e.timestampMs))
            Text(
                text = "$ts — ${if (e.activated) "ACTIVATED" else "DEACTIVATED"}",
                style = MaterialTheme.typography.titleMedium
            )
            Text("Reason: ${e.reason}", style = MaterialTheme.typography.bodyMedium)

            val luxText = e.lux?.let { "%.1f".format(it) } ?: "Unavailable"
            val proxText = when (e.proximityNear) { true -> "Near"; false -> "Far"; null -> "Unavailable" }
            val chgText = when (e.charging) { true -> "Yes"; false -> "No"; null -> "Unknown" }

            Text("Snapshot → lux=$luxText, proximity=$proxText, charging=$chgText", style = MaterialTheme.typography.bodyMedium)
        }
    }
}