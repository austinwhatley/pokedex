package com.whatley.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.whatley.pokedex.ui.theme.PokedexTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokedexTheme {
                //Navigation component to handle each screen in the app
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "pokemon_list_screen"){
                    //TODO: Decide if I want a home screen or not
                    //define single screens within the app
                    composable(route = "pokemon_list_screen"){
                        //TODO: Add composable here for list of Pokemon.
                    }
                    composable(
                        //This acts like a url. The {} parameters pass the same named string
                        //to the arguments.
                        route = "pokemon_details_screen/{primaryColor}/{pokemonName}",
                        arguments = listOf(
                            navArgument("primaryColor"){ type = NavType.IntType },
                            navArgument("pokemonName"){ type = NavType.StringType }
                        )
                    ) {
                        val primaryColor = remember {
                            val color = it.arguments?.getInt("primaryColor")
                            color?.let { Color(it) } ?: Color.White
                        }
                        val pokemon = remember {
                            it.arguments?.getString("pokemonName")
                        }

                        //TODO: Add Composable here that takes two args primaryColor, pokemonName
                    }
                }
            }
        }
    }
}

