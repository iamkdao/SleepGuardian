//Gia Huy Phạm
//Ryan Zhang

package wpics.sleepguardian.ui

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import wpics.sleepguardian.ui.navigation.BottomNavBar
import wpics.sleepguardian.ui.navigation.SleepGuardianNavGraph
import wpics.sleepguardian.ui.theme.SleepGuardianTheme
import wpics.sleepguardian.viewmodel.SettingsViewModel
import androidx.compose.foundation.layout.padding


@Composable
fun SleepGuardianApp() {
    val navController = rememberNavController()
    val settingsVm: SettingsViewModel = viewModel()
    val darkTheme by settingsVm.darkThemeEnabled.collectAsState()

    SleepGuardianTheme(darkTheme = darkTheme) {
        Scaffold(
            bottomBar = { BottomNavBar(navController) }
        ) { innerPadding ->
            SleepGuardianNavGraph(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}