package com.ozhan.mustafa.pokemongoeye

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView

class Browser : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browser)
        val wv = findViewById(R.id.webw) as WebView


        wv.loadUrl("https://pokemon.gameinfo.io/en/tools/evolution-calculator")
    }
}
