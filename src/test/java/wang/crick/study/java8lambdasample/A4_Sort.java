package wang.crick.study.java8lambdasample;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 * Comparator
 *
 * @author wangyuheng
 * @date 2018-12-17 21:59
 */
public class A4_Sort {

    private List<Item> list;

    @Before
    public void setUp() {
        long id = 0L;
        list = Arrays.asList(
                new Item(++id, "a"),
                new Item(++id, "b"),
                new Item(++id, "b"),
                new Item(++id, "c"),
                new Item(++id, "d")
        );
        System.out.println("--------------------");
    }

    @Test
    public void should_return_min_item_by_sorted() {
        Optional<Item> first = list.stream().sorted(Comparator.comparingLong(Item::getId)).findAny();
        first.ifPresent(System.out::println);
        assertEquals(first.get().getUid(), "a");
    }

    @Test
    public void should_return_min_item_by_min() {
        Optional<Item> first = list.stream().min(Comparator.comparingLong(Item::getId));
        first.ifPresent(System.out::println);
        assertEquals(first.get().getUid(), "a");
    }

    @Data
    @AllArgsConstructor
    private static class Item {
        private Long id;
        private String uid;
    }

}
