package com.main.Algo;

import com.main.Graph.Edge;
import com.main.Graph.Vertex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Helper {
    static List<List<String>> rows = new ArrayList<>();

    static List<String> row = new ArrayList<>();
    public Helper(){
        List<String> headers = Arrays.asList("| GraphType |", " Source and Destination ", "| DijktrasWithoutHeap |", " DijktrasWithHeap ", "| Krushkals |" +
                "\n| ========= | ====================== | =================== | ================ | ========= |");
        rows.add(headers);
    }

    /**
     * This method is responsible for running Dijkstras without heap, printing maximum bandwidth found by the algo,
     * print path and reset graph so that next algorithm can find the maximum bandwidth from scratch on the same graph.
     *
     * @param graph - Vertex object array which represents the entire graph.
     * @param randomSourceVertexIndex - Index of the randomly selected source vertex.
     * @param randomDestinationVertexIndex - Index of the randomly selected destination vertex.
     * @param totalNumberOfVertices - Total number of vertices in the graph.
     * @param typeOfGraph - String to specify whether the algorithm is being run on sparse or dense graph.
     * @return - The maximum bandwidth value found by the algo.
     */
    public int dijktrasWithoutHeap(Vertex[] graph, int randomSourceVertexIndex, int randomDestinationVertexIndex, int totalNumberOfVertices, String typeOfGraph, double[] runTime){
        Dijkstras dijkstras = new Dijkstras(totalNumberOfVertices);
        dijkstras.withoutHeap(graph, randomSourceVertexIndex, typeOfGraph, runTime);
        int maxBandwidth = printMaxBW(graph, randomSourceVertexIndex, randomDestinationVertexIndex);
        printPath(graph, randomSourceVertexIndex, randomDestinationVertexIndex);
        reset(graph);

        row.add("| "+typeOfGraph);
        row.add("| From ("+randomSourceVertexIndex+") to ("+randomDestinationVertexIndex+")");
        row.add("| "+runTime[0]);
        System.out.println("=====");
        return maxBandwidth;
    }

    /**
     * This method is responsible for running Dijkstras with heap, printing maximum bandwidth found by the algo,
     * print path and reset graph so that next algorithm can find the maximum bandwidth from scratch on the same graph.
     *
     * @param graph - Vertex object array which represents the entire graph.
     * @param randomSourceVertexIndex - Index of the randomly selected source vertex.
     * @param randomDestinationVertexIndex - Index of the randomly selected destination vertex.
     * @param totalNumberOfVertices - Total number of vertices in the graph.
     * @param typeOfGraph - String to specify whether the algorithm is being run on sparse or dense graph.
     * @return - The maximum bandwidth value found by the algo.
     */
    public int dijktrasWithHeap(Vertex[] graph, int randomSourceVertexIndex, int randomDestinationVertexIndex, int totalNumberOfVertices, String typeOfGraph, double[] runTime){
        Dijkstras dijkstras = new Dijkstras(totalNumberOfVertices);
        dijkstras.withHeap(graph, randomSourceVertexIndex, typeOfGraph, runTime);
        int maxBandwidth = printMaxBW(graph, randomSourceVertexIndex, randomDestinationVertexIndex);
        printPath(graph, randomSourceVertexIndex, randomDestinationVertexIndex);
        reset(graph);

        row.add("|   "+runTime[0]);
        System.out.println("=====");
        return maxBandwidth;
    }

    /**
     * This method is responsible for running Kruskal's, running DFS to find maxBW path, printing maximum bandwidth
     * found by the algo, print path and reset graph so that next algorithm can find the maximum bandwidth
     * from scratch on the same graph.
     *
     * @param graph - Vertex object array which represents the entire graph.
     * @param edges - LinkedList of all the edges object of the above-mentioned graph.
     * @param randomSourceVertexIndex - Index of the randomly selected source vertex.
     * @param randomDestinationVertexIndex - Index of the randomly selected destination vertex.
     * @param totalNumberOfVertices - Total number of vertices in the graph.
     * @param typeOfGraph - String to specify whether the algorithm is being run on sparse or dense graph.
     * @return - The maximum bandwidth value found by the algo.
     */
    public int kruskals(Vertex[] graph, LinkedList<Edge> edges, int randomSourceVertexIndex, int randomDestinationVertexIndex, int totalNumberOfVertices, String typeOfGraph, double[] runTime){
        Kruskals kruskals = new Kruskals(totalNumberOfVertices);
        Vertex[] T = kruskals.kruskals(graph, edges, totalNumberOfVertices, typeOfGraph, runTime);
        T[randomSourceVertexIndex].setBandwidth(Integer.MAX_VALUE);
        DFS(T[randomSourceVertexIndex]);
        int maxBandwidth = printMaxBW(T, randomSourceVertexIndex, randomDestinationVertexIndex);
        printPath(T, randomSourceVertexIndex, randomDestinationVertexIndex);
        reset(graph);

        row.add("| "+runTime[0]);
        rows.add(row);
        row = new ArrayList<>();
        return maxBandwidth;
    }

    /**
     * Normal Depth First search algo to set maximum Bandwidth path from the given source.
     *
     * @param source - Source Vertex
     */
    private void DFS(Vertex source) {
        source.setStatus("seen");

        for(Edge edge : source.getNeighbors()){
            Vertex nextVertex = edge.getDestination();
            if(nextVertex.getStatus().equals("unseen")) {
                nextVertex.setDad(source);
                nextVertex.setBandwidth(Math.min(source.getBandwidth(), edge.getWeight()));
                DFS(nextVertex);
            }
        }
    }

    /**
     * Method to print the entire given graph.
     *
     * @param graph - Vertex object array which represents the entire graph.
     */
    public void printGraph(Vertex[] graph){
        for (Vertex vertex : graph) {
            System.out.print(vertex.getId() + ": ");
            for (Edge edge : vertex.getNeighbors()){
                System.out.print("(V:"+edge.getDestination().getId() + ", W:"+edge.getWeight()+") -> ");
            }
            System.out.println();
        }
    }

    /**
     * Method to calculate and print the degree of the entire given graph.
     *
     * @param graph - Vertex object array which represents the entire graph.
     * @param typeOfGraph - String to specify whether the algorithm is being run on sparse or dense graph.
     */
    public void printGraphDegree(Vertex[] graph, String typeOfGraph){
        int sumOfEdges = 0;
        for (Vertex vertex : graph) {
            sumOfEdges += vertex.getNeighbors().size();
        }
        System.out.println("Degree of "+ typeOfGraph +" is :"+sumOfEdges/graph.length);
    }

    /**
     * Method to print Maximum bandwidth value found by the algorithms.
     *
     * @param graph - Vertex object array which represents the entire graph.
     * @param sourceIndex - Index of the selected source vertex.
     * @param destinationIndex - Index of the selected destination vertex.
     * @return - Maximum Bandwidth value calculated by the algo for the given destination vertex.
     */
    private int printMaxBW(Vertex[] graph, int sourceIndex, int destinationIndex){
        Vertex w = graph[destinationIndex];
        System.out.println("Maximum Bandwidth from source vertex ("+graph[sourceIndex].getId()+ ") to destination vertex (" +w.getId() +") is: " +w.getBandwidth());
        return w.getBandwidth();
    }

    /**
     * Method to print Maximum bandwidth path found by the algorithms.
     *
     * @param graph - Vertex object array which represents the entire graph.
     * @param sourceIndex - Index of the selected source vertex.
     * @param destinationIndex - Index of the selected destination vertex.
     */
    private void printPath(Vertex[] graph, int sourceIndex, int destinationIndex){
        Vertex source = graph[sourceIndex];
        Vertex destination = graph[destinationIndex];
        System.out.print("Path from (" + source.getId() + ") to (" + destination.getId() +") is ");
        while(destination != source){
            System.out.print("(V:"+destination.getId()+ ", W:"+destination.getBandwidth()+") <- ");
            destination = destination.getDad();
        }
        System.out.println("(V:"+destination.getId()+ ", W:"+destination.getBandwidth()+")");
    }

    /**
     * Method to end the timer which was started at the beginning of the algorithm run and print execution time.
     *
     * @param start - Start time param provided by the algorithm.
     * @param algoName - String to report algorithm name.
     * @param typeOfGraph - String to report which type of graph the algorithm ran on.
     */
    public static double endTimerAndPrintStats(long start, String algoName, String typeOfGraph){
        long end = System.currentTimeMillis();
        System.out.println("Printing stats for " + algoName + " on "+ typeOfGraph+":");
        System.out.println("Execution time: " + (end - start) + " milliseconds");

        return (end - start);
    }

    /**
     * Method to make sure that the bandwidths for all 3 algorithms is exactly the same.
     * If it is not then a custom exception is raised.
     *
     * @param maxBwFromDijk - Max bandwidth value from Dijkstras without heap algo.
     * @param maxBwFromDijkWithHeap - Max bandwidth value from Dijkstras with heap algo.
     * @param maxBwFromKrushkals - Max bandwidth value from Krushkals algo.
     * @throws Exception - Exception to report the invalid result as all algos should have the same bandwidth value.
     */
    public void checkBW(int maxBwFromDijk, int maxBwFromDijkWithHeap, int maxBwFromKrushkals) throws Exception {
        if (maxBwFromDijk != maxBwFromKrushkals || maxBwFromDijkWithHeap != maxBwFromKrushkals) {
            throw new Exception("Invalid result as Maximum Bandwidth value should be the same for all algos");
        }
        System.out.println("=======================");
    }

    /**
     * Creates table with all the algorithm runtime for easier comparison.
     *
     * @return - Table in string format.
     */
    public String printStatsTable() {
        int[] maxLengths = new int[rows.get(0).size()];
        for (List<String> row : rows) {
            for (int i = 0; i < row.size(); i++) {
                maxLengths[i] = Math.max(maxLengths[i], row.get(i).length());
            }
        }

        StringBuilder formatBuilder = new StringBuilder();
        for (int maxLength : maxLengths) {
            formatBuilder.append("%-").append(maxLength).append("s");
        }
        String format = formatBuilder.toString();

        StringBuilder result = new StringBuilder();
        for (List<String> row : rows) {
            result.append(String.format(format, row.toArray(new String[0]))).append("\n");
        }
        return result.toString();
    }

    /**
     * Method to reset graph.
     *
     * @param graph - Vertex object array which represents the entire graph.
     */
    private void reset(Vertex[] graph){
        for(Vertex v : graph){
            v.setStatus("unseen");
            v.setBandwidth(0);
            v.setDad(null);
        }
    }
}
