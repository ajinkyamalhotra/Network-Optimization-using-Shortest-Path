package com.main.Algo;

import com.main.Graph.Edge;
import com.main.Graph.Vertex;
import com.main.Heap.Heap;

import java.util.LinkedList;

public class Kruskals {

    int totalNumberOfVertices;
    Edge[] allEdges;
    Heap heap;
    Vertex[] T;

    /**
     * Construct to initialize objects.
     *
     * @param totalNumberOfVertices - Total number of vertices in the graph.
     */
    public Kruskals(int totalNumberOfVertices){
        this.totalNumberOfVertices = totalNumberOfVertices;
        this.allEdges = new Edge[(this.totalNumberOfVertices*this.totalNumberOfVertices)/2];
        this.heap = new Heap((this.totalNumberOfVertices*this.totalNumberOfVertices)/2);
        this.T = new Vertex[totalNumberOfVertices];
    }

    /**
     * This method runs the Kruskal's algorithm.
     * Time Complexity: O(m * log m)
     *
     * @param graph - Vertex object array which represents the entire graph.
     * @param edges - LinkedList of all the edges object of the above-mentioned graph.
     * @param totalNumberOfVertices - Total number of vertices in the graph.
     * @param typeOfGraph - String to specify whether the algorithm is being run on sparse or dense graph.
     */
    public Vertex[] kruskals(Vertex[] graph, LinkedList<Edge> edges, int totalNumberOfVertices, String typeOfGraph, double[] runtime){
        long start = System.currentTimeMillis();

        for(int i=0; i<totalNumberOfVertices; ++i)
            T[i] = makeSet(graph[i]);

        generateHeapAndEdges(edges);
        int[] sortedEdges = heap.sort();

        for(int i=0; i<heap.getHeapSize(); ++i){
            Edge maxEdge = allEdges[sortedEdges[i]];
            Vertex u = maxEdge.getSource();
            Vertex v = maxEdge.getDestination();

            Vertex rootU = find(u);
            Vertex rootV = find(v);
            if(rootV != rootU)
                union(T, rootU, rootV, maxEdge);
        }

        runtime[0] = Helper.endTimerAndPrintStats(start, "Krushkals on ", typeOfGraph);

        return T;
    }

    /**
     * This method creates new disjoint vertex copy.
     *
     * @param v - Vertex whose disjoint copy needs to be created.
     * @return - The newly created vertex.
     */
    private Vertex makeSet(Vertex v) {
        return new Vertex(v.getId());
    }

    /**
     * This method find root of the given vertex
     *
     * @param v - Vertex whose root needs to be found.
     * @return - Root of vertex v.
     */
    private Vertex find(Vertex v){
        Vertex r = v;
        LinkedList<Vertex> s = new LinkedList<>();
        int vCount = 0;
        while(r.getDad() != null){
            s.add(r);
            r = r.getDad();
            ++vCount;
        }

        while(vCount != 0){
            Vertex w = s.remove();
            w.setDad(r);
            --vCount;
        }

        return r;
    }

    /**
     * This method connects two vertices to each other by adding an edge.
     *
     * @param T - Graph where the vertices are to be connected.
     * @param r1 - First vertex to be connected.
     * @param r2 - Second vertex to be connected.
     * @param maxEdge - Edge used to connect r1 and r2 vertex.
     */
    private void union(Vertex[] T, Vertex r1, Vertex r2, Edge maxEdge){
        if(r1.getRank() > r2.getRank()) {
            r2.setDad(r1);
        } else if(r1.getRank() < r2.getRank()) {
            r1.setDad(r2);
        } else {
            r1.setDad(r2);
            r2.setRank(r2.getRank()+1);
        }

        //Make edge between r1 and r2 vertex along with backEdge
        LinkedList<Edge> neighbors = T[r1.getId()].getNeighbors();
        neighbors.add(maxEdge);

        LinkedList<Edge> destinationNeighbors = T[r2.getId()].getNeighbors();
        Edge backEdge = new Edge(maxEdge.getDestination(), maxEdge.getSource(), maxEdge.getWeight());
        destinationNeighbors.add(backEdge);
    }

    /**
     * This method generates new edge object and stores them in array and edge heap.
     *
     * @param edges - LinkedList containing all the edges of graph.
     */
    private void generateHeapAndEdges(LinkedList<Edge> edges){
        int i = 0;
        for(Edge e : edges){
            Edge edge = new Edge(T[e.getDestination().getId()], T[e.getSource().getId()], e.getWeight(), i);
            this.heap.insert(edge.getId(), edge.getWeight());
            this.allEdges[i] = edge;
            i++;
        }
    }
}
