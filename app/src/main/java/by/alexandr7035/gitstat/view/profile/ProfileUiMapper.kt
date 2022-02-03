package by.alexandr7035.gitstat.view.profile

import android.text.format.DateFormat
import by.alexandr7035.domain.model.ProfileDomain
import by.alexandr7035.gitstat.core.Mapper
import by.alexandr7035.gitstat.view.core.HideableField

class ProfileUiMapper : Mapper<ProfileDomain, ProfileUi> {
    override fun transform(data: ProfileDomain): ProfileUi {
        val locationField = HideableField(value = data.location, isVisible = data.location.isNotEmpty())
        val nameField = HideableField(value = data.name, isVisible = data.name.isNotEmpty())

        val creationDate = DateFormat.format(PROFILE_DATE_FORMAT, data.created_at).toString()
        val updateDate = DateFormat.format(PROFILE_DATE_FORMAT, data.updated_at).toString()

        return ProfileUi(
            id = data.id.toString(),
            login = data.login,

            name = nameField,
            location = locationField,

            followers = data.followers.toString(),

            avatar_url = data.avatar_url,
            created_at = creationDate,
            updated_at = updateDate
        )
    }

    companion object {
        private const val PROFILE_DATE_FORMAT = "dd.MM.yyyy HH:mm"
    }
}