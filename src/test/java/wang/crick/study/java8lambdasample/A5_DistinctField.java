package wang.crick.study.java8lambdasample;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * predicate distinct
 *
 * @author wangyuheng
 * @date 2018-12-17 21:59
 */
public class A5_DistinctField {

    private List<User> userList = new ArrayList<>();

    @Before
    public void setUp() {
        long id = 0L;
        userList.add(new User(++id, "张三", 15));
        userList.add(new User(++id, "张三", 16));
        userList.add(new User(++id, "李四", 16));
        userList.add(new User(++id, "李四", 17));
        System.out.println("--------------------");
    }

    @Test
    public void should_return_3_because_distinct_by_age() {
        userList = userList.stream()
                .filter(distinctByKey(User::getAge))
                .collect(Collectors.toList());
        userList.forEach(System.out::println);
        assertEquals(3, userList.size());
    }

    @Test
    public void should_return_2_because_distinct_by_age() {
        userList = userList.stream()
                .filter(distinctByKey(User::getName))
                .collect(Collectors.toList());
        userList.forEach(System.out::println);
        assertEquals(2, userList.size());
    }

    private static <T, R> Predicate<T> distinctByKey(Function<T, R> keyExtractor) {
        Set<R> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    @Data
    @AllArgsConstructor
    private static class User implements Serializable {
        private Long id;
        private String name;
        private Integer age;
    }
}
