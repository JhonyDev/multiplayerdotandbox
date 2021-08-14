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



//      Previous version of the method above
//     */
       /* Graph gameTree = game.getGameTree();
//
//        // pre-generate the moves
        Collection<Edge> availableMoves = gameTree.getAvailableEdges();
//
//        // assume the first to be the next one
        Edge next = null;
//
//        // worst case scenario for the outcome
        int value = Integer.MIN_VALUE;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
//
//        // try every move and see which one is best
        for (Edge move : availableMoves) {
            if (next == null)
                next = move;
//
//            // prefer the moves that will complete boxes
           gameTree.addEdge(move);
//
           value = Math.max(value, minimax(0, miniMaxBoard, false, alpha, beta));
            miniMaxBoard.removeLineForDots(move.getDotStart(), move.getDotEnd());
            alpha = Math.max(alpha, value);
//
           if (beta <= alpha) {
               Log.d(TAG, "getNextMove: " + next.getKey() + "  value: " + value);
                gameTree.removeEdge(move);
//
               return move;
           }
//
            gameTree.removeEdge(move);
        }

//
//    /**
//     * Serves as an utility function for the minimax algorithm
//     *
//     * @param board the board which represents the current game state.
//     * @return The integer value which is the maximum score that <b>Player 2</b> can make
//     */
   /* private int getValue(Board board) {
        return maxResult + board.getScore(Game.Player.PLAYER2) - board.getScore(Game.Player.PLAYER1);
    }

//    /**
//     *  Partially implemented minimax algorithm
//     */
    /*private int minimax(int level, Board board, boolean isMaximizer, int alpha, int beta) {
       Graph gameTree = game.getGameTree();
//
       // pre-generate the moves
       Collection<Edge> availableMoves = gameTree.getAvailableEdges();

        // don't go too deep into recursion
        if (level >= 3 || availableMoves.size() == 0) {
            return getValue(board);
        }

       if (isMaximizer) { // maximizer
            int result = Integer.MIN_VALUE;
           for (Edge nextMove : availableMoves) {
               // prefer the moves that will complete boxes
                gameTree.addEdge(nextMove);

                Board miniMaxBoard = new Board(board.getRows(), board.getColumns());
               miniMaxBoard.loadBoard(board.toString());
    miniMaxBoard.setLineForDots(nextMove.getDotStart(), nextMove.getDotEnd(), ID);

               result = Math.max(result, minimax(level + 1, miniMaxBoard, false, alpha, beta));
                alpha = Math.max(alpha, result);

               if (beta <= alpha) {
                  gameTree.removeEdge(nextMove);
                    return alpha;
                }

                miniMaxBoard.removeLineForDots(nextMove.getDotStart(), nextMove.getDotEnd());
                gameTree.removeEdge(nextMove);
            }

            return result;
        } else { // minimizer
            int result = Integer.MAX_VALUE;
            for (Edge nextMove : availableMoves) {
                gameTree.addEdge(nextMove);

                Board miniMaxBoard = new Board(board.getRows(), board.getColumns());
                miniMaxBoard.loadBoard(board.toString());
                miniMaxBoard.setLineForDots(nextMove.getDotStart(), nextMove.getDotEnd(), ID);

        result = Math.min(result, minimax(level + 1, miniMaxBoard, true, alpha, beta));
              beta = Math.min(beta, result);

               if (beta <= alpha) {
                    gameTree.removeEdge(nextMove);
                   return beta;
                }

             miniMaxBoard.removeLineForDots(nextMove.getDotStart(), nextMove.getDotEnd());
                gameTree.removeEdge(nextMove);
          }
          return result;
       }*/







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