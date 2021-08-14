package com.example.multiplayerdotandbox.game.GameModelView;

import java.util.ArrayList;

public class Dot {
    public String board;
    public ArrayList<Lines> links;
    public int value;

    public Dot(String board, int value) {
        this.links = new ArrayList<>();
        this.value = value;
        this.board = board;
    }
}
