//Gia Huy Phạm
//Ryan Zhang

package wpics.sleepguardian.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    data object Dashboard : Screen("dashboard", "Dashboard", Icons.Filled.Dashboard)
    data object Context : Screen("context", "Context", Icons.Filled.Tune)
    data object Privacy : Screen("privacy", "Privacy", Icons.Filled.PrivacyTip)
    data object Settings : Screen("settings", "Settings", Icons.Filled.Settings)
}

val bottomNavScreens = listOf(
    Screen.Dashboard,
    Screen.Context,
    Screen.Privacy,
    Screen.Settings
)