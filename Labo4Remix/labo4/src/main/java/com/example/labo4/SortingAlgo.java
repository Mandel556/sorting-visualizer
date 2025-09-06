package com.example.labo4;

public abstract class SortingAlgo {

    protected int[] array;


    public SortingAlgo(int[] array) {
        this.array = array;
    }

    public final void sort() {
        divide();
    }

    protected abstract void divide();

}
