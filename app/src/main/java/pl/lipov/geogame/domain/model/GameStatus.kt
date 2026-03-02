package pl.lipov.geogame.domain.model

data class GameStatus(
    val activeRound: Int,
    val gameState: String,
    val player1: Player,
    val player2: Player,
    val lastMoveBy: String
)
