package com.example.multiplayerdotandbox.game.GameModelView;

import com.example.multiplayerdotandbox.game.controllerss.Player;

public class Box {
        public boolean top;
        public boolean right;
        public boolean bottom;
        public boolean left;
        public int player;

        Box(byte b) {
            top = ((b & 1) == 1);
            right = ((b & 2) == 2);
            bottom = ((b & 4) == 4);
            left = ((b & 8) == 8);

            if ((b & 16) == 16)
                player = Player.PLAYER1;
            else if ((b & 32) == 32)
                player = Player.PLAYER2;
            else
                player = Player.PLAYER_NONE;
        }
    }