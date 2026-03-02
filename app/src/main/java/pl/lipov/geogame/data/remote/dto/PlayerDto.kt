package pl.lipov.geogame.data.remote.dto

import com.google.firebase.firestore.GeoPoint

data class PlayerDto(
    val faction: String = "",
    val lives: Int = 0,
    val resources: Int = 0,
    val shieldsActive: Boolean = false,
    val baseCoordinates: GeoPoint = GeoPoint(0.0, 0.0)
)
