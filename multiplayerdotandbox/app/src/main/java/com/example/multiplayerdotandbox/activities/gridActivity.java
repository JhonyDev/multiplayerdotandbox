package com.example.multiplayerdotandbox.activities;//

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.util.Pair;

import com.example.multiplayerdotandbox.R;
import com.example.multiplayerdotandbox.sharedPrefs.ConstantClass;

// Created by FA17-BSE-155 on 14/02/2021.
//
public class gridActivity extends MainActivity {

    private TextView gridSizeFourByFour;
    private TextView gridSizeFiveByFive;
    private TextView gridSizeSixBySix;
    private TextView gridSizeSevenBySeven;
    private Pair<String, String> rowColumnPair = new Pair<>("4", "4");
    private String gameMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridsize);
        gridSizeFourByFour = findViewById(R.id.gridSizeFourByFour);
        gridSizeFiveByFive = findViewById(R.id.gridSizeFiveByFive);
        gridSizeSixBySix = findViewById(R.id.gridSizeSixBySix);
        gridSizeSevenBySeven = findViewById(R.id.gridSizeSevenBySeven);
        gridSizeFourByFour.setOnClickListener(this::onClickGridSize);
        gridSizeFiveByFive.setOnClickListener(this::onClickGridSize);
        gridSizeSixBySix.setOnClickListener(this::onClickGridSize);
        gridSizeSevenBySeven.setOnClickListener(this::onClickGridSize);
        Intent intent=new Intent();
        gameMode = intent.getStringExtra(ConstantClass.GAME_MODE);
    }

    public void onClickGridSize(View view) {

        switch (view.getId()) {
            case R.id.gridSizeFourByFour:
                rowColumnPair = new Pair<String, String>("4", "4");
                onStartGame(view);
                break;
            case R.id.gridSizeFiveByFive:
                rowColumnPair = new Pair<String, String>("5", "5");
                onStartGame(view);
                break;
            case R.id.gridSizeSixBySix:
                rowColumnPair = new Pair<String, String>("6", "6");
                onStartGame(view);
                break;
            case R.id.gridSizeSevenBySeven:
                rowColumnPair = new Pair<String, String>("7", "7");
                onStartGame(view);
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
