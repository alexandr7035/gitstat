package by.alexandr7035.gitstat.view.login

import androidx.lifecycle.ViewModel
import by.alexandr7035.gitstat.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository): ViewModel() {

    fun saveToken(token: String) {
        authRepository.saveToken(token)
    }

    fun clearToken() {
        authRepository.clearToken()
    }
}