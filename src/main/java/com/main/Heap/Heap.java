package com.main.Heap;

import java.util.Arrays;

public class Heap {

    private int H[];
    private int D[];
    private int P[];
    private int HEAP_SIZE;

    /**
     * Constructor to initialize H, D and P arrays to totalHeapSize params.
     *
     * @param totalHeapSize - Maximum size of heap.
     */
    public Heap(int totalHeapSize){
        this.H = new int[totalHeapSize];
        this.D = new int[totalHeapSize];
        this.P = new int[totalHeapSize];
        Arrays.fill(this.P, -1);

        this.HEAP_SIZE = 0;
    }

    /**
     * Method to adjust an element to the correct position in H, D and P arrays.
     *
     * @param id - Id of the element to be adjusted in H, D and P arrays.
     * @param bandwidth - New bandwidth value of the vertex=id
     */
    public void adjust(int id, int bandwidth){
        int i = this.P[id];
        D[i] = bandwidth;
        heapifyUp(i);
    }

    /**
     * Method to return max element id and data/bandwidth stored in heap arrays.
     *
     * @return - array with vertex id and bandwidth value.
     */
    public int[] MAX() {
        return new int[]{H[0], D[0]};
    }

    /**
     * Method to insert element into the heap.
     *
     * @param id - Id of the element to be inserted in H, D and P arrays.
     * @param bandWidth - New bandwidth value of the vertex=id
     */
    public void insert(int id, int bandWidth) {
        //Increase H length by 1 and adding a to last index
        int i = this.HEAP_SIZE;
        this.HEAP_SIZE++;
        this.H[i] = id;
        this.P[H[i]] = i;
        this.D[i] = bandWidth;

        //if a was the first element in H then return as no swap needed
        if(this.HEAP_SIZE == 1)
            return;

        heapifyUp(i);
    }

    /**
     * Method to delete element from the heap.
     *
     * @param id - Id of the element to be deleted from H, D and P arrays.
     */
    public void delete(int id) {
        //Replace ith element with the last element in Heap H
        //and decrease overall length of H by 1
        int i = this.P[id];
        this.P[id] = -1;

        this.H[i] = this.H[this.HEAP_SIZE-1];

        this.P[H[i]] = i;
        this.D[i] = this.D[this.HEAP_SIZE-1];

        this.H[this.HEAP_SIZE-1] = -1;

        this.HEAP_SIZE--;

        //If there is 0 or only 1 element remaining after the deletion then return
        //No need to perform swaps
        if(H.length < 2) return;

        heapifyDown(i);
    }

    /**
     * Method to compute largest child index for ith element in the heap
     *
     * @param i - ith element index.
     * @return - largest child index.
     */
    private int computeLargeChildIndex(int i){
        //Calculating left child and right child index using formula
        int leftChildIndex = (2*i) + 1; int rightChildIndex = (2*i) + 2;
        int largeChildIndex = i;

        //If the calculated left child index if less than heap size and if
        //left child val is greater than parent val then replace largeChildIndex val.
        if(leftChildIndex < this.HEAP_SIZE && D[leftChildIndex] > D[largeChildIndex])
            largeChildIndex = leftChildIndex;

        //If the calculated right child index if less than heap size and if
        //right child val is greater than parent val then replace largeChildIndex val.
        if(rightChildIndex < this.HEAP_SIZE && D[rightChildIndex] > D[largeChildIndex])
            largeChildIndex = rightChildIndex;

        return largeChildIndex;
    }

    /**
     * Method to move ith element down a level in the heap/tree.
     *
     * @param i - ith element index
     * @return - element index of where the swapping stopped
     */
    private int heapifyDown(int i){
        //Calculating initial index value for large Child Index
        int smallestElementIndex = i;
        int largeChildIndex = computeLargeChildIndex(smallestElementIndex);

        //While our computed largeChildIndex is not equal to smallestElementIndex and
        //while our Parent val is greater than child val
        //Our smallestElementIndex and largeChildIndex will only be equal
        //when smallestElementIndex val is not larger than left or right child val.
        //This implies smallestElementIndex element is in the correct place in Heap H
        //This will also be true if left or right child index is greater than heapSize
        while (smallestElementIndex != largeChildIndex && D[smallestElementIndex]
                < D[largeChildIndex]) {

            //If parent value is smaller than child value
            //then we perform a swap between parent and child node
            int temp = H[smallestElementIndex];
            H[smallestElementIndex] = H[largeChildIndex];
            H[largeChildIndex] = temp;

            int tempP = P[H[smallestElementIndex]];
            P[H[smallestElementIndex]] = P[H[largeChildIndex]];
            P[H[largeChildIndex]] = tempP;

            int tempD = D[smallestElementIndex];
            D[smallestElementIndex] = D[largeChildIndex];
            D[largeChildIndex] = tempD;

            //As we performed the above swap, updating the index values for parent and
            //re-computing the largest child index
            smallestElementIndex = largeChildIndex;
            largeChildIndex = computeLargeChildIndex(smallestElementIndex);
        }

        return smallestElementIndex;
    }

    /**
     * Method to move ith element up a level in the heap/tree.
     *
     * @param i - ith element index
     * @return - element index of where the swapping stopped
     */
    private int heapifyUp(int i) {
        //Using the formula to calculate parent index
        int parentI = (i - 1) / 2;

        //While our child index is greater than 0 and
        //while our Parent val is smaller than child val
        //Once i is equal to 0 we have traversed the entire tree
        while (i > 0 && D[parentI] < D[i]) {

            //If parent value is smaller than child value
            //then we perform a swap between parent and child node
            int temp = H[i];
            H[i] = H[parentI];
            H[parentI] = temp;

            int tempP = P[H[i]];
            P[H[i]] = P[H[parentI]];
            P[H[parentI]] = tempP;

            int tempD = D[i];
            D[i] = D[parentI];
            D[parentI] = tempD;

            //Updating index value to parent index value
            i = parentI;

            //Using the formula to calculate parent index
            parentI = (i - 1) / 2;
        }
        return i;
    }

    /**
     * Method to adjust element in heap/tree structure.
     *
     * @param n - Size of heap.
     * @param i - ith element index
     */
    public void heapify(int n, int i)
    {
        int largest = i;
        int l = 2*i + 1;
        int r = 2*i + 2;

        if (l < n && this.D[l] < this.D[largest])
            largest = l;

        if (r < n && this.D[r] < this.D[largest])
            largest = r;

        if (largest != i) {
            int swap =  this.H[i];
            this.H[i] = this.H[largest];
            this.H[largest] = swap;

            int tempP = P[H[i]];
            P[H[i]] = P[H[largest]];
            P[H[largest]] = tempP;

            int tempD = D[i];
            D[i] = D[largest];
            D[largest] = tempD;

            heapify(n, largest);
        }
    }

    /**
     * Method to perform sorting in non-decreasing order
     *
     * @return - H array
     */
    public int[] sort() {
        int n = this.getHeapSize();

        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(n, i);

        for (int i=n-1; i>=0; i--) {
            int temp = this.H[0];
            this.H[0] = this.H[i];
            this.H[i] = temp;

            int tempP = P[H[0]];
            P[H[0]] = P[H[i]];
            P[H[i]] = tempP;

            int tempD = D[0];
            D[0] = D[i];
            D[i] = tempD;

            heapify(i, 0);
        }

        return this.H;
    }

    public int getHeapSize() {
        return HEAP_SIZE;
    }
}