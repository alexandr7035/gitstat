package by.alexandr7035.gitstat.view.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.alexandr7035.domain.usecase.profile.GetProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val gtProfileUseCase: GetProfileUseCase,
    private val profileUiMapper: ProfileUiMapper
) : ViewModel() {

    private val profileLiveData = MutableLiveData<ProfileUi>()

    fun load() {
        viewModelScope.launch(Dispatchers.IO) {
            val profile = gtProfileUseCase.execute()

            withContext(Dispatchers.Main) {
                val uiProfile = profileUiMapper.transform(profile)
                profileLiveData.value = uiProfile
            }
        }
    }

    fun getUserLiveData(): LiveData<ProfileUi> {
        return profileLiveData
    }

}