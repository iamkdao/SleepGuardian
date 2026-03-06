//Gia Huy Phạm
//Ryan Zhang

package wpics.sleepguardian.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import wpics.sleepguardian.ui.screens.context.ContextLogicScreen
import wpics.sleepguardian.ui.screens.dashboard.DashboardScreen
import wpics.sleepguardian.ui.screens.privacy.PrivacyScreen
import wpics.sleepguardian.ui.screens.settings.SettingsScreen

@Composable
fun SleepGuardianNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
        modifier = modifier
    ) {
        composable(Screen.Dashboard.route) { DashboardScreen() }
        composable(Screen.Context.route) { ContextLogicScreen() }
        composable(Screen.Privacy.route) { PrivacyScreen() }
        composable(Screen.Settings.route) { SettingsScreen() }
    }
}