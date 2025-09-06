package com.example.labo4;

import javafx.animation.Timeline;
import javafx.application.Platform;

import java.util.Arrays;

public class MergeSort extends SortingAlgo {

    private MergeSortController mergeSortController;


    public MergeSort(int[] array, MergeSortController mergeSortController) {
        super(array);
        this.mergeSortController = mergeSortController;
    }


    @Override
    protected void divide() {

        Thread sortThread = new Thread(() -> {
            mergeSort(array, 0, array.length - 1);
        });
        sortThread.setDaemon(true);
        sortThread.start();
    }


    private void mergeSort(int[] arr, int left, int right) {
        if (left < right) {

            int mid = left + (right - left) / 2;


            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);


            merge(arr, left, mid, right);
        }
    }




    private void merge(int[] arr, int left, int mid, int right) {

        int n1 = mid - left + 1;
        int n2 = right - mid;


        int[] L = new int[n1];
        int[] R = new int[n2];


        for (int i = 0; i < n1; ++i)
            L[i] = arr[left + i];
        for (int j = 0; j < n2; ++j)
            R[j] = arr[mid + 1 + j];


        int i = 0, j = 0;
        int k = left;

        mergeSortController.waitIfPaused();

        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;



            int[] currentState = arr.clone();
            mergeSortController.updateBarChart(currentState, k-1, mid);


            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }


        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;


            int[] currentState = arr.clone();
            mergeSortController.updateBarChart(currentState, k-1, mid);

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }


        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;


            int[] currentState = arr.clone();
            mergeSortController.updateBarChart(currentState, k-1, mid);
            mergeSortController.waitIfPaused();

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        int[] currentState = arr.clone();
        mergeSortController.updateBarChart(currentState, k-1, mid);
        mergeSortController.waitIfPaused();


    }




    }





