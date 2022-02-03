package by.alexandr7035.domain.repository

import by.alexandr7035.domain.model.ProfileDomain

interface ProfileRepository {
    fun getProfile(): ProfileDomain
}