package by.alexandr7035.gitstat.di

import by.alexandr7035.domain.usecase.profile.GetProfileUseCase
import by.alexandr7035.gitstat.view.profile.ProfileUiMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {
    @Provides
    fun provideGetProfileUseCase(): GetProfileUseCase {
        return GetProfileUseCase()
    }

    @Provides
    fun provideProfileUiMapper(): ProfileUiMapper {
        return ProfileUiMapper()
    }
}