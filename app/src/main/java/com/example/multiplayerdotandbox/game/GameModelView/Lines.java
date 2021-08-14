package com.example.multiplayerdotandbox.game.GameModelView;

import java.util.Locale;

public class Lines {
    private final int dot_start;
    private final int dot_end;
    private String key;
    public Dot from;
    public Dot to;
    public int color;

    public String getKey() {
        return key;
    }

    private void setKey(int dot_start, int dot_end, int color) {
        this.key = generateKey(dot_start, dot_end, color);
    }

    static String generateKey(int dot_start, int dot_end, int color) {
        return String.format(Locale.US, "%d_%d_%d", dot_start, dot_end, color);
    }

    public Lines(int dot_start, int dot_end) {
        this.dot_start = dot_start;
        this.dot_end = dot_end;
        setKey(dot_start, dot_end, color);
    }

    public int getDotStart() {
        return dot_start;
    }

    public int getDotEnd() {
        return dot_end;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "dot_start=" + dot_start +
                ", dot_end=" + dot_end +
                ", key='" + key + '\'' +
                ", from=" + from +
                ", to=" + to +
                '}';
    }

}
