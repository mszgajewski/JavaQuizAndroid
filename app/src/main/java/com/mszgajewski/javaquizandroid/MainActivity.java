package com.mszgajewski.javaquizandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mszgajewski.javaquizandroid.databinding.ActivityMainBinding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private final List<QuestionsList> questionsLists = new ArrayList<>();
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://quizappadds-default-rtdb.europe-west1.firebasedatabase.app/");
    private CountDownTimer countDownTimer;
    private int currentQuestionPosition = 0;
    private int selectedOption = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final int getQuizTime = Integer.parseInt(snapshot.child("time").getValue(String.class));

                for (DataSnapshot questions : snapshot.child("questions").getChildren()){

                    String getQuestion = questions.child("question").getValue(String.class);
                    String getOption1 = questions.child("option1").getValue(String.class);
                    String getOption2 = questions.child("option2").getValue(String.class);
                    String getOption3 = questions.child("option3").getValue(String.class);
                    String getOption4 = questions.child("option4").getValue(String.class);
                     int getAnswer = Integer.parseInt(questions.child("answer").getValue(String.class));

                    QuestionsList questionsList = new QuestionsList(getQuestion, getOption1, getOption2, getOption3, getOption4, getAnswer);
                    questionsLists.add(questionsList);
                }

                binding.totalQuestionsTextView.setText("/" + questionsLists.size());

                startQuizTimer(getQuizTime);

                selectQuestion(currentQuestionPosition);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Bład pobierania danych", Toast.LENGTH_SHORT).show();
            }
        });

        binding.option1Layout.setOnClickListener(v -> {
            selectedOption = 1;
            selectOption(binding.option1Layout, binding.option1Icon);
        });

        binding.option2Layout.setOnClickListener(v -> {
            selectedOption = 2;
            selectOption(binding.option2Layout, binding.option2Icon);
        });

        binding.option3Layout.setOnClickListener(v -> {
            selectedOption = 3;
            selectOption(binding.option3Layout, binding.option3Icon);
        });

        binding.option4Layout.setOnClickListener(v -> {
            selectedOption = 4;
            selectOption(binding.option4Layout, binding.option4Icon);
        });

        binding.nextQuestionBtn.setOnClickListener(v -> {
            if (selectedOption != 0){
                questionsLists.get(currentQuestionPosition).setUserSelectedAnswer(selectedOption);
                selectedOption = 0;
                currentQuestionPosition++;

                if (currentQuestionPosition < questionsLists.size()){
                    selectQuestion(currentQuestionPosition);
                } else {
                    countDownTimer.cancel();
                    finishQuiz();
                }
            } else {
                Toast.makeText(MainActivity.this, "Proszę wybrać odpowiedź", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void finishQuiz(){
        Intent intent = new Intent(MainActivity.this,QuizResult.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("questions", (Serializable) questionsLists);

        intent.putExtras(bundle);
        startActivity(intent);

        finish();
    }

    private void startQuizTimer(int maxTimeInSeconds) {
        countDownTimer = new CountDownTimer(maxTimeInSeconds * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long getHour = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                long getMinute  = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                long getSeconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);

                String generateTime = String.format(Locale.getDefault(),"%02d:%02d:%02d",
                        getHour,
                        getMinute - TimeUnit.HOURS.toMinutes(getHour),
                        getSeconds - TimeUnit.MINUTES.toSeconds(getMinute));

                binding.quizTimer.setText(generateTime);
            }

            @Override
            public void onFinish() {

                finishQuiz();
            }
        };

        countDownTimer.start();
    }

    private void selectQuestion(int questionListPosition){

        resetOptions();

        binding.questionTextView.setText(questionsLists.get(questionListPosition).getQuestion());
        binding.option1TextView.setText(questionsLists.get(questionListPosition).getOption1());
        binding.option2TextView.setText(questionsLists.get(questionListPosition).getOption2());
        binding.option3TextView.setText(questionsLists.get(questionListPosition).getOption3());
        binding.option4TextView.setText(questionsLists.get(questionListPosition).getOption4());

        binding.currentQuestionTextView.setText("Pytanie" + (questionListPosition + 1));
    }

    private void resetOptions(){

        binding.option1Layout.setBackgroundResource(R.drawable.round_back_white50_10);
        binding.option2Layout.setBackgroundResource(R.drawable.round_back_white50_10);
        binding.option3Layout.setBackgroundResource(R.drawable.round_back_white50_10);
        binding.option4Layout.setBackgroundResource(R.drawable.round_back_white50_10);

        binding.option1Icon.setImageResource(R.drawable.round_back_white50_100);
        binding.option2Icon.setImageResource(R.drawable.round_back_white50_100);
        binding.option3Icon.setImageResource(R.drawable.round_back_white50_100);
        binding.option4Icon.setImageResource(R.drawable.round_back_white50_100);
    }

    private void selectOption(RelativeLayout selectedOptionLayout, ImageView selectedOptionIcon){
        resetOptions();

        selectedOptionIcon.setImageResource(R.drawable.ic_check);
        selectedOptionLayout.setBackgroundResource(R.drawable.round_back_selected_option);
    }
}