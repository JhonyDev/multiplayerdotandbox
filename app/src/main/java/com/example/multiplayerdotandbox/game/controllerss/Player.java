package com.example.multiplayerdotandbox.game.controllerss;

import com.example.multiplayerdotandbox.game.GameModelView.Graph;
import com.example.multiplayerdotandbox.game.GameModelView.Grid;
import com.example.multiplayerdotandbox.game.GameModelView.Lines;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Player {

    public static final int PLAYER1 = 1;
    public static final int PLAYER2 = 2;
    public static final int PLAYER_NONE = 0;

    private final Game game;

    public Player(Game game) {
        this.game = game;
    }

    public Lines getNextMove() {

        ArrayList<Lines> completionMoves = getCompletionMoves(game.getGameTree(), game.getBoard());
        if (completionMoves.size() > 0) {
            Collections.shuffle(completionMoves);
            return completionMoves.get(0);
        } else {
            int minOpponentScoreAfterMove = Integer.MAX_VALUE;
            int opponentScore;
            Lines nextMove = null;

            ArrayList<Lines> availableMoves = game.getGameTree().getAvailableEdges();
            for (Lines moveToMake : availableMoves) {
                Grid boardCopy = game.getBoard().getCopy();
                Graph gameTreeCopy = game.getGameTree().getCopy();
                ArrayList<Lines> opponentCompletionMoves;
                boardCopy.setLineForDots(moveToMake.getDotStart(), moveToMake.getDotEnd(), Player.PLAYER2);
                gameTreeCopy.addEdge(moveToMake);
                opponentScore = 0;
                opponentCompletionMoves = getCompletionMoves(gameTreeCopy, boardCopy);
                Graph gameTreeOpponent = gameTreeCopy.getCopy();
                Grid boardOpponent = boardCopy.getCopy();

                while (opponentCompletionMoves.size() > 0) {
                    opponentScore += opponentCompletionMoves.size();

                    for (Lines opponentMoveToMake : opponentCompletionMoves) {
                        boardOpponent.setLineForDots(opponentMoveToMake.getDotStart(), opponentMoveToMake.getDotEnd(), Player.PLAYER1);
                        gameTreeOpponent.addEdge(opponentMoveToMake);
                    }

                    opponentCompletionMoves = getCompletionMoves(gameTreeOpponent, boardOpponent);
                }

                if (opponentScore < minOpponentScoreAfterMove) {
                    minOpponentScoreAfterMove = opponentScore;
                    nextMove = moveToMake;
                }
            }

            if (nextMove != null) {
                return nextMove;
            } else {
                Collections.shuffle(availableMoves);
                return availableMoves.get(0);
            }
        }
    }

    private ArrayList<Lines> getCompletionMoves(Graph gameTree, Grid board) {

        ArrayList<Lines> completionMoves = new ArrayList<>();
        HashMap<String, Lines> madeMoves = gameTree.getEdges();
        ArrayList<Lines> availableMoves = gameTree.getAvailableEdges();

        for (Lines moveToMake : availableMoves) {
            if (!madeMoves.containsKey(moveToMake.getKey())) {
                Grid tempBoard = board.getCopy();
                int scoreBefore = tempBoard.getScore(PLAYER2);
                tempBoard.setLineForDots(moveToMake.getDotStart(), moveToMake.getDotEnd(), PLAYER2);
                int scoreAfter = tempBoard.getScore(PLAYER2);
                if (scoreBefore < scoreAfter) {
                    completionMoves.add(moveToMake);
                }
            }
        }

        return completionMoves;
    }

}