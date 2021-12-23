package by.alexandr7035.gitstat.data.helpers

import by.alexandr7035.gitstat.core.Language
import by.alexandr7035.gitstat.data.local.model.RepositoryEntity

class LanguagesHelperImpl: LanguagesHelper {
    // FIXME find more optimal way (too many iterations)
    override fun getLanguagesForReposList(repos: List<RepositoryEntity>): List<Language> {
        val languagesList = ArrayList<Language>()

        repos.forEach {
            languagesList.add(Language(it.primaryLanguage, it.primaryLanguageColor, 0))
        }

        val trimmedLanguages = languagesList.distinct()

        repos.forEach { repo ->
            trimmedLanguages.forEach { language ->
                if (repo.primaryLanguage == language.name) {
                    language.count += 1
                }
            }
        }

        return trimmedLanguages.sortedBy {
            it.name
        }
    }
}