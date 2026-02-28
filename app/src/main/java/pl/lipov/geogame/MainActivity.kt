package pl.lipov.geogame

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView
import org.maplibre.android.style.layers.PropertyFactory.iconAllowOverlap
import org.maplibre.android.style.layers.PropertyFactory.iconIgnorePlacement
import org.maplibre.android.style.layers.PropertyFactory.iconImage
import org.maplibre.android.style.layers.PropertyFactory.iconSize
import org.maplibre.android.style.layers.SymbolLayer
import org.maplibre.android.style.sources.GeoJsonSource

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapLibre.getInstance(this)
        setContent {
            MapScreen()
        }
    }
}

@Composable
fun MapScreen() {
    val context = LocalContext.current
    val lifecycle = androidx.lifecycle.compose.LocalLifecycleOwner.current.lifecycle
    val coroutineScope = rememberCoroutineScope()
    val mapView = remember {
        MapView(context).apply {
            onCreate(null)
        }
    }

    DisposableEffect(lifecycle) {
        val observer = object : DefaultLifecycleObserver {

            override fun onStart(owner: LifecycleOwner) {
                mapView.onStart()
            }

            override fun onResume(owner: LifecycleOwner) {
                mapView.onResume()
            }

            override fun onPause(owner: LifecycleOwner) {
                mapView.onPause()
            }

            override fun onStop(owner: LifecycleOwner) {
                mapView.onStop()
            }

            override fun onDestroy(owner: LifecycleOwner) {
                mapView.onDestroy()
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            mapView.apply {
                getMapAsync { map ->
                    map.setStyle(
                        "https://demotiles.maplibre.org/style.json"
                    ) { style ->
                        if (style.getSource("rocket-source") != null) return@setStyle
                        val start = LatLng(52.2297, 21.0122)
                        val end = LatLng(55.7558, 37.6176)
                        map.cameraPosition = CameraPosition.Builder()
                            .target(
                                LatLng(
                                    (start.latitude + end.latitude) / 2.0,
                                    (start.longitude + end.longitude) / 2.0
                                )
                            )
                            .zoom(4.5)
                            .build()
                        val source = GeoJsonSource(
                            "rocket-source",
                            rocketGeoJson(start.longitude, start.latitude)
                        )
                        style.addSource(source)
                        val original = BitmapFactory.decodeResource(
                            mapView.resources,
                            R.drawable.ic_rocket
                        )
                        val scaled = Bitmap.createScaledBitmap(
                            original,
                            64,
                            64,
                            true
                        )
                        style.addImage("rocket-icon", scaled)
                        val layer = SymbolLayer(
                            "rocket-layer",
                            "rocket-source"
                        ).withProperties(
                            iconImage("rocket-icon"),
                            iconSize(1.0f),
                            iconAllowOverlap(true),
                            iconIgnorePlacement(true)
                        )
                        style.addLayer(layer)
//                        map.cameraPosition = CameraPosition.Builder()
//                            .target(LatLng(51.2, 20.0))
//                            .zoom(3.5)
//                            .build()
                        animateRocket(source, start, end, coroutineScope)
                    }
                }
            }
        }
    )
}

private fun rocketGeoJson(lon: Double, lat: Double): String {
    return """
        {
          "type": "FeatureCollection",
          "features": [
            {
              "type": "Feature",
              "geometry": {
                "type": "Point",
                "coordinates": [$lon, $lat]
              }
            }
          ]
        }
    """.trimIndent()
}

private fun animateRocket(
    source: GeoJsonSource,
    start: LatLng,
    end: LatLng,
    scope: CoroutineScope,
    durationMs: Long = 3000L
) {
    scope.launch {
        var startTime = System.currentTimeMillis()

        while (true) {
            val elapsed = System.currentTimeMillis() - startTime
            val t = (elapsed.toDouble() / durationMs).coerceIn(0.0, 1.0)
            val lat = start.latitude + (end.latitude - start.latitude) * t
            val lon = start.longitude + (end.longitude - start.longitude) * t
            source.setGeoJson(rocketGeoJson(lon, lat))
            if (t >= 1.0) {
                startTime = System.currentTimeMillis()
            }
            delay(16L)
        }
    }
}