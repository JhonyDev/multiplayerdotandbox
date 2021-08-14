package com.example.multiplayerdotandbox.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.multiplayerdotandbox.R;
import com.example.multiplayerdotandbox.activities.GameActivity;
import com.example.multiplayerdotandbox.game.GameModelView.BoardView;
import com.example.multiplayerdotandbox.game.GameModelView.Lines;
import com.example.multiplayerdotandbox.game.controllerss.Game;
import com.example.multiplayerdotandbox.game.controllerss.Player;
import com.example.multiplayerdotandbox.game.controllerss.PlayerRobot;
import com.example.multiplayerdotandbox.interfaces.GameEventListener;
import com.example.multiplayerdotandbox.sharedPrefs.ConstantClass;
import com.example.multiplayerdotandbox.sharedPrefs.CountDownTimerWithPause;
import com.example.multiplayerdotandbox.sharedPrefs.OnFragmentInteractionListener;

import java.util.Locale;
import java.util.Objects;

public class GameFragment extends Fragment implements GameEventListener {
    public static final String ARG_PLAYER1_SCORE = "args.score.player1";
    public static final String ARG_PLAYER2_SCORE = "args.score.player2";
    public static final String ARG_GAME_MODE = "args.game.mode";
    public static final String ARG_PLAYER1_NAME = "args.name.payer1";
    public static final String ARG_PLAYER2_NAME = "args.name.payer2";
    private static final long BOT_DELAY_TIME = 1500;

    private TextView tvPlayer1Score;
    private TextView tvPlayer2Score;
    private BoardView boardView;
    private TextView tvPlayer1Name;
    private TextView tvPlayer2Name;
    private Game game;
    private PlayerRobot bot;
    private Game.Mode mode;
    private OnFragmentInteractionListener mListener;
    private String player1Name = "";
    private String player2Name = "";
    private boolean isTurnMe;
    private String playerYou;
    private String playerFriend;
    private String gameMode;
    private String playerNameMeFromPreference;
    private long botDelayTime = 0L;
    private CountDownTimerWithPause progressTimer;
    private LinearLayout llYou;
    private LinearLayout llBot;

    public GameFragment() {

    }

