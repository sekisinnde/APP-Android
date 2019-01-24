package com.example.oumar.topquiz.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.oumar.topquiz.R;
import com.example.oumar.topquiz.model.Scores;
import com.example.oumar.topquiz.model.User;

import static java.lang.System.out;

public class MainActivity extends AppCompatActivity {

    private TextView mGreetingText;
    private EditText mNameInput;
    private Button mPlayButton;

    // Score Button
    private Button mScoreButton;
    private User mUser;
    public static final int GAME_ACTIVITY_REQUEST_CODE = 42;
    private SharedPreferences mPreferences;
    private Scores mScores;

    public static final String PREF_KEY_SCORE = "PREF_KEY_SCORE";
    public static final String PREF_KEY_SCORETAB = "PREF_KEY_SCORETAB";
    public static final String PREF_KEY_FIRSTNAME = "PREF_KEY_FIRSTNAME";

    public static final String BUNDLE_SCORES = "BUNDLE_SCORES";

    public MainActivity() {
        mScoreButton = (Button) findViewById(R.id.activity_main_scores_btn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("MainActivity::onCreate()");

        mGreetingText = (TextView) findViewById(R.id.activity_main_greeting_txt);
        mNameInput = (EditText) findViewById(R.id.activity_main_name_input);
        mPlayButton = (Button) findViewById(R.id.activity_main_play_btn);

        // Score Button
        mScoreButton = (Button) findViewById(R.id.activity_main_scores_btn);

        // Par défaut, le bouton de jeu est inactif
        mPlayButton.setEnabled(false);

        // On récupère les données de l'apli sauvegardé dans le téléphone
        mPreferences = getPreferences(MODE_PRIVATE);
        //Permet d'effacer toutes les préférences ( utile pour la phase de test)Mélanie
        //mPreferences.edit().clear().commit();
        // Alimente le tableau des scores avec celui récupéré
        Gson gson = new Gson();

        String json = mPreferences.getString(PREF_KEY_SCORETAB,null);
        if (json != null) {
            mScores = gson.fromJson(json,Scores.class);
        } else {
            mScores = new Scores();
            mScoreButton.setEnabled(false);
        }
        out.println("Tableau des scores dans les préférences");
        for (User user : mScores.getScoresTab()){
            out.println(user.getFirstname()+" "+user.getScore());
        }

        // On crée un nouvel objet User qui contiendra
        // les informations concernant le précédent joueur
        // ==> celui sauvegardé dans le téléphone
        mUser = new User();
        // On récupère le nom du joueur
        mUser.setFirstname(mPreferences.getString(PREF_KEY_FIRSTNAME, null));
        // Si un nom de joueur existe
        if (null != mUser.getFirstname()) {
            // On récupère le score du joueur
            mUser.setScore(mPreferences.getInt(PREF_KEY_SCORE, 0));

            // On affiche le nom et le score du joueur
            greetUser();

            // Active le bouton de jeu
            mPlayButton.setEnabled(true);
        }


        mNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPlayButton.setEnabled(s.toString().length() != 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent activityIntent;

                out.println("Click PLAY");

                // User clicked the button
                activityIntent = new Intent(MainActivity.this, GameActivity.class);
                startActivityForResult(activityIntent, GAME_ACTIVITY_REQUEST_CODE);
            }
        });

        // Click sur le bouton "affichage des scores"
        mScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent activityIntent;

                out.println("Tableau des scores avant lancement de l'activité tableua des scores");
                for (User user : mScores.getScoresTab()){
                    out.println(user.getFirstname()+" "+user.getScore());
                }

                out.println("Click SCORES");
                // User clicked the button
                activityIntent = new Intent(MainActivity.this, ScoresActivity.class);

                // Put the scores tab to ScoresActivity
                activityIntent.putExtra("Scores", (Parcelable) mScores );
                startActivity(activityIntent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (GAME_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            // Fetch the score from the Intent
            int score = data.getIntExtra(GameActivity.BUNDLE_EXTRA_SCORE, 0);

            // On crée un nouvel objet User qui contiendra les inforations
            // du nouveau joueur : son nom et son score
            mUser = new User();

            // Save the name of the player
            mUser.setFirstname(mNameInput.getText().toString());
            // Put the player's name in the preferences
            mPreferences.edit().putString(PREF_KEY_FIRSTNAME, mUser.getFirstname()).apply();

            // Save the score of the player in the User Object
            mUser.setScore(score);
            // Put the score of the player in the preferences
            mPreferences.edit().putInt(PREF_KEY_SCORE, score).apply();

            // Add the new player and his score in table scores
            mScores.addScore(mUser);

            // Put the table scores in the preferences
            //------------------------------------------------------------

            // Un joueur et son score sont maintenant disponible, on active le bouton
            // permettant de visualiser le tableau des scores
            mScoreButton.setEnabled(true);

            // On affiche les informations sur le joueur actuel
            greetUser();
        }
    }

    private void greetUser() {

        // On affiche le message récapitulatif
        String fulltext = "Welcome back, " + mUser.getFirstname()
                + "!\nYour last score was " + mUser.getScore()
                + ", will you do better this time?";
        mGreetingText.setText(fulltext);
        mNameInput.setText(mUser.getFirstname());
        mNameInput.setSelection(mUser.getFirstname().length());
        mPlayButton.setEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        out.println("MainActivity::onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();

        out.println("MainActivity::onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();

        out.println("MainActivity::onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();

        out.println("MainActivity::onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        out.println("MainActivity::onDestroy()");
    }
}