package com.ozhan.mustafa.pokemongoeye

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_browser.*

class Browser : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browser)
        webw.loadUrl("https://pokemon.gameinfo.io/en/tools/evolution-calculator")
    }
}