    private static long getBotDelayTime(int rows, int columns) {
        if (rows == 4 && columns == 4 || rows == 5 && columns == 5 || rows == 6 && columns == 6) {
            return BOT_DELAY_TIME;
        }
        return 0;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int rows = getResources().getInteger(R.integer.default_rows);
        int columns = getResources().getInteger(R.integer.default_columns);

        Bundle args = getArguments();
        if (args != null) {
            rows = args.getInt(ConstantClass.SELECTED_ROW, rows);
            columns = args.getInt(ConstantClass.SELECTED_COLUMN, columns);
            playerYou = args.getString(ConstantClass.PLAYER1_NAME, ConstantClass.PLAYER_YOU);
            playerFriend = args.getString(ConstantClass.PLAYER2_NAME, ConstantClass.PLAYER_ROBOT);
            gameMode = args.getString(ConstantClass.GAME_MODE);
        }

        game = new Game(rows, columns);
        bot = new PlayerRobot(game);
        botDelayTime = getBotDelayTime(rows, columns);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        llBot = view.findViewById(R.id.ll_bot);
        llYou = view.findViewById(R.id.ll_you);
        tvPlayer1Name = view.findViewById(R.id.tv_player_1_name);
        tvPlayer1Score = view.findViewById(R.id.tv_player_1_score);
        tvPlayer2Name = view.findViewById(R.id.tv_player_2_name);
        tvPlayer2Score = view.findViewById(R.id.tv_player_2_score);
        boardView = view.findViewById(R.id.boardView);
        game.setGameEventListener(this);
        boardView.setGame(game);

        if (ConstantClass.ROBOT.equals(gameMode)) {
            if (playerNameMeFromPreference != null && !TextUtils.isEmpty(playerNameMeFromPreference)) {
                tvPlayer1Name.setText(playerNameMeFromPreference);
            } else {
                tvPlayer1Name.setText(playerYou);
            }

            mode = Game.Mode.CPU;
            tvPlayer2Name.setText(getString(R.string.Android));
        }

        if (mListener != null) {
            mListener.onChooseTurnFragmentLoad();
        }
        boardView.enableInteraction();
        llYou.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.box_filled));
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (progressTimer != null && progressTimer.isPaused()) {
            progressTimer.resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (progressTimer != null && progressTimer.isRunning()) {
            progressTimer.pause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void onPlayer1Clicked() {
        if (mode == Game.Mode.CPU) {
            player1Name = getString(R.string.you);
            isTurnMe = true;
            player2Name = getString(R.string.Android);

            if (playerNameMeFromPreference != null && !TextUtils.isEmpty(playerNameMeFromPreference)) {
                tvPlayer1Name.setText(playerNameMeFromPreference);
            } else {
                tvPlayer1Name.setText(player1Name);
            }

            boardView.setTurnMe(isTurnMe);
            setTurnText(Player.PLAYER1);
        }
    }

    public Bundle loadGameResultFragment() {
        Bundle args = getArguments();

        if (args == null)
            args = new Bundle();

        int player1Score = game.getBoard().getScore(Player.PLAYER1);
        int player2Score = game.getBoard().getScore(Player.PLAYER2);

        args.putInt(ARG_PLAYER1_SCORE, player1Score);
        args.putInt(ARG_PLAYER2_SCORE, player2Score);
        args.putSerializable(ARG_GAME_MODE, mode);
        args.putString(ARG_PLAYER1_NAME, player1Name);
        args.putString(ARG_PLAYER2_NAME, player2Name);
        args.putString(ConstantClass.GAME_MODE, gameMode);
        args.putBoolean(ConstantClass.TURN_ME, isTurnMe);

        return args;
    }

    private void takeTurnFromBot() {
        boardView.disableInteraction();

        progressTimer = new CountDownTimerWithPause(botDelayTime, 4, true) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (game.getGameEventListener() != null) {
                    game.getGameEventListener().onBotCompute(bot.getNextMove());
                }
            }
        };

        progressTimer.create();
    }

    public void setTurnText(int player) {
        if (ConstantClass.ROBOT.equals(gameMode)) {
            setTextWhenRobot(player);
        }
    }

    public void setTextWhenRobot(int player) {
        String playerName;
        Spannable turnString;
        if (isTurnMe) {
            if (player == Player.PLAYER1) {
                playerName = getString(R.string.player1TurnName);
            } else {
                playerName = getString(R.string.player2TurnName);
            }
        } else {
            if (player == Player.PLAYER1) {
                playerName = getString(R.string.player2TurnName);
            } else {
                playerName = getString(R.string.player1TurnName);
            }
        }
        turnString = new SpannableString(String.format(Locale.getDefault(), getString(R.string.turn_text), playerName));
        if (getActivity() instanceof GameActivity) {
            GameActivity gameActivity = (GameActivity) getActivity();
            gameActivity.getTvTurnText().setText(turnString);
        }
    }

    @Override
    public void onAttach(Context context) {
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onScoreUpdate(int player, int score) {
        if (player == Player.PLAYER1) {
            tvPlayer1Score.setText(String.format(Locale.getDefault(), "%d", score));
        } else {
            tvPlayer2Score.setText(String.format(Locale.getDefault(), "%d", score));
        }
    }

    @Override
    public void onTurnChange(Game.State state, int player) {
        game.setGameState(state);
        if (player == Player.PLAYER1) {
            llYou.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.box_filled));
            llBot.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.box_unfilled));
        } else {
            llBot.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.box_filled));
            llYou.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.box_unfilled));
        }
        setTurnText(player);
    }

    @Override
    public void onGameEnd(int winner) {
        game.setGameState(Game.State.END);
        Bundle args = loadGameResultFragment();

        if (mListener != null) {
            mListener.onWinFragmentLoad(ResultFragment.FRAGMENT_ID, args);
        }
    }

    @Override
    public void onPlayerMove(Lines edge) {
        int boxesCompleted = game.makeAMove(edge.getDotStart(), edge.getDotEnd());
        boardView.invalidate();

        if (boxesCompleted == 0) {
            setTurnText(Player.PLAYER2);
            if (mode == Game.Mode.CPU) {
                takeTurnFromBot();
            } else {
                boardView.invalidate();
                boardView.enableInteraction();
            }
        }
    }

    @Override
    public void onBotCompute(Lines edge) {
        int boxesCompleted = game.makeAMove(edge.getDotStart(), edge.getDotEnd());
        boardView.invalidate();

        if (boxesCompleted > 0 && game.getState() != Game.State.END) {
            takeTurnFromBot();
        } else {
            boardView.invalidate();
            boardView.enableInteraction();
            setTurnText(Player.PLAYER1);
        }
    }


}