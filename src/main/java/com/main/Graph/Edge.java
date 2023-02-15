package com.main.Graph;

public class Edge {

    Vertex source;
    Vertex destination;
    int weight;

    int id;

    public Edge(Vertex source, Vertex destination, int weight){
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    public Edge(Vertex source, Vertex destination, int weight, int id){
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    public Vertex getSource() {
        return this.source;
    }

    public Vertex getDestination() {
        return this.destination;
    }

    public int getWeight() {
        return weight;
    }
    public int getId() {
        return id;
    }

}
