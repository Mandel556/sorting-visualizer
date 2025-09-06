package com.example.labo4;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

public class QuickSortController {

    @FXML
    private BarChart<String, Number> barChartQuickSort;

    @FXML
    private Button stopQuickSort;

    @FXML
    private Button startQuickSort;

    @FXML
    private Label labelQuickSort;

    @FXML
    private void handleStopButton() {
        isPaused = true;
    }

    @FXML
    private void handleStartButton() {
        isPaused = false;
        synchronized (this) {
            notifyAll(); // Wake up any waiting threads
        }
    }


    private int[] numbers;
    private String speed;
    private Timeline timeline;
    private Queue<Runnable> animationQueue = new LinkedList<>();
    private boolean animationInProgress = false;
    private int delayMs = 2000;
    private volatile boolean isPaused = false;



    public void initializeData(int[] numbers, String speed) {
        this.numbers = numbers;
        this.speed = speed;
        initializeBarChart();

        stopQuickSort.setOnAction(e -> handleStopButton());
        startQuickSort.setOnAction(e -> handleStartButton());

        switch (speed) {
            case "Fast":
                delayMs = 500;
                break;
            case "Slow":
                delayMs = 2000;
                break;
            default: // "Normal"
                delayMs = 1000;
                break;
        }


    }

    public void waitIfPaused() {
        while (isPaused) {
            try {
                synchronized (this) {
                    wait(100); // Wait a bit, then check again
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void initializeBarChart() {
        barChartQuickSort.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (int i = 0; i < numbers.length; i++) {
            series.getData().add(new XYChart.Data<>(String.valueOf(i), numbers[i]));
        }
        barChartQuickSort.getData().add(series);
    }

    public synchronized void updateBarChart(int[] array, int index1, int index2) {
        if (animationInProgress) {

            try {
                Thread.sleep(delayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        animationInProgress = true;


        Platform.runLater(() -> {

            refreshChartData(array);


            highlightBars(index1, index2);


            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.millis(delayMs), e -> {

                        resetBarStyles();
                        animationInProgress = false;
                    })
            );
            timeline.play();
        });
    }

    private void refreshChartData(int[] array) {

        XYChart.Series<String, Number> series = barChartQuickSort.getData().get(0);
        for (int i = 0; i < array.length && i < series.getData().size(); i++) {
            series.getData().get(i).setYValue(array[i]);
        }

    }


    private void highlightBars(int index1, int index2) {
        XYChart.Series<String, Number> series = barChartQuickSort.getData().get(0);


        if (index1 < series.getData().size() && index2 < series.getData().size()) {
            series.getData().get(index1).getNode().setStyle("-fx-bar-fill: blue;");
            series.getData().get(index2).getNode().setStyle("-fx-bar-fill: blue;");
        }
    }


    private void resetBarStyles() {
        XYChart.Series<String, Number> series = barChartQuickSort.getData().get(0);
        for (XYChart.Data<String, Number> data : series.getData()) {
            data.getNode().setStyle("-fx-bar-fill: #f3622d;"); 
        }
    }
}








