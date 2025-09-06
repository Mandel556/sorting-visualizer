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

public class MergeSortController {

    @FXML
    private BarChart<String, Number> barChartMerge;

    @FXML
    private Button stopMerge;

    @FXML
    private Button startMerge;

    @FXML
    private Label labelMerge;

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
    private boolean animationInProgress = false;
    private int delayMs = 2000;
    private volatile boolean isPaused = false;








    public void initializeData(int[] numbers, String speed) {
        this.numbers = numbers;
        this.speed = speed;
        initializeBarChart();
        stopMerge.setOnAction(e -> handleStopButton());
        startMerge.setOnAction(e -> handleStartButton());

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
        barChartMerge.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (int i = 0; i < numbers.length; i++) {
            series.getData().add(new XYChart.Data<>(String.valueOf(i), numbers[i]));
        }
        barChartMerge.getData().add(series);
    }

    public synchronized void updateBarChart(int[] currentState, int highlightIndex1, int highlightIndex2) {
        if (animationInProgress) {
            try {
                Thread.sleep(delayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    animationInProgress = true;




        Platform.runLater(() -> {
            try {

                XYChart.Series<String, Number> series = barChartMerge.getData().get(0);


                for (int i = 0; i < currentState.length && i < series.getData().size(); i++) {
                    series.getData().get(i).setYValue(currentState[i]);
                }


                for (XYChart.Data<String, Number> data : series.getData()) {
                    data.getNode().setStyle("-fx-bar-fill: #f3622d;"); // Default color - same as QuickSort
                }


                if (highlightIndex1 >= 0 && highlightIndex1 < series.getData().size()) {
                    series.getData().get(highlightIndex1).getNode().setStyle("-fx-bar-fill: blue;");
                }
                if (highlightIndex2 >= 0 && highlightIndex2 < series.getData().size()) {
                    series.getData().get(highlightIndex2).getNode().setStyle("-fx-bar-fill: blue;");
                }


                Timeline timeline = new Timeline(
                        new KeyFrame(Duration.millis(delayMs), e -> {

                            for (XYChart.Data<String, Number> data : series.getData()) {
                                data.getNode().setStyle("-fx-bar-fill: #f3622d;"); // Default color
                            }
                            animationInProgress = false;
                        })
                );
                timeline.play();
            } catch (Exception e) {
                System.err.println("Merge animation error: " + e.getMessage());
                animationInProgress = false;
            }
        });



}
}
