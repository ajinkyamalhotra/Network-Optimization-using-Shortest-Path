package com.main.Algo;

import com.main.Heap.Heap;
import com.main.Graph.Edge;
import com.main.Graph.Vertex;

import java.util.LinkedList;

public class Dijkstras {
    private final static String IN_TREE = "intree";
    private final static String FRINGER = "fringer";
    private final static String UNSEEN = "unseen";
    int totalNumberOfVertices;
    Heap heap;

    public Dijkstras(int totalNumberOfVertices){
        this.totalNumberOfVertices = totalNumberOfVertices;
        this.heap = new Heap(this.totalNumberOfVertices);
    }

    /**
     * This method runs the Dijkstras WITHOUT heap algorithm.
     * Time Complexity: O(n^2)
     *
     * @param graph - Vertex object array which represents the entire graph.
     * @param sourceIndex - Index of the selected source vertex.
     * @param typeOfGraph - String to specify whether the algorithm is being run on sparse or dense graph.
     */
    public void withoutHeap(Vertex[] graph, int sourceIndex, String typeOfGraph, double[] runtime){
        long start = System.currentTimeMillis();

        Vertex source = graph[sourceIndex];
        source.setStatus(IN_TREE);
        source.setBandwidth(Integer.MAX_VALUE);
        source.setDad(null);

        LinkedList<Vertex> fringers = new LinkedList<>();;
        int numberOfFringers = 0;
        for(Edge edge : source.getNeighbors()){
            Vertex w = edge.getDestination();
            w.setStatus(FRINGER);
            w.setDad(source);
            w.setBandwidth(edge.getWeight());
            fringers.add(w);
            numberOfFringers++;
        }

        while(numberOfFringers != 0){
            Vertex v = new Vertex(-1);
            v.setBandwidth(Integer.MIN_VALUE);
            for(Vertex fringe : fringers){
                if(fringe.getBandwidth() > v.getBandwidth())
                    v = fringe;
            }

            v.setStatus(IN_TREE); fringers.remove(v); numberOfFringers--;

            for(Edge edge : v.getNeighbors()){
                int wMinBandWidth = Math.min(v.getBandwidth(), edge.getWeight());
                Vertex w = edge.getDestination();
                if(w.getStatus().equals(UNSEEN)){
                    w.setStatus(FRINGER); w.setDad(v);
                    w.setBandwidth(wMinBandWidth);
                    fringers.add(w); numberOfFringers++;
                }

                else if(w.getStatus().equals(FRINGER) && w.getBandwidth()<wMinBandWidth){
                    w.setDad(v);
                    w.setBandwidth(wMinBandWidth);
                }
            }
        }
        runtime[0] = Helper.endTimerAndPrintStats(start, "Dijkstras Without Heap", typeOfGraph);
    }

    /**
     * This method runs the Dijkstras WITH heap algorithm.
     * Time Complexity: O((n+m) * log n) = O(m * log n)
     *
     * @param graph - Vertex object array which represents the entire graph.
     * @param sourceIndex - Index of the selected source vertex.
     * @param typeOfGraph - String to specify whether the algorithm is being run on sparse or dense graph.
     */
    public void withHeap(Vertex[] graph, int sourceIndex, String typeOfGraph, double[] runtime){
        long start = System.currentTimeMillis();

        Vertex source = graph[sourceIndex];
        source.setStatus(IN_TREE);
        source.setBandwidth(Integer.MAX_VALUE);
        source.setDad(null);

        for(Edge edge : source.getNeighbors()){
            Vertex w = edge.getDestination();
            w.setStatus(FRINGER);
            w.setDad(source);
            w.setBandwidth(edge.getWeight());
            heap.insert(w.getId(), w.getBandwidth());
        }

        while(heap.getHeapSize() > 0){
            int[] max = heap.MAX();
            Vertex v = graph[max[0]];
            v.setStatus(IN_TREE);
            heap.delete(v.getId());

            for(Edge edge : v.getNeighbors()){
                int wMinBandWidth = Math.min(v.getBandwidth(), edge.getWeight());
                Vertex w = edge.getDestination();
                if(w.getStatus().equals(UNSEEN)){
                    w.setStatus(FRINGER); w.setDad(v);
                    w.setBandwidth(wMinBandWidth);
                    heap.insert(w.getId(), w.getBandwidth());
                }

                else if(w.getStatus().equals(FRINGER) && w.getBandwidth()<wMinBandWidth){
                    w.setDad(v);
                    w.setBandwidth(wMinBandWidth);
                    heap.adjust(w.getId(), w.getBandwidth());
                }
            }
        }
        runtime[0] = Helper.endTimerAndPrintStats(start, "Dijkstras With Heap", typeOfGraph);
    }
}
