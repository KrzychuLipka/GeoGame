package pl.lipov.geogame.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.maplibre.android.MapLibre
import pl.lipov.geogame.domain.model.NavDestination
import pl.lipov.geogame.ui.map.MapScreen
import pl.lipov.geogame.ui.theme.AppTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val navController = rememberNavController()
                Scaffold { padding ->
                    NavHost(navController, startDestination = NavDestination.FACTION.name) {

                        composable(NavDestination.FACTION.name) {
                            FactionSelectionScreen(
                                modifier = Modifier.Companion.padding(padding),
                                onFactionSelected = { faction ->
                                    viewModel.onFactionSelected(faction)
                                    navController.navigate(NavDestination.MAP.name)
                                }
                            )
                        }

                        composable(NavDestination.MAP.name) {
                            viewModel.selectedFactionType?.let { factionType ->
                                MapLibre.getInstance(this@MainActivity)
                                MapScreen(factionType)
                            }
                        }
                    }
                }
            }
        }
    }
}