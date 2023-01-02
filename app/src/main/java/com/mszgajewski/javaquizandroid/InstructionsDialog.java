package com.mszgajewski.javaquizandroid;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

public class InstructionsDialog extends Dialog {

    private int instructionPoints = 0;

    public InstructionsDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instructions_dialog_layout);

        final AppCompatButton continueBtn = findViewById(R.id.continueBtn);
        final TextView instructionsTextView = findViewById(R.id.instructionsTextView);

        setInstructionPoint(instructionsTextView,"1. Masz 2 minuty na ukończenie testu 2. Za każdą poprawną odpowiedź dostajesz 1 punkt ");

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void setInstructionPoint(TextView instructionTextView, String instructionPoint){

        if (instructionPoints == 0) {
            instructionTextView.setText(instructionPoint);
        } else {
            instructionTextView.setText(instructionTextView.getText() + "/n/n" + instructionPoint);
        }
    }
}