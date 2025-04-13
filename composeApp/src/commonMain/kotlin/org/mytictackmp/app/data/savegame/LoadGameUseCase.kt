package org.mytictackmp.app.data.savegame


import kotlinx.serialization.json.Json
import org.mytictackmp.app.data.SaveGame

interface LoadGameUseCase {
    suspend fun invoke(): Result<SaveGame>
}

class LoadGameUseCaseImpl(
    private val dataStoreManager: DataStoreManager,
    private val json: Json = Json
) : LoadGameUseCase {
    override suspend fun invoke(): Result<SaveGame> {
        return try {
            dataStoreManager.get(
                preferenceKey = DataStoreManager.PreferenceKey.SAVED_GAME
            ).getOrNull()?.let {
                val savedGame = json.decodeFromString<SaveGame>(it)
                Result.success(savedGame)
            } ?: Result.failure(Exception("Game not loaded."))
        } catch (e: Exception) {
            Result.failure(Exception("Game not loaded."))
        }
    }
}
