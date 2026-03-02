package pl.lipov.geogame.data.mapper

import pl.lipov.geogame.data.remote.dto.GameStatusDto
import pl.lipov.geogame.domain.model.GameStatus

fun GameStatusDto.toGameStatus() = GameStatus(
    activeRound = this.activeRound,
    gameState = this.gameState,
    player1 = this.player1.toDomain(),
    player2 = this.player2.toDomain(),
    lastMoveBy = this.lastMoveBy
)
