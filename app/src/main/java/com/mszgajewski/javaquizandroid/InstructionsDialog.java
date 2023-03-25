package com.mszgajewski.javaquizandroid;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.mszgajewski.javaquizandroid.databinding.InstructionsDialogLayoutBinding;

public class InstructionsDialog extends Dialog {

    InstructionsDialogLayoutBinding binding;

    public InstructionsDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = InstructionsDialogLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setInstructionPoint(binding.instructionsTextView);

        binding.continueBtn.setOnClickListener(v -> dismiss());
    }

    private void setInstructionPoint(TextView instructionTextView){

        instructionTextView.setText("1. Masz 2 minuty na ukończenie testu 2. Za każdą poprawną odpowiedź dostajesz 1 punkt ");
    }
}