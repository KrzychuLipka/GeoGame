package pl.lipov.geogame.data.repository

import org.maplibre.android.geometry.LatLng
import pl.lipov.geogame.domain.model.Faction
import pl.lipov.geogame.domain.model.FactionType

object FactionRepository {

    val factions = listOf(
        Faction(
            type = FactionType.POLANDIA,
            baseCoordinates = LatLng(52.2297, 21.0122)
        ),
        Faction(
            type = FactionType.AFRYKANIA,
            baseCoordinates = LatLng(9.03, 38.74)
        )
    )
}
