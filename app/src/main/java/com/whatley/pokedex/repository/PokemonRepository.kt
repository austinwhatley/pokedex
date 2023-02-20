package com.whatley.pokedex.repository

import com.whatley.pokedex.data.remote.PokeApi
import com.whatley.pokedex.data.remote.responses.Pokemon
import com.whatley.pokedex.data.remote.responses.PokemonList
import com.whatley.pokedex.util.CallResponse
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class PokemonRepository  @Inject constructor(
    private val pokeApi: PokeApi
){

    //This will get a list of pokemon
    suspend fun getPokemonList(limit: Int, offset: Int) : CallResponse<PokemonList> {
        val response = try {
            pokeApi.getPokemonList(limit, offset)
        } catch (e: Exception) {
            //If there was any error, log it here
            return CallResponse.Error(message = "Error Occurred")
        }
        //If it was successful, return the response body
        return CallResponse.Success(response)
    }

    suspend fun getPokemonDetail(pokemon: String) : CallResponse<Pokemon> {
        val response = try {
            pokeApi.getPokemonInfo(pokemon)
        } catch (e: Exception) {
            return CallResponse.Error(message = "Error Occurred")
        }
        return CallResponse.Success(response)
    }

}