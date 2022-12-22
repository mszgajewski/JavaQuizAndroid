package com.mszgajewski.javaquizandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private final List<QuestionsList> questionsLists = new ArrayList<>();

    private TextView quizTimer;

    private RelativeLayout option1Layout, option2Layout, option3Layout, option4Layout;
    private TextView option1TextView, option2TextView, option3TextView, option4TextView;
    private ImageView option1Icon, option2Icon, option3Icon, option4Icon;

    private TextView questionTextView;

    private TextView totalQuestionTextView;
    private TextView currentQuestion;

    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://quizappadds-default-rtdb.europe-west1.firebasedatabase.app/");

    private CountDownTimer countDownTimer;

    private int currentQuestionPosition = 0;
    private int selectedOption = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quizTimer = findViewById(R.id.quizTimer);

        option1Layout = findViewById(R.id.option1Layout);
        option2Layout = findViewById(R.id.option2Layout);
        option3Layout = findViewById(R.id.option3Layout);
        option4Layout = findViewById(R.id.option4Layout);

        option1TextView = findViewById(R.id.option1TextView);
        option2TextView = findViewById(R.id.option2TextView);
        option3TextView = findViewById(R.id.option3TextView);
        option4TextView = findViewById(R.id.option4TextView);

        option1Icon = findViewById(R.id.option1Icon);
        option2Icon = findViewById(R.id.option2Icon);
        option3Icon = findViewById(R.id.option3Icon);
        option4Icon = findViewById(R.id.option4Icon);

        questionTextView = findViewById(R.id.questionTextView);
        totalQuestionTextView = findViewById(R.id.totalQuestionsTextView);
        currentQuestion = findViewById(R.id.currentQuestionTextView);

        final AppCompatButton nextBtn = findViewById(R.id.nextQuestionBtn);

        InstructionsDialog instructionsDialog = new InstructionsDialog(MainActivity.this);
        instructionsDialog.setCancelable(false);
        instructionsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        instructionsDialog.show();

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

                totalQuestionTextView.setText("/" + questionsLists.size());

                startQuizTimer(getQuizTime);

                selectQuestion(currentQuestionPosition);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Bład pobierania danych", Toast.LENGTH_SHORT).show();
            }
        });

        option1Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOption = 1;
                selectOption(option1Layout, option1Icon);
            }
        });

        option2Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOption = 2;
                selectOption(option2Layout, option2Icon);
            }
        });

        option3Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOption = 3;
                selectOption(option3Layout, option3Icon);
            }
        });

        option4Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOption = 4;
                selectOption(option4Layout, option4Icon);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        countDownTimer = new CountDownTimer(maxTimeInSeconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long getHour = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                long getMinute  = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                long getSeconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);

                String generateTime = String.format(Locale.getDefault(),"%02d:%02d:%02d", getHour,
                        getMinute - TimeUnit.HOURS.toMinutes(getHour),
                        getSeconds - TimeUnit.MINUTES.toSeconds(getMinute));

                quizTimer.setText(generateTime);
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

        questionTextView.setText(questionsLists.get(questionListPosition).getQuestion());
        option1TextView.setText(questionsLists.get(questionListPosition).getOption1());
        option2TextView.setText(questionsLists.get(questionListPosition).getOption2());
        option3TextView.setText(questionsLists.get(questionListPosition).getOption3());
        option4TextView.setText(questionsLists.get(questionListPosition).getOption4());

        currentQuestion.setText("Pytanie" + (questionListPosition + 1));
    }

    private void resetOptions(){

        option1Layout.setBackgroundResource(R.drawable.round_back_white50_10);
        option2Layout.setBackgroundResource(R.drawable.round_back_white50_10);
        option3Layout.setBackgroundResource(R.drawable.round_back_white50_10);
        option4Layout.setBackgroundResource(R.drawable.round_back_white50_10);

        option1Icon.setImageResource(R.drawable.round_back_white50_100);
        option2Icon.setImageResource(R.drawable.round_back_white50_100);
        option3Icon.setImageResource(R.drawable.round_back_white50_100);
        option4Icon.setImageResource(R.drawable.round_back_white50_100);
    }

    private void selectOption(RelativeLayout selectedOptionLayout, ImageView selectedOptionIcon){
        resetOptions();

        selectedOptionIcon.setImageResource(R.drawable.ic_check);
        selectedOptionLayout.setBackgroundResource(R.drawable.round_back_selected_option);
    }
}