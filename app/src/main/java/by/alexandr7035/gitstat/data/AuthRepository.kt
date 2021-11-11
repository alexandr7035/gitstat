package by.alexandr7035.gitstat.data

import by.alexandr7035.gitstat.core.KeyValueStorage
import javax.inject.Inject

class AuthRepository @Inject constructor(private val keyValueStorage: KeyValueStorage) {

    fun saveToken(token: String) {
        keyValueStorage.saveToken(token)
    }

}