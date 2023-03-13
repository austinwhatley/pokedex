package com.whatley.pokedex.pokemonlist

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toDrawable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.size.Size
import coil.transform.Transformation
import com.whatley.pokedex.R
import com.whatley.pokedex.data.models.PokedexViewEntry

@Composable
fun PokemonListScreen(
    navController: NavController,

) {
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            //TODO: This is the icon for the top of the page.
//            Image(painter = painterResource(), contentDescription = )
            SearchBar()
            Spacer(modifier = Modifier.height(16.dp))
            PokemonList(navController = navController)
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "Search...",
    onSearch: (String) -> Unit = {}
) {
   // Gets two state vairables, text and hint
    var text by remember {
        mutableStateOf("")
    }
    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }
    
    Box(modifier = modifier) {
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black)
        //TODO: Add modifer to make search fancy
        )
    }
    
}

@Composable
fun PokemonList(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val pokemonList by remember {
        viewModel.pokemonList
    }
    val endReached by remember {
        viewModel.endReached
    }
    val loadError by remember {
        viewModel.loadError
    }
    val isLoading by remember {
        viewModel.isLoading
    }

    LazyColumn(contentPadding = PaddingValues(16.dp)){
        //Check if the current result set from the API is an even or odd number
        val itemCount = if (pokemonList.size %2 ==0) {
            pokemonList.size / 2
        } else {
            pokemonList.size / 2 +1
        }
        items(itemCount){
            // Check if we reached the end of the loaded pokemon and if we reached the end of
            // all available pokemon
            if(it >= itemCount - 1 && !endReached){
                viewModel.loadPokemonPagingated()
            }
            //Adds pokemon to the lazy column list.
            PokedexRow(rowIndex = it, entries = pokemonList, navController = navController)
        }
    }
}


@Composable
fun PokedexEntry(
    entry: PokedexViewEntry,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val defualtPrimaryColor  = MaterialTheme.colors.surface
    var pokemonPrimaryColor by remember {
        mutableStateOf(defualtPrimaryColor)
    }

    Box(
        modifier = modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1f)
            .background(
                Brush.verticalGradient(
                    listOf(
                        pokemonPrimaryColor,
                        defualtPrimaryColor
                    )
                )

            ),//TODO: Remove this comma when ready to imlpement onclick action
//            .clickable {
//
//                navController.navigate(
//                    "pokemon_detail_screen/${pokemonPrimaryColor.toArgb()}/${entry.pokemonName}"
//                )
//            },
        contentAlignment = Alignment.Center,
    ) {
        Column {
            //GetImage
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(entry.imageUrl)
                    .crossfade(true)
                    .build(),
                //TODO get placehodler image
                //placeholder = pokemonBall
                contentDescription = entry.pokemonName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .align(CenterHorizontally)
            )
            Text(
                text = entry.pokemonName,
                fontSize = 20.sp,  
                textAlign = TextAlign.Center, 
                modifier = Modifier.fillMaxWidth()
            )
            //Get Name
        }
    }
}


@Composable
fun PokedexRow(
    rowIndex: Int, 
    entries: List<PokedexViewEntry>,
    navController: NavController
){
    Column {
        Row {
            PokedexEntry(
                entry = entries[rowIndex*2],
                navController = navController,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            if(entries.size >= rowIndex *2 +2){
                PokedexEntry(
                    entry = entries[rowIndex*2 + 1],
                    navController = navController,
                    modifier = Modifier.weight(1f)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
