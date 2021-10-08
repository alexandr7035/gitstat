package com.alexandr7035.gitstat.view.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexandr7035.gitstat.core.AppPreferences
import com.alexandr7035.gitstat.core.AuthStatus
import com.alexandr7035.gitstat.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val appPreferences: AppPreferences): ViewModel() {

    private val authResultLiveData = MutableLiveData<AuthStatus>()

    fun authorize() {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.authorize(authResultLiveData)
        }
    }

    fun saveToken(token: String) {
        appPreferences.token = token
    }

    fun getAuthResultLiveData(): MutableLiveData<AuthStatus> {
        return authResultLiveData
    }
}