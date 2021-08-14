package com.example.multiplayerdotandbox.game.controllerss;

import com.example.multiplayerdotandbox.game.GameModelView.Grid;
import com.example.multiplayerdotandbox.interfaces.GameEventListener;

public class PlayerRobot extends Player {

    public PlayerRobot(Game game) {
        super(game);
    }

    public int takeTurnPlayer2(Grid board, int maxScore, int dotStart, int dotEnd, GameEventListener eventListener) {
        int boxesCompleted = board.setLineForDots(dotStart, dotEnd, Player.PLAYER2);

        if (boxesCompleted > 0) {
            int player1Score = board.getScore(PLAYER1);
            int player2Score = board.getScore(PLAYER2);
            eventListener.onScoreUpdate(PLAYER2, player2Score);

            if (player1Score + player2Score == maxScore) {
                if (player1Score == player2Score) {
                    eventListener.onGameEnd(PLAYER_NONE);
                } else if (player1Score > player2Score) {
                    eventListener.onGameEnd(PLAYER1);
                } else {
                    eventListener.onGameEnd(PLAYER2);
                }

            }
        } else {
            eventListener.onTurnChange(Game.State.PLAYER1_TURN, Player.PLAYER1);
        }

        return boxesCompleted;
    }

}