package pl.lipov.geogame.data.remote.firestore

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import pl.lipov.geogame.data.remote.dto.GameStatusDto
import pl.lipov.geogame.data.mapper.toDto
import pl.lipov.geogame.domain.model.Player
import pl.lipov.geogame.domain.model.GameState

object GameRemoteDataSource {

    private const val TAG = "pw.GameRemoteDataSource"
    private const val GAME_COLLECTION_NAME = "game"
    private const val GAME_DOCUMENT_ID = "UXtxLPMYIcPRi8NbfoGa"

    suspend fun fetchGameState(): GameStatusDto? {
        return try {
            val snapshot = Firebase.firestore
                .collection(GAME_COLLECTION_NAME)
                .document(GAME_DOCUMENT_ID)
                .get()
                .await()
            snapshot.toObject(GameStatusDto::class.java)
        } catch (exception: Exception) {
            Log.e(TAG, exception.localizedMessage, exception)
            null
        }
    }

    suspend fun createNewGame(
        userFaction: Player,
        enemyFaction: Player
    ) {
        val game = GameStatusDto(
            activeRound = 1,
            gameState = GameState.IN_PROGRESS.name,
            player1 = userFaction.toDto(),
            player2 = enemyFaction.toDto(),
            lastMoveBy = ""
        )

        Firebase.firestore
            .collection(GAME_COLLECTION_NAME)
            .document(GAME_DOCUMENT_ID)
            .set(game)
            .await()
    }
}
