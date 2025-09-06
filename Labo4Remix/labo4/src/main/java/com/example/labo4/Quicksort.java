package com.example.labo4;

import static java.util.Collections.swap;

public class Quicksort extends SortingAlgo {
    private final QuickSortController quickSortController;

    public Quicksort(int[] array, QuickSortController quickSortController) {
        super(array);
        this.quickSortController = quickSortController;

    }
    @Override
    protected void divide() {

        Thread sortThread = new Thread(() -> {
            quickSort(0, array.length - 1);
        });
        sortThread.setDaemon(true);
        sortThread.start();
    }

    private void quickSort(int low, int high) {
        if (low < high) {
            int partitionIndex = partition(low, high);

            quickSort(low, partitionIndex - 1);
            quickSort(partitionIndex + 1, high);


        }
    }

    private int partition(int low, int high) {
        int pivot = array[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (array[j] <= pivot) {
                i++;
                swap(i, j);

                quickSortController.updateBarChart(array, i, j);

            }
        }


        swap(i + 1, high);

        quickSortController.updateBarChart(array, i + 1, high);


        return i + 1;
    }

    private void swap(int i, int j) {

        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;


        int[] currentState = array.clone();


        quickSortController.waitIfPaused();


        quickSortController.updateBarChart(currentState, i, j);


        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }



}
