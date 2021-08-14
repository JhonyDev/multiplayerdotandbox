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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.multiplayerdotandbox.R;
import com.example.multiplayerdotandbox.sharedPrefs.ConstantClass;

public class MainActivity extends AppCompatActivity {
    public boolean doubleBackToExitPressedOnce;
    private TextView txtGameType;
    private TextView gameTypeSinglePlayer;
    private TextView gameTypeMultiPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        gameTypeSinglePlayer = findViewById(R.id.gameTypeSinglePlayer);
        gameTypeMultiPlayer = findViewById(R.id.gameTypeMultiPlayer);

        gameTypeMultiPlayer.setOnClickListener(this::onClickGameType);
        gameTypeSinglePlayer.setOnClickListener(this::onClickGameType);

    }

    public void onClickGameType(View view) {
        switch (view.getId()) {
            case R.id.gameTypeSinglePlayer:
                onStartGame(view);
                break;
            case R.id.gameTypeMultiPlayer:
                Toast.makeText(this, "Under Development", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void onStartGame(View view) {
        Intent intent = new Intent(this, gridActivity.class);
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
