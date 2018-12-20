package wang.crick.study.java8lambdasample;

import lombok.Data;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.Assert.*;

/**
 * chain return notNull & fail-fast
 *
 * @author wangyuheng
 * @date 2018-12-17 21:59
 */
public class A9_ChainGet {

    private String name1;
    private String name2;
    private String name3;
    private String name4;
    private Integer age1;
    private Integer age2;

    private String getName1() {
        System.out.println("handler getName1");
        return name1;
    }

    private String getName2() {
        System.out.println("handler getName2");
        return name2;
    }

    private String getName3() {
        System.out.println("handler getName3");

        return name3;
    }

    private String getName4() {
        System.out.println("handler getName4");
        return name4;
    }

    private Integer getAge1() {
        System.out.println("handler age1");
        return age1;
    }

    private Integer getAge2() {
        System.out.println("handler age2");
        return age2;
    }

    private String getGender1(String gender){
        System.out.println("handler gender1");
        if ("gender1".equalsIgnoreCase(gender)) {
            return gender;
        } else {
            return null;
        }
    }

    private String getGender2(String gender){
        System.out.println("handler gender2");
        if ("gender2".equalsIgnoreCase(gender)) {
            return gender;
        } else {
            return null;
        }
    }

    @Before
    public void setUp() {
        System.out.println("--------------------");
        name1 = "name1";
        name2 = "name2";
        name3 = "name3";
        name4 = "name4";
        age1 = 1;
        age2 = 2;
    }

    @Test
    public void should_get_name3_if_name1_and_name2_is_null() {
        name1 = null;
        name2 = null;
        Optional<String> optionalName = getOptionalName();
        assertTrue(optionalName.isPresent());
        assertEquals(name3, optionalName.get());
        optionalName.ifPresent(System.out::println);
    }

    @Test
    public void should_get_empty_if_all_name_is_null() {
        name1 = null;
        name2 = null;
        name3 = null;
        name4 = null;
        Optional<String> optionalName = getOptionalName();
        assertFalse(optionalName.isPresent());
    }

    private Optional<String> getOptionalName() {
        Stream<Supplier<String>> nameStream = Stream.of(
                this::getName1,
                this::getName2,
                this::getName3,
                this::getName4
        );
        return nameStream
                .map(Supplier::get)
                .filter(Objects::nonNull)
                .findAny();
    }

    @Test
    public void should_get_gender2_if_in_gender2(){
        String in = "gender2";
        Optional<String> gender = this.getOptionalGender(in);
        assertTrue(gender.isPresent());
        assertEquals(in, gender.get());
    }

    private Optional<String> getOptionalGender(String gender) {
        Stream<Supplier<String>> nameStream = Stream.of(
                ()->this.getGender1(gender),
                ()->this.getGender2(gender)
        );
        return nameStream
                .map(Supplier::get)
                .filter(Objects::nonNull)
                .findAny();
    }


    @Test
    public void should_get_user_with_name4_and_age2_when_others_is_null() {
        name1 = null;
        name2 = null;
        name3 = null;
        age1 = null;

        Optional<User> optionalUser = fillBuild();
        assertTrue(optionalUser.isPresent());
        assertEquals(name4, optionalUser.get().getName());
        assertEquals(age2, optionalUser.get().getAge());
        optionalUser.ifPresent(System.out::println);

    }

    @Test
    public void should_get_empty_user_if_all_age_is_null() {
        age1 = null;
        age2 = null;
        Optional<User> optionalUser = fillBuild();
        assertFalse(optionalUser.isPresent());
        optionalUser.ifPresent(System.out::println);
    }

    @Deprecated
    private Optional<User> fillBuild() {
        Stream<String> nameStreamWrapper = buildStream(Arrays.asList(this::getName1, this::getName2, this::getName3, this::getName4));
        Stream<Integer> ageStreamWrapper = buildStream(Arrays.asList(this::getAge1, this::getAge2));

        BuildUser buildUser = (name, age) -> Stream.of(new User(name, age));

        return ageStreamWrapper.flatMap(age ->
                nameStreamWrapper.flatMap(name ->
                        buildUser.build(name, age)
                )
        ).findAny();
    }

    private <T> Stream<T> buildStream(List<Supplier<T>> suppliers) {
        return suppliers.stream()
                .map(Supplier::get)
                .filter(Objects::nonNull);
    }

    @FunctionalInterface
    private interface BuildUser {
        Stream<User> build(String name, Integer age);
    }

    @Data
    private static class User {
        private String name;
        private Integer age;

        User(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

    }

}
