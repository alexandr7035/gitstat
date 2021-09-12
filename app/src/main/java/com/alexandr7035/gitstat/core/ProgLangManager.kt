package com.alexandr7035.gitstat.core

import android.content.Context
import com.alexandr7035.gitstat.R
import com.alexandr7035.gitstat.data.local.model.RepositoryEntity
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader
import java.util.*
import kotlin.collections.ArrayList

class ProgLangManager(context: Context) {

    private var languagesColorsList: Map<String, Map<String, String>>? = null
    private val colorUnknownLanguageOrNullColor = "#C3C3C3"
    private val UNKNOWN_LANGUAGE = "Unknown"

    init {
        val inputStream = context.resources.openRawResource(R.raw.language_colors)
        val reader = InputStreamReader(inputStream)
        val builder = GsonBuilder()
        val itemsMapType = object : TypeToken<Map<String, Map<String, String>>>() {}.type

        languagesColorsList = builder.create().fromJson(reader, itemsMapType)
    }


    fun getLanguageColor(language: String): String {
        return when(language) {
            // When repo language can't be defined
            // E.g when there are no commits in main branch
            UNKNOWN_LANGUAGE -> colorUnknownLanguageOrNullColor

            else -> {
                when (languagesColorsList!![language]!!["color"]) {
                    // When Lang color is explicitly defined in JSON as null ¯\_(-_-)_/
                    null -> colorUnknownLanguageOrNullColor
                    else -> languagesColorsList!![language]!!["color"]!!
                }
            }
        }
    }


    // TODO simplify - too many loops
    fun getLanguagesList(repositories: List<RepositoryEntity>): List<Language> {

        val languagesList = ArrayList<Language>()

        val languagesTreeMap: TreeMap<String, Int> = TreeMap()

        // Init languages map
        repositories.forEachIndexed { i, repo ->
            languagesTreeMap[repo.language] = 0
        }

        // Populate languages map
        repositories.forEachIndexed { i, repo ->
            languagesTreeMap[repo.language] = languagesTreeMap[repo.language]!! + 1
        }

        languagesTreeMap.forEach {
            languagesList.add(
                Language(
                    name = it.key,
                    count = it.value,
                    color = getLanguageColor(it.key)
            ))
        }

        return languagesList
    }

}