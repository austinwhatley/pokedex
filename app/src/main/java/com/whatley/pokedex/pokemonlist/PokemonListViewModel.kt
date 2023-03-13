package com.whatley.pokedex.pokemonlist

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

import android.telecom.Call
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.whatley.pokedex.data.models.PokedexViewEntry
import com.whatley.pokedex.repository.PokemonRepository
import com.whatley.pokedex.util.CallResponse
import com.whatley.pokedex.util.Constants.PAGE_SIZE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

//Declare that we are going to inject dependencies with Hilt
@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
    ): ViewModel() {

        private var currentPage = 0

        var pokemonList = mutableStateOf<List<PokedexViewEntry>>(listOf())
        var loadError = mutableStateOf("")
        var isLoading = mutableStateOf(false)
        var endReached = mutableStateOf(false)

        init {
            loadPokemonPagingated()
        }

        fun loadPokemonPagingated() {
            isLoading.value = true
            viewModelScope.launch {
                val result = repository.getPokemonList(PAGE_SIZE, currentPage * PAGE_SIZE)
                when (result) {
                    is CallResponse.Success -> {
                        endReached.value = currentPage * PAGE_SIZE >= result.data!!.count
                        val pokedexEntries = result.data.results.mapIndexed { index, entry ->
                            val number = if(entry.url.endsWith("/")){
                                entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                            } else {
                                entry.url.takeLastWhile { it.isDigit() }
                            }
                            val url ="https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
                            PokedexViewEntry(
                                pokemonName = entry.name.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(
                                    Locale.ENGLISH
                                ) else it.toString()
                            },
                                imageUrl = url,
                                pokemonId = number.toInt()
                            )
                        }
                        currentPage++
                        loadError.value = ""
                        isLoading.value = false
                        pokemonList.value += pokedexEntries
                    }
                    is CallResponse.Error -> {
                        loadError.value = result.message!!
                        isLoading.value = false
                    }
                    else -> {
                        loadError.value = "oops"
                        isLoading.value = false
                    }
                }
            }
        }

        fun calcPrimaryColor(drawable: Drawable, onFinish: (Color) -> Unit) {
            val bitmap = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
            Palette.from(bitmap).generate { palette ->
                palette?.dominantSwatch?.rgb?.let { colorValue ->
                    onFinish(Color(colorValue))
                }
            }
        }
}