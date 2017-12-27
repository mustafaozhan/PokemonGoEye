@file:Suppress("DEPRECATION")

package com.ozhan.mustafa.pokemongoeye

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
import android.widget.Toast

import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.InterstitialAd
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync

import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

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
        howManyMinutes.setText("5")
        showBannerAd()
        interstitial1.adUnitId = getString(R.string.interstitial1)



        calculator.setOnClickListener {
           calculatorInterstitialAd()

            val intent = Intent(this@MainActivity, Browser::class.java)
            startActivity(intent)
        }

        button.setOnClickListener {
            if (radioGoogle.isChecked)
                urlCheck = "http://cmmcd.com/PokemonGo/"
            else if (radioTrainerClub.isChecked)
                urlCheck = "http://cmmcd.com/PokemonTrainerClub/"

            textView.text = "Checking.."
            getData()
        }
        startPokemonGoEye.setOnClickListener {
            startPokemonGoEye()
        }
    }

    private fun calculatorInterstitialAd() {
        interstitial2.adUnitId = getString(R.string.interstitial2)
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
                            if (radioGoogle.isChecked) {
                                urlCheck = "http://cmmcd.com/PokemonGo/"
                            } else if (radioTrainerClub.isChecked) {
                                urlCheck = "http://cmmcd.com/PokemonTrainerClub/"
                            }
                            getData()
                            if (textView.text.toString() === "Online") {
                                val intent = Intent(this@MainActivity, NotificationListener::class.java)
                                startService(intent)
                                create()
                            }

                        } else if (radioOnlineAndUnstable.isChecked) {
                            if (radioGoogle.isChecked) {
                                urlCheck = "http://cmmcd.com/PokemonGo/"

                            } else if (radioTrainerClub.isChecked) {
                                urlCheck = "http://cmmcd.com/PokemonTrainerClub/"

                            }
                            getData()
                            if (textView.text.toString() === "Online" || textView.text.toString() === "Unstable") {

                                val intent = Intent(this@MainActivity, NotificationListener::class.java)
                                startService(intent)
                                create()
                            }
                        }
                        handler.postDelayed(this, (Integer.parseInt(howManyMinutes.text.toString()) * 60000).toLong())
                    }
                }, (Integer.parseInt(howManyMinutes.text.toString()) * 60000).toLong())

                minimizeApp()
                Toast.makeText(this@MainActivity, R.string.started, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getData() {
        var temp: String? = null
        doAsync {
            var urlConnection: HttpURLConnection? = null
            val result = StringBuilder()
            try {
                val url = URL(urlCheck)
                urlConnection = url.openConnection() as HttpURLConnection
                val code = urlConnection.responseCode

                if (code == 200) {
                    val `in` = BufferedInputStream(urlConnection.inputStream)
                    val bufferedReader = BufferedReader(InputStreamReader(`in`))
                    var line: String

                    while (bufferedReader.readLine() != null) {
                        line = bufferedReader.readLine()
                        result.append(line)
                    }
                    `in`.close()
                }
                temp = result.toString()
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                urlConnection!!.disconnect()
            }
            temp = result.toString()

            kotlin.run {


                val online = "Online!"
                val ofline = "Ofline!"
                val unstable = "Unstable!"
                when {
                    temp!!.contains(online) -> {
                        textView.text = "Online"
                        textView.setTextColor(Color.GREEN)
                    }
                    temp!!.contains(unstable) -> {
                        textView.text = "Unstable"
                        textView.setTextColor(Color.YELLOW)
                    }
                    temp!!.contains(ofline) -> {
                        textView.text = "Ofline"
                        textView.setTextColor(Color.RED)
                    }
                    else -> {
                        textView.text = "Ofline"
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
        nComp.setSmallIcon(R.mipmap.ic_launcher)
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
