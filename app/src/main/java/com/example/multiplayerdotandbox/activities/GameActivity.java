package com.example.multiplayerdotandbox.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.multiplayerdotandbox.R;
import com.example.multiplayerdotandbox.fragments.GameFragment;
import com.example.multiplayerdotandbox.fragments.ResultFragment;
import com.example.multiplayerdotandbox.sharedPrefs.ConstantClass;
import com.example.multiplayerdotandbox.sharedPrefs.OnFragmentInteractionListener;

public class GameActivity extends MainActivity implements FragmentManager.OnBackStackChangedListener,
        OnFragmentInteractionListener, ResultFragment.OnFragmentInteractionListener{

    private AppCompatTextView tvTurnText;

    boolean mIsStateAlreadySaved = false;
    boolean mPendingShowDialog = false;
    private GameFragment gameFragment;
    private String gameMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        init(savedInstanceState);
    }

    @Override
    public void onResumeFragments() {
        super.onResumeFragments();
        mIsStateAlreadySaved = false;
        if (mPendingShowDialog) {
            mPendingShowDialog = false;
            Bundle args = gameFragment.loadGameResultFragment();
            onWinFragmentLoad(ResultFragment.FRAGMENT_ID, args);
        }
    }

    private void init(Bundle savedInstanceState) {

        tvTurnText = findViewById(R.id.tv_turn_text);

        Intent intent = getIntent();
        String selectedRow = intent.getStringExtra(ConstantClass.SELECTED_ROW);
        String selectedColumn = intent.getStringExtra(ConstantClass.SELECTED_COLUMN);
        gameMode = intent.getStringExtra(ConstantClass.GAME_MODE);

        int rows = Integer.parseInt(selectedRow);
        int columns = Integer.parseInt(selectedColumn);

        getSupportFragmentManager().addOnBackStackChangedListener(this);

        if (savedInstanceState != null) {
            gameFragment = (GameFragment) getSupportFragmentManager().getFragment(savedInstanceState, GameFragment.class.getName());
        } else {
            Bundle args = new Bundle();
            args.putInt(ConstantClass.SELECTED_ROW, rows);
            args.putInt(ConstantClass.SELECTED_COLUMN, columns);
            args.putString(ConstantClass.GAME_MODE, gameMode);
            gameFragment = new GameFragment();
            gameFragment.setArguments(args);
        }
        loadGameFragment();
    }

    private void loadGameFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, gameFragment);
        transaction.commit();
    }

    @Override
    public void onBackStackChanged() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int backStackCount = fragmentManager.getBackStackEntryCount();

        if (backStackCount < 1) {
            finish();
            startGameActivity();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, GameFragment.class.getName(), gameFragment);
    }

    @Override
    public void onWinFragmentLoad(int fragmentId, Bundle args) {
        if (mIsStateAlreadySaved) {
            mPendingShowDialog = true;
        } else {

            if (fragmentId == ResultFragment.FRAGMENT_ID) {
                ResultFragment dialog = ResultFragment.newInstance(args);
                dialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(), "dialog_fragment");
                dialog.setCancelable(false);
            }
        }
    }

    @Override
    public void onChooseTurnFragmentLoad() {
        gameFragment.onPlayer1Clicked();
    }

    @Override
    public void onReplayRequested(Bundle arguments) {
        gameFragment = new GameFragment();
        gameFragment.setArguments(arguments);

        loadGameFragment();
    }

    @Override
    public void onMenuRequested() {
        finish();
        startGameActivity();
    }

    private void startGameActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    public void showExitDialog() {
        AlertDialog.Builder exitAlertBuilder = new AlertDialog.Builder(GameActivity.this);
        exitAlertBuilder.setMessage(getString(R.string.game_exit));
        exitAlertBuilder.setCancelable(false);

        exitAlertBuilder.setPositiveButton(
                getString(R.string.yes),
                (dialog, id) -> {
                    finish();
                    dialog.cancel();
                    startGameActivity();
                });

        exitAlertBuilder.setNegativeButton(
                getString(R.string.no),
                (dialog, id) -> dialog.cancel());

        AlertDialog exitAlert = exitAlertBuilder.create();
        exitAlert.show();
    }

    public TextView getTvTurnText() {
        return tvTurnText;
    }
}