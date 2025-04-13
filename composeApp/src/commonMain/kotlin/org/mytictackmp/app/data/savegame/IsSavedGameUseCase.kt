package org.mytictackmp.app.data.savegame


interface IsSavedGameUseCase {
    suspend fun invoke(): Boolean
}

class IsSavedGameUseCaseImpl(
    private val dataStoreManager: DataStoreManager
) : IsSavedGameUseCase {
    override suspend fun invoke(): Boolean {
        return try {
            val savedGame =
                dataStoreManager.get(DataStoreManager.PreferenceKey.SAVED_GAME).getOrNull()
            savedGame != null
        } catch (e: Exception) {
            false
        }
    }
}
