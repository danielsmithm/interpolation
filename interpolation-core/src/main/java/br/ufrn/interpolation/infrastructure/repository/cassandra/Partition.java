package br.ufrn.interpolation.infrastructure.repository.cassandra;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class Partition<T> extends AbstractList<List<T>> {

    private final List<T> list;
    private final int chunkSize;


    /**
     * Implementation of partition.
     *
     * Source:
     *
     * https://e.printstacktrace.blog/divide-a-list-to-lists-of-n-size-in-Java-8/
     * @param list
     * @param chunkSize
     * @param <T>
     * @return
     */
    public static <T> Partition<T> ofSize(Collection<T> list, int chunkSize) {
        return new Partition<>(list, chunkSize);
    }

    public Partition(Collection<T> list, int chunkSize) {
        this.list = new ArrayList<>(list);
        this.chunkSize = chunkSize;
    }


    @Override
    public List<T> get(int index) {
        int start = index * chunkSize;
        int end = Math.min(start + chunkSize, list.size());

        if (start > end) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of the list range <0," + (size() - 1) + ">");
        }

        return new ArrayList<>(list.subList(start, end));
    }

    @Override
    public int size() {
        return (int) Math.ceil((double) list.size() / (double) chunkSize);
    }
}
