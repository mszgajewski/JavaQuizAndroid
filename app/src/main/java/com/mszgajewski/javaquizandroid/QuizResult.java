package com.mszgajewski.javaquizandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class QuizResult extends AppCompatActivity {

    private  List<QuestionsList> questionsLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        final TextView scoreTextView = findViewById(R.id.resultTextView);
        final TextView totalScoreTextView = findViewById(R.id.totalScoreTextView);
        final TextView correctTextView = findViewById(R.id.correctAnswerTextview);
        final TextView incorrectTextView = findViewById(R.id.incorrectAnswerTextview);
        final AppCompatButton shareBtn = findViewById(R.id.shareResultBtn);
        final AppCompatButton reTakeButton = findViewById(R.id.reTakeQuizBtn);

        questionsLists = (List<QuestionsList>) getIntent().getSerializableExtra("questions");

        totalScoreTextView.setText("/"+questionsLists.size());
        scoreTextView.setText(getCorrectAnswers() + "");
        correctTextView.setText(getCorrectAnswers() + "");
        incorrectTextView.setText(String.valueOf(questionsLists.size() - getCorrectAnswers()));

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Mój wynik = " + scoreTextView.getText());

                Intent shareIntent = Intent.createChooser(sendIntent,"Share Via");
                startActivity(shareIntent);
            }
        });

        reTakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuizResult.this, MainActivity.class));
                finish();
            }
        });

    }

    private int getCorrectAnswers() {
        int correctAnswers = 0;

        for (int i=0; i<questionsLists.size(); i++){
            int getUserSelectedOption = questionsLists.get(i).getUserSelectedAnswer();
            int getQuestionAnswer = questionsLists.get(i).getAnswer();

            if (getQuestionAnswer == getUserSelectedOption){
                correctAnswers++;
            }
        }

        return correctAnswers;
    }
}