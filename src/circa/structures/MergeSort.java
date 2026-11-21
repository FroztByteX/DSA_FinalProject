package circa.structures;

import java.util.Comparator;

public final class MergeSort {
    private MergeSort() {
    }

    public static <T> void sort(T[] array, Comparator<? super T> comparator) {
        if (array.length < 2) {
            return;
        }
        sort(array, array.clone(), 0, array.length - 1, comparator);
    }

    private static <T> void sort(T[] array, T[] temp, int low, int high, Comparator<? super T> comparator) {
        if (low >= high) {
            return;
        }
        int mid = (low + high) >>> 1;
        sort(array, temp, low, mid, comparator);
        sort(array, temp, mid + 1, high, comparator);
        int i = low;
        int j = mid + 1;
        int k = low;
        while (i <= mid && j <= high) {
            temp[k++] = comparator.compare(array[i], array[j]) <= 0 ? array[i++] : array[j++];
        }
        while (i <= mid) {
            temp[k++] = array[i++];
        }
        while (j <= high) {
            temp[k++] = array[j++];
        }
        for (k = low; k <= high; k++) {
            array[k] = temp[k];
        }
    }
}
