package pl.lipov.geogame.data.mapper

import com.google.firebase.firestore.GeoPoint
import org.maplibre.android.geometry.LatLng
import pl.lipov.geogame.data.remote.dto.PlayerDto
import pl.lipov.geogame.domain.model.FactionType
import pl.lipov.geogame.domain.model.Player

fun Player.toDto(): PlayerDto = PlayerDto(
    faction = this.faction.name,
    lives = this.lives,
    resources = this.resources,
    shieldsActive = this.shieldsActive,
    baseCoordinates = GeoPoint(this.baseCoordinates.latitude, this.baseCoordinates.longitude)
)

fun PlayerDto.toDomain(): Player {
    val factionType = FactionType
        .entries
        .firstOrNull { it.name == this.faction }
        ?: FactionType.entries.first()
    return Player(
        faction = factionType,
        lives = this.lives,
        resources = this.resources,
        shieldsActive = this.shieldsActive,
        baseCoordinates = LatLng(
            this.baseCoordinates.latitude,
            this.baseCoordinates.longitude
        )
    )
}
