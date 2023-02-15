package com.main.Graph;

import java.util.LinkedList;
import java.util.Random;

public class Graph {
    private final Random random = new Random();

    private final int TOTAL_NUMBER_OF_VERTICES = 5000;

    private final int SPARSE_GRAPH_EDGES = 6;

    private final int DENSE_GRAPH_EDGES = 945;

    private final int MAX_WEIGHT = 10000;

    private Vertex[] sparseGraph;

    private Vertex[] denseGraph;

    private LinkedList<Edge> sparseGraphEdges = new LinkedList<>();

    private LinkedList<Edge> denseGraphEdges = new LinkedList<>();

    /**
     * Construct to start graph generation.
     */
    public Graph(){
        this.sparseGraph = makeSparseGraph();
        this.denseGraph = makeDenseGraph();
    }

    /**
     * This method starts sparse graph generation by initializing all vertices, calling makeCycle method and
     * lastly calling makeRandomConnections method.
     *
     * @return - Vertex object array which represents the entire graph.
     */
    private Vertex[] makeSparseGraph(){
        System.out.print("Sparse graph generation in progress...... ");
        Vertex[] sparseGraph = new Vertex[TOTAL_NUMBER_OF_VERTICES];
        for(int i=0; i<this.TOTAL_NUMBER_OF_VERTICES; ++i){
            Vertex v = new Vertex(i);
            sparseGraph[i] = v;
        }
        makeCycle(sparseGraph, this.sparseGraphEdges);
        makeRandomConnections(sparseGraph, this.sparseGraphEdges, true, 500);
        System.out.println("Done!!");
        return sparseGraph;
    }

    /**
     * This method starts dense graph generation by initializing all vertices, calling makeCycle method and
     * lastly calling makeRandomConnections method.
     *
     * @return - Vertex object array which represents the entire graph.
     */
    private Vertex[] makeDenseGraph(){
        System.out.print("Dense graph generation in progress...... ");
        Vertex[] denseGraph = new Vertex[TOTAL_NUMBER_OF_VERTICES];
        for(int i=0; i<this.TOTAL_NUMBER_OF_VERTICES; ++i) {
            Vertex v = new Vertex(i);
            denseGraph[i] = v;
        }

        makeCycle(denseGraph, this.denseGraphEdges);
        makeRandomConnections(denseGraph, this.denseGraphEdges, false, 10);
        System.out.println("Done!!");

        return denseGraph;
    }

    /**
     * This method connects all the vertices to its adjacent vertex and make a cycle for the entire graph.
     *
     * @param graph - Vertex object array which represents the entire graph.
     * @param edges - LinkedList of all the edges object of the above-mentioned graph.
     */
    private void makeCycle(Vertex[] graph, LinkedList<Edge> edges){
        for(int i=0; i<this.TOTAL_NUMBER_OF_VERTICES; ++i) {
            int nextVertexIndex = (i==this.TOTAL_NUMBER_OF_VERTICES-1) ? 0 : i+1;
            Vertex currVertex = graph[i];
            Vertex nextVertex = graph[nextVertexIndex];
            int weight = random.nextInt(MAX_WEIGHT) + 1;
            connectVertices(currVertex, nextVertex, weight, edges, false);
            connectVertices(nextVertex, currVertex, weight, edges, true);
        }
    }

    /**
     * This method makes random connection until the numberOfConnectionsPerVertex requirement is met for the graph.
     *
     * @param graph - Vertex object array which represents the entire graph.
     * @param edges - LinkedList of all the edges object of the above-mentioned graph.
     * @param sparseGraph - Boolean flag to determine whether we are generating connections for sparse or dense graph.
     * @param retryRate - Retry rate value.
     */
    private void makeRandomConnections(Vertex[] graph, LinkedList<Edge> edges, boolean sparseGraph, int retryRate){
        for(int i=0; i<this.TOTAL_NUMBER_OF_VERTICES; ++i) {
            int numberOfConnectionsPerVertex =
                    (sparseGraph) ? this.SPARSE_GRAPH_EDGES : random.nextInt(150) + this.DENSE_GRAPH_EDGES;
            Vertex currVertex = graph[i];
            while(currVertex.getNeighbors().size() < numberOfConnectionsPerVertex){
                int retry = 0;
                Vertex randomVertex = graph[random.nextInt(TOTAL_NUMBER_OF_VERTICES)];
                while(retry <= retryRate && (randomVertex == currVertex ||
                        randomVertex.getNeighbors().size() >= numberOfConnectionsPerVertex)) {
                    randomVertex = graph[random.nextInt(TOTAL_NUMBER_OF_VERTICES)];
                    retry++;
                }

                if(retry > retryRate)
                    break;

                int weight = random.nextInt(MAX_WEIGHT) + 1;
                connectVertices(currVertex, randomVertex, weight, edges, false);
                connectVertices(randomVertex, currVertex, weight, edges, true);

            }
        }
    }

    /**
     * This method connects two vertices by adding a new edge into the adjacency list of the source.
     *
     * @param source - Source Vertex.
     * @param destination - Destination Vertex.
     * @param weight - Weight of the edge.
     * @param edges - LinkedList of all the edges object of the above-mentioned graph.
     * @param backEdge - Boolean flag to determine whether the edge being added is a backEdge orn not.
     */
    private void connectVertices(Vertex source, Vertex destination,
                                 int weight, LinkedList<Edge> edges, boolean backEdge){
        for(Edge e : source.getNeighbors())
            if(e.destination == destination)
                return;

        Edge edge = new Edge(source, destination, weight);
        source.getNeighbors().add(edge);

        if(!backEdge)
            edges.add(edge);
    }

    public int getTotalNumberOfVertices() {
        return TOTAL_NUMBER_OF_VERTICES;
    }

    public Vertex[] getSparseGraph() {
        return this.sparseGraph;
    }

    public Vertex[] getDenseGraph() {
        return this.denseGraph;
    }

    public LinkedList<Edge> getSparseGraphEdges() {
        return this.sparseGraphEdges;
    }

    public LinkedList<Edge> getDenseGraphEdges() {
        return this.denseGraphEdges;
    }
}
