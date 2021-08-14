package com.example.multiplayerdotandbox.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.util.Pair;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.example.multiplayerdotandbox.R;
import com.example.multiplayerdotandbox.sharedPrefs.ConstantClass;

public class MainActivity extends AppCompatActivity {
    public boolean doubleBackToExitPressedOnce;

    private AppCompatCheckedTextView gameTypeMultiPlayer;
    private AppCompatCheckedTextView gridSizeFourByFour;
    private AppCompatCheckedTextView gridSizeFiveByFive;
    private AppCompatCheckedTextView gridSizeSixBySix;
    private AppCompatCheckedTextView gridSizeSevenBySeven;

    private Pair<String, String> rowColumnPair = new Pair<>("4", "4");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatCheckedTextView gameTypeSinglePlayer = findViewById(R.id.gameTypeSinglePlayer);
        gameTypeMultiPlayer = findViewById(R.id.gameTypeMultiPlayer);
        gridSizeFourByFour = findViewById(R.id.gridSizeFourByFour);
        gridSizeFiveByFive = findViewById(R.id.gridSizeFiveByFive);
        gridSizeSixBySix = findViewById(R.id.gridSizeSixBySix);
        gridSizeSevenBySeven = findViewById(R.id.gridSizeSevenBySeven);

        AppCompatTextView btnStart = findViewById(R.id.btnStart);

        gridSizeFourByFour.setOnClickListener(this::onClickGridSize);
        gridSizeFiveByFive.setOnClickListener(this::onClickGridSize);
        gridSizeSixBySix.setOnClickListener(this::onClickGridSize);
        gridSizeSevenBySeven.setOnClickListener(this::onClickGridSize);
        gameTypeMultiPlayer.setOnClickListener(this::onClickGameType);

        gameTypeSinglePlayer.setOnClickListener(this::onClickGameType);
        btnStart.setOnClickListener(this::onStartGame);

    }

    public void onClickGameType(View view) {
        ((AppCompatCheckedTextView) view).setChecked(true);
        switch (view.getId()) {
            case R.id.gameTypeSinglePlayer:
                gameTypeMultiPlayer.setChecked(false);
                break;
            case R.id.gameTypeMultiPlayer:
                gameTypeMultiPlayer.setChecked(false);
                Toast.makeText(this, "Under Development", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void onClickGridSize(View view) {
        ((AppCompatCheckedTextView) view).setChecked(true);
        switch (view.getId()) {
            case R.id.gridSizeFourByFour:
                rowColumnPair = new Pair<String, String>("4", "4");
                gridSizeFiveByFive.setChecked(false);
                gridSizeSixBySix.setChecked(false);
                gridSizeSevenBySeven.setChecked(false);
                break;
            case R.id.gridSizeFiveByFive:
                rowColumnPair = new Pair<String, String>("5", "5");
                gridSizeFourByFour.setChecked(false);
                gridSizeSixBySix.setChecked(false);
                gridSizeSevenBySeven.setChecked(false);
                break;
            case R.id.gridSizeSixBySix:
                rowColumnPair = new Pair<String, String>("6", "6");
                gridSizeFourByFour.setChecked(false);
                gridSizeFiveByFive.setChecked(false);
                gridSizeSevenBySeven.setChecked(false);
                break;
            case R.id.gridSizeSevenBySeven:
                rowColumnPair = new Pair<String, String>("7", "7");
                gridSizeFourByFour.setChecked(false);
                gridSizeFiveByFive.setChecked(false);
                gridSizeSixBySix.setChecked(false);
                break;
        }
    }

    public void onStartGame(View view) {
        Intent intent = new Intent(this, DifficultySelectionActivity.class);
        String row = rowColumnPair.first;
        String column = rowColumnPair.second;
        intent.putExtra(ConstantClass.SELECTED_ROW, row);
        intent.putExtra(ConstantClass.SELECTED_COLUMN, column);
        intent.putExtra(ConstantClass.GAME_MODE, ConstantClass.ROBOT);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

    }

}
