package com.example.multiplayerdotandbox.game.controllerss;

import com.example.multiplayerdotandbox.game.GameModelView.Graph;
import com.example.multiplayerdotandbox.game.GameModelView.Grid;
import com.example.multiplayerdotandbox.game.GameModelView.Lines;
import com.example.multiplayerdotandbox.interfaces.GameEventListener;

public class Game {

    private final Grid board;
    private final Graph gameTree;
    private final int maxScore;
    private PlayerHuman playerHuman;
    private PlayerRobot playerRobot;
    private GameEventListener gameEventListener;

    public State getState() {
        return gameState;
    }

    public void setGameState(State gameState) {
        this.gameState = gameState;
    }

    public Graph getGameTree() {
        return gameTree;
    }

    public enum State {
        PLAYER1_TURN, PLAYER2_TURN, END
    }

    private State gameState = State.PLAYER1_TURN;

    public enum Mode {
        PLAYER, CPU
    }

    public Game(int rows, int columns) {
        this.maxScore = rows * columns;
        this.board = new Grid(rows, columns);
        this.gameTree = new Graph(rows, columns);
        this.gameState = State.PLAYER1_TURN;
        playerHuman = new PlayerHuman(this);
        playerRobot = new PlayerRobot(this);
    }

    public Grid getBoard() {
        return board;
    }

    public int makeAMove(int dotStart, int dotEnd) {
        if (gameTree.hasEdge(dotStart, dotEnd))
            return -1;

        gameTree.addEdge(new Lines(dotStart, dotEnd));

        switch (gameState) {
            case PLAYER1_TURN:
                gameState = Game.State.PLAYER1_TURN;
                return playerHuman.takeTurnPlayer1(board, maxScore, dotStart, dotEnd, gameEventListener);
            case PLAYER2_TURN:
                gameState = Game.State.PLAYER2_TURN;
                return playerRobot.takeTurnPlayer2(board, maxScore, dotStart, dotEnd, gameEventListener);
            case END:
                gameState = Game.State.END;
                return 0;
        }

        return 0;
    }

    public GameEventListener getGameEventListener() {
        return gameEventListener;
    }

    public void setGameEventListener(GameEventListener gameEventListener) {
        this.gameEventListener = gameEventListener;
    }


}


