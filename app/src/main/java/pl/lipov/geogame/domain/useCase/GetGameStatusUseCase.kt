package pl.lipov.geogame.domain.useCase

import pl.lipov.geogame.domain.model.GameStatus
import pl.lipov.geogame.domain.repository.GameRepository

class GetGameStatusUseCase(
    private val repository: GameRepository
) {

    suspend operator fun invoke(): GameStatus? = repository.fetchGameStatus()
}
