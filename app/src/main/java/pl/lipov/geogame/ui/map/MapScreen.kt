package pl.lipov.geogame.ui.map

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView
import pl.lipov.geogame.R
import pl.lipov.geogame.domain.model.FactionType
import pl.lipov.geogame.domain.model.GameState
import pl.lipov.geogame.ui.theme.Dimens

private const val MAP_STYLE = "https://demotiles.maplibre.org/style.json"
private const val DEFAULT_ZOOM_LEVEL = 4.5

@Composable
fun MapScreen(
    userFactionType: FactionType
) {
    val viewModel: MapViewModel = viewModel()

    LaunchedEffect(userFactionType) {
        viewModel.startGame(userFactionType)
    }

    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply { onCreate(null) }
    }

    mapView.BindLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        val target = viewModel.userFaction?.baseCoordinates
        Map(mapView, target)

        StatusPanel(
            modifier = Modifier.align(Alignment.TopCenter),
            viewModel = viewModel
        )

        Buttons(
            modifier = Modifier.align(Alignment.BottomStart),
            viewModel = viewModel
        )
    }
}

@Composable
fun StatusPanel(
    modifier: Modifier = Modifier,
    viewModel: MapViewModel
) {
    val user = viewModel.userFaction
    val enemy = viewModel.enemyFaction
    val round = viewModel.activeRound

    if (user == null || enemy == null) return

    Card(
        modifier = modifier
            .padding(Dimens.PaddingStandard)
    ) {
        Column(
            modifier = Modifier.padding(Dimens.PaddingStandard)
        ) {
            Text(
                text = "Runda: $round",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(Dimens.PaddingSmall))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FactionStats(
                    title = user.type.name.lowercase().replaceFirstChar { it.uppercase() },
                    lives = user.lives,
                    resources = user.resources,
                    shieldsActive = user.shieldsActive
                )

                FactionStats(
                    title = enemy.type.name.lowercase().replaceFirstChar { it.uppercase() },
                    lives = enemy.lives,
                    resources = enemy.resources,
                    shieldsActive = enemy.shieldsActive
                )
            }
        }
    }
}

@Composable
private fun FactionStats(
    title: String,
    lives: Int,
    resources: Int,
    shieldsActive: Boolean
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall
        )
        Text("Życia: $lives")
        Text("Zasoby: $resources")
        Text("Tarcza: ${if (shieldsActive) "aktywna" else "wyłączona"}")
    }
}

@Composable
fun Buttons(
    modifier: Modifier,
    viewModel: MapViewModel
) {
    Column(modifier = modifier.padding(Dimens.PaddingStandard)) {
        val enabled = viewModel.gameState == GameState.IN_PROGRESS
        ActionButton(R.string.attack_base, enabled) { viewModel.attackBase() }
        Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
        ActionButton(R.string.destroy_resources, enabled) { viewModel.destroyResources() }
        Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
        ActionButton(R.string.strengthen_defenses, enabled) { viewModel.strengthenDefenses() }
        Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
        ActionButton(R.string.mine_gold, enabled) { viewModel.mineGold() }
    }
}

@Composable
fun ActionButton(
    @StringRes textResId: Int,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled
    ) {
        Text(stringResource(textResId))
    }
}

@Composable
fun Map(
    mapView: MapView,
    target: LatLng?
) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { mapView },
        update = {
            it.setUp(target)
        }
    )
}

@Composable
private fun MapView.BindLifecycle() {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        val observer = object : DefaultLifecycleObserver {

            override fun onStart(owner: LifecycleOwner) {
                onStart()
            }

            override fun onResume(owner: LifecycleOwner) {
                onResume()
            }

            override fun onPause(owner: LifecycleOwner) {
                onPause()
            }

            override fun onStop(owner: LifecycleOwner) {
                onStop()
            }

            override fun onDestroy(owner: LifecycleOwner) {
                onDestroy()
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}

fun MapView.setUp(
    target: LatLng?
) {
    getMapAsync { map ->
        map.setStyle(MAP_STYLE) {
            target?.let {
                map.cameraPosition = CameraPosition.Builder()
                    .target(it)
                    .zoom(DEFAULT_ZOOM_LEVEL)
                    .build()
            }
        }
    }
}