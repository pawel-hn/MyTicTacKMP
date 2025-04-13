package org.mytictackmp.app.data.savegame

import kotlinx.serialization.json.Json
import org.mytictackmp.app.data.SaveGame


interface SaveGameUseCase {
    suspend fun invoke(savedGame: SaveGame): Result<Unit>
}

class SaveGameUseCaseImpl(
    private val dataStoreManager: DataStoreManager,
    private val json: Json = Json
) : SaveGameUseCase {
    override suspend fun invoke(savedGame: SaveGame): Result<Unit> {
        return try {
            val data = json.encodeToString(SaveGame.serializer(), savedGame)
            dataStoreManager.store(
                preferenceKey = DataStoreManager.PreferenceKey.SAVED_GAME,
                value = data
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Game not saved."))
        }
    }
}
