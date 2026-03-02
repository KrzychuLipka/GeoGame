package pl.lipov.geogame.data.remote.dto

data class GameStatusDto(
    val activeRound: Int = 0,
    val gameState: String = "",
    val player1: PlayerDto = PlayerDto(),
    val player2: PlayerDto = PlayerDto(),
    val lastMoveBy: String = ""
)
