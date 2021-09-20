package com.alexandr7035.gitstat.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alexandr7035.gitstat.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    private val resCodeLiveData = MutableLiveData<Int>()

    fun doLoginRequest(token: String) {
        repository.doLoginRequest(resCodeLiveData, token)
    }

    fun getLoginResponseCodeLiveData(): LiveData<Int> {
        return resCodeLiveData
    }

    fun saveToken(token: String) {
        repository.saveToken(token)
    }
}