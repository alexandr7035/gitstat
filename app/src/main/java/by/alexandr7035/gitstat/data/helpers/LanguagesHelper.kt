package by.alexandr7035.gitstat.data.helpers

import by.alexandr7035.gitstat.core.Language
import by.alexandr7035.gitstat.data.local.model.RepositoryEntity

// TODO rename language to languageCount and move to the data layer
interface LanguagesHelper {
    fun getLanguagesForReposList(repos: List<RepositoryEntity>): List<Language>
}