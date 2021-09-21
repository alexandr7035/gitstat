package com.alexandr7035.gitstat.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alexandr7035.gitstat.data.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository): ViewModel() {

    private val resCodeLiveData = MutableLiveData<Int>()

    fun doLoginRequest(token: String) {
        loginRepository.doLoginRequest(resCodeLiveData, token)
    }

    fun getLoginResponseCodeLiveData(): LiveData<Int> {
        return resCodeLiveData
    }

    fun saveToken(token: String) {
        loginRepository.saveToken(token)
    }
}