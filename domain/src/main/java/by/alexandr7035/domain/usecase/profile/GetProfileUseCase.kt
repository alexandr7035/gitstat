package by.alexandr7035.domain.usecase.profile

import by.alexandr7035.domain.model.ProfileDomain

class GetProfileUseCase {
    fun execute(): ProfileDomain {
        return ProfileDomain(
            0,
            "url",
            "name",
            "",
            "",
            0,
            0,
            0,
            0
        )
    }
}