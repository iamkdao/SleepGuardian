package wpics.sleepguardian.ui.screens.context

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import wpics.sleepguardian.viewmodel.ContextLogicViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ContextLogicScreen(vm: ContextLogicViewModel = viewModel()) {
    val s by vm.ui.collectAsState()

    val fmt = remember { SimpleDateFormat("MMM d, h:mm:ss a", Locale.US) }
    val updatedText = if (s.lastUpdated > 0) fmt.format(Date(s.lastUpdated)) else "Never"

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Context Logic", style = MaterialTheme.typography.headlineMedium)

        Text(
            "Rule: isDark && isNear && isCharging",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(6.dp))

        Text("Last updated: $updatedText", style = MaterialTheme.typography.bodyMedium)

        Text("Lux: ${s.lux?.let { "%.1f".format(it) } ?: "Unavailable"}")
        Text("Proximity: ${when (s.proximityNear) { true -> "Near"; false -> "Far"; null -> "Unavailable" }}")
        Text("Charging: ${when (s.charging) { true -> "Yes"; false -> "No"; null -> "Unknown" }}")

        Spacer(Modifier.height(10.dp))

        Text("isDark: ${s.isDark}")
        Text("isNear: ${s.isNear}")
        Text("isCharging: ${s.isCharging}")
        Text("Rule result: ${s.ruleResult}", style = MaterialTheme.typography.titleMedium)
    }
}