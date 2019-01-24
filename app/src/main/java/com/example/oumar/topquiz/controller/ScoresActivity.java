package com.example.oumar.topquiz.controller;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.oumar.topquiz.R;
import com.example.oumar.topquiz.model.Scores;
import com.example.oumar.topquiz.model.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.lang.System.out;
import static java.util.Collections.reverse;
import static java.util.Collections.sort;

public class ScoresActivity extends AppCompatActivity {

    TextView mTitleTextView = (TextView) findViewById(R.id.activity_scores_title_txt);
    private TextView mFirstScoreTextView;
    private TextView mSecondScoreTextView;
    private TextView mThirdScoreTextView;
    private TextView mFourthScoreTextView;
    private TextView mScoreTitleTextView;
    private TextView mNameTitleTextView;
    private TextView mFifthScoreTextView;
    private Button mSortNameButton;
    private Button mSortScoreButton;

    private Scores mScores;

    private List<TextView> textViewList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        mFirstScoreTextView = (TextView) findViewById(R.id.activity_scores_first_txt);
        mSecondScoreTextView = (TextView) findViewById(R.id.activity_scores_second_txt);
        mThirdScoreTextView = (TextView) findViewById(R.id.activity_scores_third_txt);
        mFourthScoreTextView = (TextView) findViewById(R.id.activity_scores_fourth_txt);
        mFifthScoreTextView = (TextView) findViewById(R.id.activity_scores_fifth_txt);
        mSortNameButton = (Button) findViewById(R.id.activity_scores_sort_nameplayers_btn);
        mSortScoreButton = (Button) findViewById(R.id.activity_scores_sort_highscores_btn);
        mScoreTitleTextView  = (TextView) findViewById(R.id.activity_scores_title_score_txt);
        mNameTitleTextView = (TextView) findViewById(R.id.activity_scores_title_name_txt);


        // Stock les lignes de score dans un tableau
        textViewList = new ArrayList<TextView>();
        textViewList.add(mFirstScoreTextView);
        textViewList.add(mSecondScoreTextView);
        textViewList.add(mThirdScoreTextView);
        textViewList.add(mFourthScoreTextView);
        textViewList.add(mFifthScoreTextView);

        // Get back the table of score
        Intent i = getIntent();
        mScores = (Scores) i.getSerializableExtra("Scores");

        // Sort out the scores of the biggest in the smallest
        // Creation of the table of 5 better scores
        ArrayList<User> scoresFive = createFiveTab();
        sort(scoresFive, (Comparator<? super User>) new User());
        reverse(scoresFive);

        // Updates the display of the table of the scores
        if (mScores != null) updateScoresTab(mScores.getScoresTab());

        // Bouton permettant le tri par rapport au score
        mSortScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mScores != null) {

                    // Creation of the table of 5 better scores
                    ArrayList<User> scoresFive;
                    scoresFive = createFiveTab();

                    // Sort out the scores of the biggest in the smallest
                    sort(scoresFive, (Comparator<? super User>) new User());
                    reverse(scoresFive);

                    // Display the scores tab
                    updateScoresTab(scoresFive);
                }
            }
        });

        // Bouton permettant le tri par rapport au nom
        mSortNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mScores != null) {

                    // Creation of the table of 5 better scores
                    ArrayList<User> scoresFive;
                    scoresFive = createFiveTab();

                    // Sort the scores tab by name


                    // Display the scores tab
                    updateScoresTab(scoresFive);
                }
            }
        });
    }

    // Method for the creation of the table of 5 better scores
    private ArrayList<User> createFiveTab() {

        ArrayList<User> scoresFive = new ArrayList<User>();

        // On trie préalablement la liste afin d'avoir le score le plus élevé en début
        // de list et le score le moins élevé en fin de liste
        // Sort out the scores of the biggest in the smallest
        sort(mScores.getScoresTab(), (Comparator<? super User>) new User());
        reverse(mScores.getScoresTab());

        // On conserve uniquement les 5 premiers postes du tableau de score
        for (int i = 0;i<mScores.getScoresTab().size();i++){
            out.println("index = "+i);
            scoresFive.add(mScores.getScoresTab().get(i));
        }
        return scoresFive;
    }

    // Methode qui met à jour l'affichage du tableau de scores
    @SuppressLint("SetTextI18n")
    private void updateScoresTab(List<User> userList) {

        String name;
        int score, index = 0;

        for (TextView textView : textViewList) {
            if (index < userList.size() ) {
                if (userList.get(index) != null) {
                    name = userList.get(index).getFirstname();
                    score = userList.get(index).getScore();
                    textView.setText(   (index + 1)
                            + title(index+1)
                            + "        "
                            + score
                            + "          "
                            + name);
                }
            } else {
                textView.setText(   (index + 1)
                        + title(index+1)
                        + "        "
                        + "-"
                        + "          "
                        + "unknown");
                textView.setTextColor(Color.GRAY);
                textView.setTypeface(Typeface.DEFAULT,Typeface.ITALIC);
            }
            index++;
        }
    }

    // Méthode permettant de retourner le bon titre
    private String title(int rang) {
        String position;
        switch (rang) {
            case 1: position = "ST";
                break;
            case 2: position = "ND";
                break;
            case 3: position = "RD";
                break;
            default: position = "TH";
                break;
        }
        return position;
    }
}
