package com.main.Graph;

import java.util.LinkedList;

public class Vertex {

    private int rank = 0;
    private int id;
    private String status;
    private Vertex dad = null;
    private LinkedList<Edge> neighbors = new LinkedList<Edge>();
    private int bandwidth;

    /**
     * Constructor to initialize/create vertex object.
     *
     * @param id - id of the vertex.
     */
    public Vertex(int id){
        this.id = id;
        this.status = "unseen";
        this.rank = 0;
        this.neighbors = new LinkedList<>();
        this.dad = null;
    }

    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Vertex getDad() {
        return dad;
    }

    public void setDad(Vertex dad) {
        this.dad = dad;
    }

    public LinkedList<Edge> getNeighbors() {
        return neighbors;
    }

    public int getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(int bandwidth) {
        this.bandwidth = bandwidth;
    }

    public int getRank() {
        return this.rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}

