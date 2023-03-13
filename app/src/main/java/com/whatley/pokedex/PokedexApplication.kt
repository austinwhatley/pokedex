package com.whatley.pokedex

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PokedexApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}