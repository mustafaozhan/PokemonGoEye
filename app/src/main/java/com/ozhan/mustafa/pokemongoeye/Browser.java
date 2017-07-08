package com.ozhan.mustafa.pokemongoeye;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class Browser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        WebView wv=(WebView)findViewById(R.id.webw);


        wv.loadUrl("https://pokemon.gameinfo.io/en/tools/evolution-calculator");
    }
}
