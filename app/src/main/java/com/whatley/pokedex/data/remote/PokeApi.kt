package com.whatley.pokedex.data.remote

import com.whatley.pokedex.data.remote.responses.Pokemon
import com.whatley.pokedex.data.remote.responses.PokemonList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApi {

    //Base URL https://pokeapi.co/api/v2/
    //Gets a paginated list ok Pokemon
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit : Int,
        @Query("offset") offset: Int,
    ): PokemonList

    //Get single Pokemons details
    @GET("pokemon/")
    suspend fun getPokemonInfo(
        @Path("pokemon") pokemon: String
    ) : Pokemon
}