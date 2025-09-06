package com.example.labo4;

import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

public class HelloController implements Initializable {


    @FXML
    private Label labelTri;

    @FXML
    private Label labelCollection;

    @FXML
    private Label labelSpeed;

    @FXML
    private ComboBox<String> comboBoxTri;

    @FXML
    private ComboBox<String> comboBoxSpeed;

    @FXML
    private TextField TextFieldCollection;


    @FXML
    private Button startButton;


    private int[] numbers;
    private String speed;



    @FXML
    private void handleStartButtonAction(ActionEvent event) {

        String selectedAlgorithm = comboBoxTri.getSelectionModel().getSelectedItem();


        String input = TextFieldCollection.getText();
        numbers = Arrays.stream(input.split(","))
                .map(String::trim)
                .mapToInt(Integer::parseInt)
                .toArray();


        speed = comboBoxSpeed.getSelectionModel().getSelectedItem();

        initializeData(numbers, speed);



        switch (selectedAlgorithm) {
            case "MergeSort":
                switchToMergeSortScene(event);
                break;
            case "Quicksort":
                switchToQuickSortScene(event);
                break;
            default:
                System.out.println("Invalid algorithm selected");
                break;
        }
    }



    private void switchToMergeSortScene(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("MergeSort-view.fxml"));
            Parent root = loader.load();


            MergeSortController controller = loader.getController();
            controller.initializeData(numbers, speed);

            MergeSort mergeSort = new MergeSort(numbers, controller);
            mergeSort.sort();


            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void switchToQuickSortScene(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/labo4/QuickSort.fxml"));
            Parent root = loader.load();

            QuickSortController controller = loader.getController();
            controller.initializeData(numbers, speed);

            Quicksort quickSort = new Quicksort(numbers, controller);
            quickSort.sort();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initializeData(int[] numbers, String speed) {
        this.numbers = numbers;
        this.speed = speed;

    }










    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.startButton.setOnAction(event -> handleStartButtonAction(event));


            comboBoxSpeed.getItems().addAll("Slow", "Normal", "Fast");



            comboBoxTri.getItems().addAll("Quicksort", "MergeSort");





        startButton.setOnAction(this::handleStartButtonAction);
    }
}