package com.ozhan.mustafa.pokemongoeye;

import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView tv, cal;
    EditText howManyMinutes;
    Button button, startPokemonGoEye;
    RadioButton googleRadio, trainerClubRadio, onlyOnlineRadio, onlineAndUnstableRadio;
    String urlCheck;

    InterstitialAd interstitial1 = new InterstitialAd(this);
    InterstitialAd interstitial2 = new InterstitialAd(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.button);
        googleRadio = (RadioButton) findViewById(R.id.radioGoogle);
        trainerClubRadio = (RadioButton) findViewById(R.id.radioTrainerClub);
        howManyMinutes = (EditText) findViewById(R.id.howManyMinutes);
        startPokemonGoEye = (Button) findViewById(R.id.startPokemonGoEye);
        onlyOnlineRadio = (RadioButton) findViewById(R.id.radioOnlyOnline);
        onlineAndUnstableRadio = (RadioButton) findViewById(R.id.radioOnlineAndUnstable);
        cal = (TextView) findViewById(R.id.calculator);

        howManyMinutes.setText("5");


        AdView mAdView1 = (AdView) findViewById(R.id.adView_1);
        AdRequest adRequest1 = new AdRequest.Builder().build();

        mAdView1.loadAd(adRequest1);

        AdView mAdView2 = (AdView) findViewById(R.id.adView_3);
        AdRequest adRequest2 = new AdRequest.Builder().build();

        mAdView2.loadAd(adRequest2);


        interstitial1.setAdUnitId(String.valueOf(R.string.interstitial1));
        interstitial2.setAdUnitId(String.valueOf(R.string.interstitial2));
/*
        // Load an ad into the AdMob banner view.
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        adView.loadAd(adRequest);

        // Toasts the test ad message on the screen. Remove this after defining your own ad unit ID.
        //Toast.makeText(this, TOAST_TEXT, Toast.LENGTH_LONG).show();

*/

        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdRequest adRequest1 = new AdRequest.Builder().build();

                interstitial2.loadAd(adRequest1);

                interstitial2.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        if (interstitial2.isLoaded()) {
                            interstitial2.show();
                        }


                    }


                });

                Intent intent = new Intent(MainActivity.this, Browser.class);
                startActivity(intent);

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (googleRadio.isChecked()) {
                    urlCheck = "http://cmmcd.com/PokemonGo/";
                } else if (trainerClubRadio.isChecked()) {
                    urlCheck = "http://cmmcd.com/PokemonTrainerClub/";
                }/*else{
                    Toast.makeText(MainActivity.this,"Please check one of account type !",Toast.LENGTH_SHORT).show();
                    urlCheck=null;
                }*/
                tv.setText("Checking..");
                new GetData().execute();
            }
        });
        startPokemonGoEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AdRequest adRequest1 = new AdRequest.Builder().build();

                interstitial1.loadAd(adRequest1);

                interstitial1.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        if (interstitial1.isLoaded()) {
                            interstitial1.show();
                        }
                    }


                    @Override
                    public void onAdClosed() {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {

                                if (onlyOnlineRadio.isChecked()) {
                                    if (googleRadio.isChecked()) {
                                        urlCheck = "http://cmmcd.com/PokemonGo/";
                                    } else if (trainerClubRadio.isChecked()) {
                                        urlCheck = "http://cmmcd.com/PokemonTrainerClub/";
                                    }
                                    new GetData().execute();
                                    if (tv.getText().toString() == "Online") {
                                        Intent intent = new Intent(MainActivity.this, NotificationListener.class);
                                        startService(intent);
                                        create();
                                    }

                                } else if (onlineAndUnstableRadio.isChecked()) {
                                    if (googleRadio.isChecked()) {
                                        urlCheck = "http://cmmcd.com/PokemonGo/";

                                    } else if (trainerClubRadio.isChecked()) {
                                        urlCheck = "http://cmmcd.com/PokemonTrainerClub/";

                                    }
                                    new GetData().execute();
                                    if (tv.getText().toString() == "Online" || tv.getText().toString() == "Unstable") {

                                        Intent intent = new Intent(MainActivity.this, NotificationListener.class);
                                        startService(intent);
                                        create();
                                    }
                                }
                                handler.postDelayed(this, Integer.parseInt(howManyMinutes.getText().toString()) * 60000);
                            }
                        }, Integer.parseInt(howManyMinutes.getText().toString()) * 60000);

                        minimizeApp();
                        Toast.makeText(MainActivity.this, R.string.started, Toast.LENGTH_SHORT).show();
                    }
                });


            }//bunu sonradan ekledim
        });
    }

    public void minimizeApp() {


        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    public void create() {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder ncomp = new NotificationCompat.Builder(this);
        ncomp.setSound(uri);
        ncomp.setContentTitle("Eye of Pokemon Go");
        ncomp.setContentText("Now you can play Pokemon Go!");
        ncomp.setSmallIcon(R.mipmap.ic_launcher);
        ncomp.setAutoCancel(true);
        nManager.notify((int) System.currentTimeMillis(), ncomp.build());

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class GetData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            String result = "";
            try {
                URL url = new URL(urlCheck);
                urlConnection = (HttpURLConnection) url.openConnection();
                int code = urlConnection.getResponseCode();

                if (code == 200) {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    if (in != null) {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                        String line = "";

                        while ((line = bufferedReader.readLine()) != null)
                            result += line;
                    }
                    in.close();
                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String online = "Online!";
            String ofline = "Ofline!";
            String unstable = "Unstable!";
            if (result.contains(online)) {
                tv.setText("Online");
                tv.setTextColor(Color.GREEN);
            } else if (result.contains(unstable)) {
                tv.setText("Unstable");
                tv.setTextColor(Color.YELLOW);
            } else if (result.contains(ofline)) {
                tv.setText("Ofline");
                tv.setTextColor(Color.RED);
            } else {
                tv.setText("Ofline");
                tv.setTextColor(Color.RED);
            }
        }
    }
}
