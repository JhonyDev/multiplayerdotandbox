package com.example.multiplayerdotandbox.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;

import com.example.multiplayerdotandbox.R;
import com.example.multiplayerdotandbox.sharedPrefs.ConstantClass;

import org.jetbrains.annotations.NotNull;

public class ResultFragment extends DialogFragment{

    public static final int FRAGMENT_ID = 1124574;
    private OnFragmentInteractionListener mListener;
    private int player1Score;
    private int player2Score;
    private String player1Name = "";
    private String player2Name = "";
    private boolean isTurnMe;
    private String playerNameMeFromPreference = "";

    public static ResultFragment newInstance(Bundle args) {
        ResultFragment f = new ResultFragment();

        if (args != null)
            f.setArguments(args);

        return f;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog();
    }

    @SuppressLint("NonConstantResourceId")
    public void onViewClicked(View view) {
                mListener.onMenuRequested();
                dismiss();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        player1Score = getArguments().getInt(GameFragment.ARG_PLAYER1_SCORE);
        player2Score = getArguments().getInt(GameFragment.ARG_PLAYER2_SCORE);
        player1Name = getArguments().getString(GameFragment.ARG_PLAYER1_NAME);
        player2Name = getArguments().getString(GameFragment.ARG_PLAYER2_NAME);
        isTurnMe = getArguments().getBoolean(ConstantClass.TURN_ME);
        playerNameMeFromPreference = getArguments().getString(ConstantClass.PREFERENCES.NAME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppCompatTextView tvWinLoss = view.findViewById(R.id.tv_win_loss);
        AppCompatTextView tvHome = view.findViewById(R.id.tv_home);
        tvHome.setOnClickListener(this::onViewClicked);

        if (player1Score > player2Score) {
            tvWinLoss.setText(String.format("%s Win", player1Name));
        } else if (player1Score < player2Score) {
            tvWinLoss.setText(String.format("%s Win", player2Name));
        } else {
            tvWinLoss.setText("Match Tie");
        }
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onMenuRequested();
        void onReplayRequested(Bundle arguments);
    }

}
