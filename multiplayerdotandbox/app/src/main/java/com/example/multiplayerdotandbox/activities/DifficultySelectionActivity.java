package com.example.multiplayerdotandbox.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;

import com.example.multiplayerdotandbox.R;
import com.example.multiplayerdotandbox.sharedPrefs.ConstantClass;

public class DifficultySelectionActivity extends MainActivity {
    private AppCompatTextView difficultyEasy;
    private AppCompatTextView difficultyMedium;
    private AppCompatTextView difficultyHard;

    private String selectedRow;
    private String selectedColumn;
    private String gameMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty_selection);
        init();
    }

    private void init() {

        difficultyEasy = findViewById(R.id.difficultyEasy);
        difficultyMedium = findViewById(R.id.difficultyMedium);
        difficultyHard = findViewById(R.id.difficultyHard);
        difficultyEasy.setOnClickListener(this::onClickDifficulty);
        difficultyMedium.setOnClickListener(this::onClickDifficulty);
        difficultyHard.setOnClickListener(this::onClickDifficulty);
        Intent intent = getIntent();
        selectedRow = intent.getStringExtra(ConstantClass.SELECTED_ROW);
        selectedColumn = intent.getStringExtra(ConstantClass.SELECTED_COLUMN);
        gameMode = intent.getStringExtra(ConstantClass.GAME_MODE);

    }

    public void onClickDifficulty(View view) {
        switch (view.getId()) {
            case R.id.difficultyEasy:
                startGame(view);
                break;
            case R.id.difficultyMedium:
            case R.id.difficultyHard:
                Toast.makeText(this, "Under Development", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void startGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(ConstantClass.SELECTED_ROW, selectedRow);
        intent.putExtra(ConstantClass.SELECTED_COLUMN, selectedColumn);
        intent.putExtra(ConstantClass.GAME_MODE, ConstantClass.ROBOT);
        startActivity(intent);
    }

}