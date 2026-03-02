package pl.lipov.geogame.domain.repository

import pl.lipov.geogame.domain.model.GameStatus

interface GameRepository {

    suspend fun fetchGameStatus(): GameStatus?
}
