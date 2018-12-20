package wang.crick.study.java8lambdasample;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * map & group & partition
 *
 * @author wangyuheng
 * @date 2018-12-17 21:59
 */
public class A7_MapCollector {

    private List<Item> list;

    @Before
    public void setUp() {
        long id = 0L;
        list = Arrays.asList(new Item(++id, "a"),
                new Item(++id, "a"),
                new Item(++id, "b"),
                new Item(++id, "b"),
                new Item(++id, "c"),
                new Item(++id, "d")
        );
        System.out.println("--------------------");
    }

    @Test
    public void map_item_by_id() {
        Map<Long, Item> idMap = list.stream().collect(Collectors.toMap(Item::getId, Function.identity()));
        idMap.forEach((k, v) -> System.out.println(String.format("key = %s : value = %s", k, v)));
    }

    @Test
    public void map_item_arr_by_uid() {
        Map<String, List<Item>> uidMap = list.stream().collect(Collectors.groupingBy(Item::getUid, Collectors.toList()));
        uidMap.forEach((k, v) -> System.out.println(String.format("key = %s : value = %s", k, v)));
    }

    @Test
    public void map_uid_arr_by_uid() {
        Map<String, List<Long>> uidMapString = list.stream().collect(Collectors.groupingBy(Item::getUid, Collectors.mapping(Item::getId, Collectors.toList())));
        uidMapString.forEach((k, v) -> System.out.println(String.format("key = %s : value = %s", k, v)));
    }

    @Test
    public void map_item_count_by_uid() {
        Map<String, Long> uidMapCount = list.stream().collect(Collectors.groupingBy(Item::getUid, Collectors.mapping(Function.identity(), Collectors.counting())));
        uidMapCount.forEach((k, v) -> System.out.println(String.format("key = %s : value = %s", k, v)));
    }

    @Test
    public void partition_uid_item_map_by_uid() {
        Map<Boolean, Map<String, Long>> uidMapCountPartition = list.stream().collect(Collectors.partitioningBy(key -> key.getId() > 2, Collectors.groupingBy(Item::getUid, Collectors.mapping(Item::getId, Collectors.counting()))));
        uidMapCountPartition.forEach((k, v) -> System.out.println(String.format("key = %s : value = %s", k, v)));
    }

    @Data
    @AllArgsConstructor
    private static class Item {
        private Long id;
        private String uid;
    }

}
