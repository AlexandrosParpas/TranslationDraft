package com.example.alex.finalproject;

import java.util.ArrayList;
import java.util.Locale;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


public class MainActivity extends ActionBarActivity {

    private TextView txtSpeechInput;
    private TextView txtSpeechOutput;
    private ImageButton btnSpeakEnglish;
    private ImageButton btnSpeakGreek;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    TranslatedData translatedData;
    String langPair = "";
    private TranslateAPI translateAPI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        txtSpeechOutput = (TextView) findViewById(R.id.txtSpeechOutput);
        btnSpeakEnglish = (ImageButton) findViewById(R.id.btnSpeakEnglish);
        btnSpeakGreek = (ImageButton) findViewById(R.id.btnSpeakGreek);
        translateAPI = new TranslateAPI();

        // hide the action bar
//        getActionBar().hide();

        btnSpeakEnglish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                langPair = "en|el";
                promptSpeechInput("en_US");
            }
        });
        btnSpeakGreek.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                langPair = "el|en";
                promptSpeechInput("el");
            }
        });
    }

        /**
         * Showing google speech input dialog
         * */
        private void promptSpeechInput(String preferredLang) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, preferredLang);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                    getString(R.string.speech_prompt));
            try {
                startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
            } catch (ActivityNotFoundException a) {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.speech_not_supported),
                        Toast.LENGTH_SHORT).show();
            }
        }

        /**
         * Receiving speech input
         * */
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            switch (requestCode) {
                case REQ_CODE_SPEECH_INPUT: {
                    if (resultCode == RESULT_OK && null != data) {

                        ArrayList<String> result = data
                                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                        String speechInput = result.get(0);
                        String request = "=" + speechInput + "&langpair=" + langPair;

                        txtSpeechInput.setText(speechInput);
                        translateAPI.translate(request);
                    }
                    break;
                }

            }
        }

//    public void translate(String toTranslate){
//        String request = "=" + toTranslate + "&langpair=" + langPair;
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://api.mymemory.translated.net/")
//                .addConverterFactory(GsonConverterFactory.create(new Gson()))
//                .build();
//        TranslateAPI service = retrofit.create(TranslateAPI.class);
//        service.getTranslation(request, new Callback<TranslatedData>() {
//            @Override
//            public void onResponse(Response<TranslatedData> response, Retrofit retrofit) {
//                System.out.println("Success");
//                //handle success
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                System.out.println("Failure");
//                //handle failure
//            }
//        });
//    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
