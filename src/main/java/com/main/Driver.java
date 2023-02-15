package com.main;

import com.main.Algo.Helper;
import com.main.Graph.Graph;
import com.main.Graph.Vertex;

import java.util.Random;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Driver extends Application {

    public static final int NUMBER_OF_GRAPHS = 5;

    public static final int NUMBER_OF_PAIRS_PER_GRAPH = 5;

    @Override
    public void start(Stage stage) throws Exception {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Iteration");
        LineChart<Number,Number> lineChartForSparseGraph = new LineChart<>(xAxis,yAxis);

        NumberAxis xAxis1 = new NumberAxis();
        NumberAxis yAxis1 = new NumberAxis();
        xAxis1.setLabel("Iteration");
        LineChart<Number,Number> lineChartForDenseGraph = new LineChart<>(xAxis1,yAxis1);

        XYChart.Series runTimeDijkSparse = new XYChart.Series();
        runTimeDijkSparse.setName("Dijktras without heap");

        XYChart.Series runTimeDijkWithHeapSparse = new XYChart.Series();
        runTimeDijkWithHeapSparse.setName("Dijkstras with heap");

        XYChart.Series runtimeKruskalsSparse = new XYChart.Series();
        runtimeKruskalsSparse.setName("Kruskals");

        XYChart.Series runTimeDijkDense = new XYChart.Series();
        runTimeDijkDense.setName("Dijktras without heap");

        XYChart.Series runTimeDijkWithHeapDense = new XYChart.Series();
        runTimeDijkWithHeapDense.setName("Dijkstras with heap");

        XYChart.Series runtimeKruskalsDense = new XYChart.Series();
        runtimeKruskalsDense.setName("Kruskals");

        int chartIndex = 1;
        for(int num = 1; num <= NUMBER_OF_GRAPHS; ++num) {
            //Initializing classes
            Helper helper = new Helper();
            Random random = new Random();
            Graph graph = new Graph();

            //Getting graphs data
            Vertex[] sparseGraph = graph.getSparseGraph();
            Vertex[] denseGraph = graph.getDenseGraph();
            int totalNumberOfVertices = graph.getTotalNumberOfVertices();

            //Printing graph and graph degree
            //helper.printGraph(sparseGraph); //Uncomment to print the entire graph
            helper.printGraphDegree(sparseGraph, "SpareGraph");
            //helper.printGraph(denseGraph); // Uncomment to print the entire graph
            helper.printGraphDegree(denseGraph, "DenseGraph");

            //Running all 3 algorithms in a loop on different randomly selected source and destination vertices
            System.out.println("\n\nRunning Algorithms:\n=======================");
            for (int i = 1; i <= NUMBER_OF_PAIRS_PER_GRAPH; ++i) {
                //Randomly selecting source and destination vertices
                int randomSourceVertexIndex = random.nextInt(totalNumberOfVertices);
                int randomDestinationVertexIndex = random.nextInt(totalNumberOfVertices);

                //while source and destination are the same, re-pick the destination vertex randomly again.
                while (randomSourceVertexIndex == randomDestinationVertexIndex)
                    randomDestinationVertexIndex = random.nextInt(totalNumberOfVertices);

                double[] runTimeForSparseGraph = new double[1];

                //Running all 3 algorithm for the randomly selected source and destination for sparse graph
                int maxBwFromDijk = helper.dijktrasWithoutHeap(sparseGraph, randomSourceVertexIndex,
                        randomDestinationVertexIndex, totalNumberOfVertices, "Sparse", runTimeForSparseGraph);
                runTimeDijkSparse.getData().add(new XYChart.Data(chartIndex, runTimeForSparseGraph[0]));

                int maxBwFromDijkWithHeap = helper.dijktrasWithHeap(sparseGraph, randomSourceVertexIndex,
                        randomDestinationVertexIndex, totalNumberOfVertices, "Sparse", runTimeForSparseGraph);
                runTimeDijkWithHeapSparse.getData().add(new XYChart.Data(chartIndex, runTimeForSparseGraph[0]));

                int maxBwFromKrushkals = helper.kruskals(sparseGraph, graph.getSparseGraphEdges(),
                        randomSourceVertexIndex, randomDestinationVertexIndex, totalNumberOfVertices,
                        "Sparse", runTimeForSparseGraph);
                runtimeKruskalsSparse.getData().add(new XYChart.Data(chartIndex, runTimeForSparseGraph[0]));

                //Making sure that all the algorithms produced the same maximum bandwidth value
                helper.checkBW(maxBwFromDijk, maxBwFromDijkWithHeap, maxBwFromKrushkals);

                double[] runTimeForDenseGraph = new double[1];
                //Running all 3 algorithm for the randomly selected source and destination for dense graph
                maxBwFromDijk = helper.dijktrasWithoutHeap(denseGraph, randomSourceVertexIndex,
                        randomDestinationVertexIndex, totalNumberOfVertices, "Dense", runTimeForDenseGraph);
                runTimeDijkDense.getData().add(new XYChart.Data(chartIndex, runTimeForDenseGraph[0]));

                maxBwFromDijkWithHeap = helper.dijktrasWithHeap(denseGraph, randomSourceVertexIndex,
                        randomDestinationVertexIndex, totalNumberOfVertices, "Dense", runTimeForDenseGraph);
                runTimeDijkWithHeapDense.getData().add(new XYChart.Data(chartIndex, runTimeForDenseGraph[0]));

                maxBwFromKrushkals = helper.kruskals(denseGraph, graph.getDenseGraphEdges(), randomSourceVertexIndex,
                        randomDestinationVertexIndex, totalNumberOfVertices, "Dense", runTimeForDenseGraph);
                runtimeKruskalsDense.getData().add(new XYChart.Data(chartIndex, runTimeForDenseGraph[0]));

                //Making sure that all the algorithms produced the same maximum bandwidth value
                helper.checkBW(maxBwFromDijk, maxBwFromDijkWithHeap, maxBwFromKrushkals);

                chartIndex++;
            }
            System.out.println("\n\nPrinting table for comparison of execution time between algorithms for graph-"+num);
            System.out.println(helper.printStatsTable());
        }

        //Plotting result in form of line charts for better representation
        lineChartForSparseGraph.setPrefSize(1250.00, 1250.00); // Adjust resolution according to your screen
        lineChartForSparseGraph.getData().addAll(runTimeDijkSparse, runTimeDijkWithHeapSparse, runtimeKruskalsSparse);
        lineChartForSparseGraph.setTitle("Performance comparison Sparse graph");

        lineChartForDenseGraph.setPrefSize(1250.00, 1250.00); // Adjust resolution according to your screen
        lineChartForDenseGraph.getData().addAll(runTimeDijkDense, runTimeDijkWithHeapDense, runtimeKruskalsDense);
        lineChartForDenseGraph.setTitle("Performance comparison Dense graph");

        FlowPane root = new FlowPane();
        root.getChildren().addAll(lineChartForSparseGraph);
        root.getChildren().addAll(lineChartForDenseGraph);

        Scene scene = new Scene(root, 200, 150);
        stage.setTitle("Graph Algo Performance Comparison");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}