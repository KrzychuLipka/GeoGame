package pl.lipov.geogame.data.repository

import pl.lipov.geogame.data.mapper.toGameStatus
import pl.lipov.geogame.data.remote.firestore.GameRemoteDataSource
import pl.lipov.geogame.domain.model.GameStatus
import pl.lipov.geogame.domain.repository.GameRepository

class GameRemoteRepository : GameRepository {

    override suspend fun fetchGameStatus(): GameStatus? =
        GameRemoteDataSource.fetchGameState()?.toGameStatus()
}
