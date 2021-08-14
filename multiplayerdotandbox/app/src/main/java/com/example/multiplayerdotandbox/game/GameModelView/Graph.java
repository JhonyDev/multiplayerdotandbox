package com.example.multiplayerdotandbox.game.GameModelView;

import java.util.ArrayList;
import java.util.HashMap;

public class Graph {
    private final HashMap<String, Lines> edges;
    private final int rows;
    private final int columns;

    public Graph(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.edges = new HashMap<>();
    }

    public HashMap<String, Lines> getEdges() {
        return edges;
    }

    public Graph getCopy() {
        Graph graph = new Graph(rows, columns);
        for (Lines edge : edges.values())
            graph.addEdge(edge);

        return graph;
    }

    public ArrayList<Lines> getAvailableEdges() {
        ArrayList<Lines> availableEdges = new ArrayList<>();

        int dots_rows = rows + 1;
        int dots_columns = columns + 1;
        int allDotsCount = dots_rows * dots_columns;

        for (int i = 0; i < allDotsCount; i++) {
            if (i % dots_columns != (dots_columns - 1)) {
                Lines edge = new Lines(i, i + 1);
                if (!hasEdge(edge.getKey()))
                    availableEdges.add(edge);
            }

            if (i / dots_rows != (dots_rows - 1)) {
                Lines edge = new Lines(i, i + dots_columns);
                if (!hasEdge(edge.getKey()))
                    availableEdges.add(edge);
            }
        }

        return availableEdges;
    }

    public void addEdge(Lines edge) {
        edges.put(edge.getKey(), edge);
    }

    public boolean hasEdge(int dotStart, int dotEnd) {
        return edges.containsKey(Lines.generateKey(dotStart, dotEnd, 0));
    }

    public boolean hasEdge(String edgeKey) {
        return edges.containsKey(edgeKey);
    }
}



