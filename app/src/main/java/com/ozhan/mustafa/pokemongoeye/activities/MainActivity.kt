@file:Suppress("DEPRECATION")

package com.ozhan.mustafa.pokemongoeye.activities

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.NotificationCompat
import android.util.Log
import android.widget.Toast

import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.InterstitialAd
import com.google.firebase.analytics.FirebaseAnalytics
import com.ozhan.mustafa.pokemongoeye.util.NotificationListener
import com.ozhan.mustafa.pokemongoeye.R
import com.ozhan.mustafa.pokemongoeye.util.HttpHandler
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync


class MainActivity : AppCompatActivity() {
    var urlCheck: String? = null
    var interstitial1 = InterstitialAd(this)
    var interstitial2 = InterstitialAd(this)

    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        setListeners()
        howManyMinutes.setText("5")
        showBannerAd()
        interstitial1.adUnitId = getString(R.string.interstitial1)
        interstitial2.adUnitId = getString(R.string.interstitial2)

    }

    private fun setListeners() {
        calculator.setOnClickListener {
            calculatorInterstitialAd()
            val intent = Intent(this@MainActivity, BrowserActivity::class.java)
            startActivity(intent)
        }
        button.setOnClickListener {
            textView.setTextColor(Color.WHITE)
            if (radioGoogle.isChecked)
                urlCheck = "http://cmmcd.com/PokemonGo/"
            if (radioTrainerClub.isChecked)
                urlCheck = "http://cmmcd.com/PokemonTrainerClub/"
            textView.text = "Checking.."
            getData()
        }
        startPokemonGoEye.setOnClickListener { startPokemonGoEye() }
    }

    private fun calculatorInterstitialAd() {

        val adRequest1 = AdRequest.Builder().build()
        interstitial2.loadAd(adRequest1)
        interstitial2.adListener = object : AdListener() {
            override fun onAdLoaded() {
                if (interstitial2.isLoaded) {
                    interstitial2.show()
                }
            }
        }
    }

    private fun startPokemonGoEye() {
        val adRequest1 = AdRequest.Builder().build()

        interstitial1.loadAd(adRequest1)

        interstitial1.adListener = object : AdListener() {
            override fun onAdLoaded() {
                if (interstitial1.isLoaded) {
                    interstitial1.show()
                }
            }


            override fun onAdClosed() {
                val handler = Handler()
                handler.postDelayed(object : Runnable {
                    override fun run() {

                        if (radioOnlyOnline.isChecked) {
                            if (radioGoogle.isChecked)
                                urlCheck = "http://cmmcd.com/PokemonGo/"
                            if (radioTrainerClub.isChecked)
                                urlCheck = "http://cmmcd.com/PokemonTrainerClub/"

                            getData()
                            if (textView.text.toString() == "Online") {
                                val intent = Intent(this@MainActivity, NotificationListener::class.java)
                                startService(intent)
                                create()
                            }

                        } else if (radioOnlineAndUnstable.isChecked) {
                            if (radioGoogle.isChecked)
                                urlCheck = "http://cmmcd.com/PokemonGo/"
                            else if (radioTrainerClub.isChecked)
                                urlCheck = "http://cmmcd.com/PokemonTrainerClub/"
                            getData()
                            if (textView.text.toString() == "Online" || textView.text.toString() == "Unstable") {
                                val intent = Intent(this@MainActivity, NotificationListener::class.java)
                                startService(intent)
                                create()
                            }
                        }
                        handler.postDelayed(this, (howManyMinutes.text.toString().toInt() * 60000).toLong())
                    }
                }, (howManyMinutes.text.toString().toInt() * 60000).toLong())

                minimizeApp()
                Toast.makeText(this@MainActivity, R.string.started, Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getData() {

        doAsync {
            val myString = HttpHandler().makeServiceCall(urlCheck!!)
            Log.d("asdsada", myString
            )
            runOnUiThread {
                when {
                    myString.contains("Online!") -> {
                        textView.text = "Online"
                        textView.setTextColor(Color.GREEN)
                    }
                    myString.contains("Unstable!") -> {
                        textView.text = "Unstable"
                        textView.setTextColor(Color.YELLOW)
                    }
                    myString.contains("Offline!") -> {
                        textView.text = "Offline"
                        textView.setTextColor(Color.RED)
                    }
                    else -> {
                        textView.text = "Offline"
                        textView.setTextColor(Color.RED)
                    }
                }

            }
        }
    }

    private fun showBannerAd() {
        val mAdView1 = findViewById<AdView>(R.id.adView_1)
        val adRequest1 = AdRequest.Builder().build()

        mAdView1.loadAd(adRequest1)

        val mAdView2 = findViewById<AdView>(R.id.adView_3)
        val adRequest2 = AdRequest.Builder().build()

        mAdView2.loadAd(adRequest2)
    }

    fun minimizeApp() {


        val startMain = Intent(Intent.ACTION_MAIN)
        startMain.addCategory(Intent.CATEGORY_HOME)
        startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(startMain)
    }

    fun create() {
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)


        val nManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val nComp = NotificationCompat.Builder(this)
        nComp.setSound(uri)
        nComp.setContentTitle("Eye of Pokemon Go")
        nComp.setContentText("Now you can play Pokemon Go!")
        nComp.setSmallIcon(R.drawable.notification_icon)
        nComp.setAutoCancel(true)
        nManager.notify(System.currentTimeMillis().toInt(), nComp.build())

        try {
            val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val r = RingtoneManager.getRingtone(applicationContext, notification)
            r.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
