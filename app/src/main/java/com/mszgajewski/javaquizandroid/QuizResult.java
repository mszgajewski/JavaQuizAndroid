package com.mszgajewski.javaquizandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.mszgajewski.javaquizandroid.databinding.ActivityQuizResultBinding;
import java.util.ArrayList;
import java.util.List;

public class QuizResult extends AppCompatActivity {

    ActivityQuizResultBinding binding;
    private  List<QuestionsList> questionsLists = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        questionsLists = (List<QuestionsList>) getIntent().getSerializableExtra("questions");
        binding.totalScoreTextView.setText("/"+questionsLists.size());
        binding.resultTextView.setText(getCorrectAnswers() + "");
        binding.correctAnswerTextview.setText(getCorrectAnswers() + "");
        binding.incorrectAnswerTextview.setText(String.valueOf(questionsLists.size() - getCorrectAnswers()));

        binding.shareResultBtn.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Mój wynik = " + binding.resultTextView.getText());

            Intent shareIntent = Intent.createChooser(sendIntent,"Udostępnij");
            startActivity(shareIntent);
        });

        binding.reTakeQuizBtn.setOnClickListener(v -> {
            startActivity(new Intent(QuizResult.this, MainActivity.class));
            finish();
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