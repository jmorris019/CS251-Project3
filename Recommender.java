import javax.management.remote.JMXServerErrorException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.rmi.server.ExportException;
import java.util.Arrays;
import java.util.Collections;

public class Recommender{

    /********************************
     * Do not change below code
     ********************************/

    int swaps, compares;
    int[] inversionCounts;
    String[] products;
    HashTable hashBaby = new HashTable();
    public Recommender(){
        swaps = 0;
        compares = 0;
    }

    public int getComapares() {
        return compares;
    }

    public int getSwaps() {
        return swaps;
    }
    /**************
     * This function is for the quick sort.
     **************/
    private boolean compare(int a ,int b){
        compares++;
        return a <= b;

    }
    /***************
     * This functions is for the quick sort.
     * By using this function, swap the similarity and the products at the same time.
     *****************/
    private void swap(int[] arr, int index1, int index2){
        swaps++;
        int temp = arr[index1];
        arr[index1] = arr[index2];
        arr[index2] = temp;

        String tempS = products[index1];
        products[index1] = products[index2];
        products[index2] = tempS;

    }

    /********************************
     * Do not change above code
     ********************************/
    public int singleInversionCount(String key) {
        int[] pList = hashBaby.get(key).value.depRating;
        int c = 0;
        for (int i = 0; i < pList.length -1; i++) {
            for (int j = i  + 1; j < pList.length; j++) {
                if (pList[i] > pList[j]) {
                    c++;
                }
            }
        }
        return c;
    }
    /**
     * This function is for the calculate inversion counts of each option's.
     * @param dataset is file name of all data for hash table
     * @param options is the list of product name which we want to getting the inversion counts
     * @return it is integer array of each option's inversion counts. The order of return should be matched with options.
     */
    public int[] inversionCounts(String dataset, String[] options)  {
        try {
            /* Loading the data in with functions from the HashTable object */
            File file = new File(dataset);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String tempString;
            boolean skip = true; // skip the first entry of the inputted text
            while ((tempString = br.readLine()) != null) {
                // add the new Product to the array
                if (!skip) {
                    Product temp = hashBaby.breakupLine(tempString);
                    hashBaby.put(temp.name, temp);
                }
                skip = false; // reset after first go through
            }
        } catch (Exception E) {
            System.out.println("ouch, file doesn't work");
        };
        /* Objects now loaded in to hashBaby.hashtable */
        /* get objects
        /* MergeSort recursive function */
        int[] toBeReturned = new int[options.length]; // create at array to store the corresponding inversion counts

        for (int i = 0; i < options.length; i++) { // sets each place in int array corresponding to the option ic
            toBeReturned[i] = singleInversionCount(options[i]);
        }
        return toBeReturned;
    }
    /* Method to create an identical array as options with item relatedness, 0 being very */
    public int[] relatedness(int based, int[] array) {
        int[] relatedA = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            int number = based - array[i];
            if (number < 0) { // absolute value
                number = number * -1;
            }
            relatedA[i] = number;
        }
        return relatedA;
    }


    int partition(int[] a, int l, int h) {
        int pvt = a[h];
        int i = (l-1);
        for (int j = l; j < h; j++) {
            if (compare(a[j], pvt)) {
                i++;
                swap(a, i , j);
            }
        }
        swap(a, (i+1), h);
        return i+1;
    }
    void qsort(int[] a, int l, int h) {
        if (l < h) {
            int pi = partition(a, l, h);
            qsort(a, l, pi -1);
            qsort(a, pi + 1, h);
        }
    }
    int findRating(String[] a, String name) {
        for (int i = 0; i < a.length; i++) {
            if (name.compareTo(a[i]) == 0) {
                return i;
            }
        }
        return -1;
    }
    /**
     * Get the sequence of recommendation from the dataset by sorting the inverse count.
     * Compare the similarity of depRating between RecentPurchase's and each option's.
     * Use inverse count to get the similarity of two array.
     * */
    public String[] recommend(String dataset, String recentPurchase, String[] options) {
        products = options.clone();
        try {
            /* Loading the data in with functions from the HashTable object */
            File file = new File(dataset);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String tempString;
            boolean skip = true; // skip the first entry of the inputted text
            while ((tempString = br.readLine()) != null) {
                // add the new Product to the array
                if (!skip) {
                    Product temp = hashBaby.breakupLine(tempString);
                    hashBaby.put(temp.name, temp);
                }
                skip = false; // reset after first go through
            }
        } catch (Exception E) {
            System.out.println("ouch, file doesn't work");
        }
        int itemRating = singleInversionCount(recentPurchase);
        int[] ratings = relatedness(itemRating, inversionCounts(dataset, options));
        System.out.println("Ratings length: " + ratings.length);
        System.out.println("Products length: " + products.length);

        System.out.println("Ratings before");
        for (int i = 0; i < ratings.length; i++) {
            System.out.format(ratings[i] + ", ");
        }

        System.out.println("Products before");
        for (int i = 0; i < products.length; i++) {
            System.out.format(products[i] + ", ");
        }
        qsort(ratings, 0, ratings.length-1);
        /*
        for (int i = 0; i < ratings.length-1; i++) {
            for (int j = 0; j < ratings.length - i -1; j++) {
                if (ratings[j+1] == ratings[j]) {
                    if (findRating(options, products[j]) > findRating(options,products[j+1])) {
                        swap(ratings, j, j+1);
                    }
                }
            }
        }
        */

        System.out.println("Ratings after");
        for (int i = 0; i < ratings.length; i++) {
            System.out.format(ratings[i] + ", ");
        }

        System.out.println("Products after");
        for (int i = 0; i < products.length; i++) {
            System.out.format(products[i] + ", ");
        }

        return products;
    }


}

