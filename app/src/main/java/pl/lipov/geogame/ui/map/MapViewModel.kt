package pl.lipov.geogame.ui.map

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import pl.lipov.geogame.data.repository.FactionRepository
import pl.lipov.geogame.domain.config.GameConfig
import pl.lipov.geogame.domain.model.Faction
import pl.lipov.geogame.domain.model.FactionType
import pl.lipov.geogame.domain.model.GameState
import kotlin.math.max
import kotlin.random.Random

class MapViewModel : ViewModel() {

    var activeRound by mutableIntStateOf(1)
        private set

    var userFaction by mutableStateOf<Faction?>(null)
        private set

    var enemyFaction by mutableStateOf<Faction?>(null)
        private set

    var gameState by mutableStateOf(GameState.IN_PROGRESS)
        private set

    private var shieldTimer = 0

    fun startGame(
        userFactionType: FactionType
    ) {
        userFaction = getFaction { it == userFactionType }
        enemyFaction = getFaction { it != userFactionType }
        activeRound = 1
        gameState = GameState.IN_PROGRESS
        shieldTimer = 0
    }

    private fun getFaction(
        predicate: (FactionType) -> Boolean
    ): Faction = FactionRepository
        .factions
        .first { faction -> predicate(faction.type) }
        .copy()

    fun attackBase() {
        val enemy = enemyFaction ?: return

        val enemyShieldActive = enemy.shieldsActive
        val hit = !enemyShieldActive || Random.nextBoolean()

        if (hit) {
            enemyFaction = enemy.copy(
                lives = max(0, enemy.lives - GameConfig.ATTACK_POWER)
            )
        }

        finishRound()
    }

    fun destroyResources() {
        val enemy = enemyFaction ?: return

        val shieldActive = enemy.shieldsActive
        val hit = !shieldActive || Random.nextInt(4) != 3

        if (hit) {
            enemyFaction = enemy.copy(
                resources = max(0, enemy.resources - GameConfig.ATTACK_POWER)
            )
        }

        finishRound()
    }

    fun strengthenDefenses() {
        val user = userFaction ?: return
        if (user.resources < GameConfig.DEFENSE_COST) return

        userFaction = user.copy(
            resources = user.resources - GameConfig.DEFENSE_COST,
            shieldsActive = true
        )

        shieldTimer = GameConfig.SHIELD_DURATION
        finishRound()
    }

    fun mineGold() {
        val user = userFaction ?: return
        userFaction = user.copy(resources = user.resources + GameConfig.RESOURCE_PRODUCTION)
        finishRound()
    }

    private fun finishRound() {
        val enemy = enemyFaction ?: return
        val user = userFaction ?: return

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
                userFaction = user.copy(
                    shieldsActive = false
                )
            }
        }
    }
}
