package by.alexandr7035.gitstat.view.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import by.alexandr7035.gitstat.data.UserRepository
import by.alexandr7035.gitstat.data.local.model.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userRepository: UserRepository): ViewModel() {

    fun getUserLiveData(): LiveData<UserEntity> {
        return userRepository.getUserLiveDataFromCache()
    }

}