package by.alexandr7035.gitstat.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import by.alexandr7035.gitstat.core.Language
import by.alexandr7035.gitstat.data.local.model.RepoLanguage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.collections.arrayListOf

@ProvidedTypeConverter
class RoomTypeConverters(private val gson: Gson) {

    @TypeConverter
    fun fromString(value: String?): ArrayList<String> {
        if (value.isNullOrBlank()) return arrayListOf()
        val listType = object : TypeToken<ArrayList<String>>() {}.type
        return gson.fromJson(value, listType) ?: arrayListOf()
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<String>?): String {
        return gson.toJson(list ?: arrayListOf<String>())
    }

    @TypeConverter
    fun fromLanguage(languages: List<RepoLanguage>?): String? {
        return gson.toJson(languages)
    }

    @TypeConverter
    fun getLanguageFromString(languagesStr: String?): List<RepoLanguage>? {
        val listType = object : TypeToken<List<RepoLanguage>?>() {}.type
        return gson.fromJson(languagesStr, listType)
    }
}