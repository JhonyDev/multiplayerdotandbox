package com.example.multiplayerdotandbox.game.controllerss;

import com.example.multiplayerdotandbox.game.GameModelView.Grid;
import com.example.multiplayerdotandbox.interfaces.GameEventListener;

public class PlayerHuman extends Player {

    public PlayerHuman(Game game) {
        super(game);
    }

    public int takeTurnPlayer1(Grid board, int maxScore, int dotStart, int dotEnd, GameEventListener eventListener) {

        int boxesCompleted = board.setLineForDots(dotStart, dotEnd, PLAYER1);
        if (boxesCompleted > 0) {
            int player1Score = board.getScore(PLAYER1);
            int player2Score = board.getScore(PLAYER2);
            eventListener.onScoreUpdate(PLAYER1, player1Score);
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
            eventListener.onTurnChange(Game.State.PLAYER2_TURN, PLAYER2);
        }

        return boxesCompleted;
    }

}