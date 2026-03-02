package pl.lipov.geogame.domain.model

import org.maplibre.android.geometry.LatLng

data class Player(
    val faction: FactionType,
    val lives: Int,
    val resources: Int,
    val shieldsActive: Boolean,
    val baseCoordinates: LatLng,
)

