package pl.lipov.geogame.domain.model

import org.maplibre.android.geometry.LatLng
import pl.lipov.geogame.domain.config.GameConfig

data class Faction(
    val type: FactionType,
    val baseCoordinates: LatLng,
    val lives: Int = GameConfig.LIVES,
    val resources: Int = GameConfig.RESOURCES,
    val shieldsActive: Boolean = false
)

