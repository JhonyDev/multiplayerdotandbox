package com.example.multiplayerdotandbox.interfaces;

import com.example.multiplayerdotandbox.game.GameModelView.Lines;
import com.example.multiplayerdotandbox.game.controllerss.Game;

public interface GameEventListener {

    void onScoreUpdate(int player, int score);

    void onTurnChange(Game.State state, int player);

    void onGameEnd(int winner);

    void onPlayerMove(Lines edge);

    void onBotCompute(Lines edge);

}
