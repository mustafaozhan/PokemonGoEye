package com.ozhan.mustafa.pokemongoeye.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ozhan.mustafa.pokemongoeye.R
import kotlinx.android.synthetic.main.activity_browser.*

class BrowserActivity : AppCompatActivity() {
//todo make it like a web browser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browser)
        webw.loadUrl("https://pokemon.gameinfo.io/en/tools/evolution-calculator")
    }
}
