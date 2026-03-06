//Gia Huy Phạm
//Ryan Zhang

package wpics.sleepguardian.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = SG_Blue,
    secondary = SG_Mint,
    tertiary = SG_Orange,
    background = SG_BackgroundLight,
    surface = SG_BackgroundLight,
)

private val DarkColors = darkColorScheme(
    primary = SG_Mint,
    secondary = SG_Blue,
    tertiary = SG_Orange,
    background = SG_BackgroundDark,
    surface = SG_SurfaceDark,
)

@Composable
fun SleepGuardianTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = SleepGuardianTypography,
        content = content
    )
}