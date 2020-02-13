package wang.crick.study.java8lambdasample;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * concurrent & paged
 *
 * @author wangyuheng
 * @date 2018-12-17 21:59
 */
public class A8_DivideHandler {

    @Test
    public void divide_print() {
        new PrintDivideHandler().batchPrint(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k","l","m","n"));
    }

    @Test
    public void thread_nums_divide_print() {
        new PrintDivideHandler().batchPrint(4, Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k","l","m","n","a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k","l","m","n","a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k","l","m","n","a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k","l","m","n"));
    }


    private interface DivideHandler {
        default int getPeriod() {
            return 10;
        }

        default <T> void divideBatchHandler(List<T> dataList, Consumer<List<T>> consumer) {
            Optional.ofNullable(dataList).ifPresent(list ->
                    IntStream.range(0, list.size())
                            .mapToObj(i -> new AbstractMap.SimpleImmutableEntry<>(i, list.get(i)))
                            .collect(Collectors.groupingBy(
                                    e -> e.getKey() / getPeriod(),
                                    Collectors.mapping(Map.Entry::getValue, Collectors.toList())))
                            .values()
                            .parallelStream()
                            .forEach(consumer)
            );
        }

        default <T> void divideBatchHandler(int threadNum, List<T> dataList, Consumer<List<T>> consumer) {
            try {
                new ForkJoinPool(threadNum).submit(() -> Optional.ofNullable(dataList).ifPresent(list ->
                        IntStream.range(0, list.size())
                                .mapToObj(i -> new AbstractMap.SimpleImmutableEntry<>(i, list.get(i)))
                                .collect(Collectors.groupingBy(
                                        e -> e.getKey() / getPeriod(),
                                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())))
                                .values()
                                .parallelStream()
                                .forEach(consumer)
                )).get();
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupted();
            }
        }
    }

    class PrintDivideHandler implements DivideHandler {

        @Override
        public int getPeriod() {
            return 2;
        }

        private void batchPrint(List<String> dataList) {
            divideBatchHandler(dataList, System.out::println);
        }

        private void batchPrint(int threadNum, List<String> dataList) {
            divideBatchHandler(threadNum, dataList, data -> System.out.println(Thread.currentThread().getName() + " : " + data));
        }
    }
}