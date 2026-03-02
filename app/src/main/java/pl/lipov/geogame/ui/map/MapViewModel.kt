package pl.lipov.geogame.ui.map

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.lipov.geogame.data.repository.GameRemoteRepository
import pl.lipov.geogame.domain.config.GameConfig
import pl.lipov.geogame.domain.model.FactionType
import pl.lipov.geogame.domain.model.GameState
import pl.lipov.geogame.domain.model.Player
import pl.lipov.geogame.domain.useCase.GetGameStatusUseCase
import kotlin.math.max
import kotlin.random.Random

class MapViewModel : ViewModel() {

    companion object {
        private const val TAG = "pw.MapViewModel"
    }

    private val getGameStatusUseCase = GetGameStatusUseCase(
        repository = GameRemoteRepository()
    )
    var activeRound by mutableIntStateOf(1)
        private set

    var userPlayer by mutableStateOf<Player?>(null)
        private set

    var enemyPlayer by mutableStateOf<Player?>(null)
        private set

    var gameState by mutableStateOf(GameState.IN_PROGRESS)
        private set

    private var shieldTimer = 0

    fun startGame(
        userFactionType: FactionType
    ) {
        viewModelScope.launch {
            Log.d(TAG, "Start game! Game status:")
            val gameStatus = getGameStatusUseCase() ?: return@launch
            Log.d(TAG, "activeRound: ${gameStatus.activeRound}")
            Log.d(TAG, "gameState: ${gameStatus.gameState}")
            val player1 = gameStatus.player1
            Log.d(
                TAG,
                "player1: type=${player1.faction.name}; lives=${player1.lives}; resources=${player1.resources}; shieldsActive=${player1.shieldsActive}; baseCoordinates=${player1.baseCoordinates.latitude};${player1.baseCoordinates.longitude}"
            )
            val player2 = gameStatus.player2
            Log.d(
                TAG,
                "player2: type=${player2.faction.name}; lives=${player2.lives}; resources=${player2.resources}; shieldsActive=${player2.shieldsActive}; baseCoordinates=${player2.baseCoordinates.latitude};${player2.baseCoordinates.longitude}"
            )

            if (userFactionType == player1.faction) {
                userPlayer = player1
                enemyPlayer = player2
            } else {
                userPlayer = player2
                enemyPlayer = player1
            }
            activeRound = gameStatus.activeRound
            gameState = GameState
                .entries
                .firstOrNull { it.name == gameStatus.gameState }
                ?: GameState.IN_PROGRESS
            shieldTimer = 0
        }
    }

    fun attackBase() {
        val enemy = enemyPlayer ?: return

        val enemyShieldActive = enemy.shieldsActive
        val hit = !enemyShieldActive || Random.nextBoolean()

        if (hit) {
            enemyPlayer = enemy.copy(
                lives = max(0, enemy.lives - GameConfig.ATTACK_POWER)
            )
        }

        finishRound()
    }

    fun destroyResources() {
        val enemy = enemyPlayer ?: return

        val shieldActive = enemy.shieldsActive
        val hit = !shieldActive || Random.nextInt(4) != 3

        if (hit) {
            enemyPlayer = enemy.copy(
                resources = max(0, enemy.resources - GameConfig.ATTACK_POWER)
            )
        }

        finishRound()
    }

    fun strengthenDefenses() {
        val user = userPlayer ?: return
        if (user.resources < GameConfig.DEFENSE_COST) return

        userPlayer = user.copy(
            resources = user.resources - GameConfig.DEFENSE_COST,
            shieldsActive = true
        )

        shieldTimer = GameConfig.SHIELD_DURATION
        finishRound()
    }

    fun mineGold() {
        val user = userPlayer ?: return
        userPlayer = user.copy(resources = user.resources + GameConfig.RESOURCE_PRODUCTION)
        finishRound()
    }

    private fun finishRound() {
        val enemy = enemyPlayer ?: return
        val user = userPlayer ?: return

        if (enemy.lives == 0) {
            gameState = GameState.WIN
            return
        }

        if (activeRound == GameConfig.NUM_OF_ROUNDS) {
            gameState = when {
                user.lives > enemy.lives -> GameState.WIN
                user.lives < enemy.lives -> GameState.LOSE
                else -> GameState.DRAW
            }
            return
        }

        activeRound++

        if (shieldTimer > 0) {
            shieldTimer--
            if (shieldTimer == 0) {
                userPlayer = user.copy(
                    shieldsActive = false
                )
            }
        }
    }
}
