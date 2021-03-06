package com.akin.leagueoflegends.domains.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akin.leagueoflegends.data.models.allcharacternameanddata.CharacterModel
import com.akin.leagueoflegends.data.models.characterlargedata.Character
import com.akin.leagueoflegends.domains.repository.ChampionRepository
import com.akin.leagueoflegends.util.Statics.BASE_IMAGE_URL
import com.akin.leagueoflegends.util.Statics.BASE_SQUARE_URL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject
import com.google.gson.*


@HiltViewModel
class ChampionFragmentViewModel
@Inject
constructor(
    private val repository: ChampionRepository,

    ) :
    ViewModel() {

    private val _response = MutableLiveData<Character>()
    val response: LiveData<Character> = _response

    private val _championSkinNumbers = mutableListOf<String>()
    val championSkinNumbers: List<String> = _championSkinNumbers

    private val _championNameAndData = MutableLiveData<List<CharacterModel>>()
    val championNameAndData: LiveData<List<CharacterModel>> = _championNameAndData

    init {

        getChampionNames()
    }

    fun getChampion(championName: String) = viewModelScope.launch {
        repository.getChampion(championName).let { response ->

            if (response.isSuccessful) {
                val gson = Gson()
                val gsonString = gson.toJson(response.body()!!.characterData).toString()
                val obj = JsonParser().parse(gsonString).asJsonObject[championName]
                val teta = gson.fromJson(obj, Character::class.java)

                _response.postValue(teta)
                teta.skins.forEach {
                    _championSkinNumbers.add(it.num.toString())
                }

            } else {
                Log.d("Tag", "Error:${response.message()}")
            }
        }
    }

    fun getChampionImageWithSkinNumbers(
        championName: String,
        championSkinNumber: String,
        type: String
    ): String {

        return type + "${championName}_${championSkinNumber}.jpg"
    }

    fun getChampionSkillImages(skillName: String, type: String): String {

        return type + "${skillName}.png"
    }

    fun getChampionSquareImage(championName: String): String {

        return BASE_SQUARE_URL + "${championName}.png"
    }

    private fun getChampionNames() {
        viewModelScope.launch {
            repository.getCharacterNames().let { response ->
                println(response)
                if (response.isSuccessful) {
                    val gson = Gson()
                    val gsonString = gson.toJson(response.body()).toString()
                    val obj = JsonParser().parse(gsonString).asJsonObject["data"]
                    val list = mutableListOf<CharacterModel>()
                    val beta = obj.asJsonObject.keySet()
                    beta.forEach {
                        val teta = gson.fromJson(obj.asJsonObject[it], CharacterModel::class.java)
                        list.add(teta)
                    }
                    _championNameAndData.value = list

                } else {
                    println(response.message())
                }
            }

        }

    }

}