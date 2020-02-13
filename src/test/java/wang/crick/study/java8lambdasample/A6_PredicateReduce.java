package wang.crick.study.java8lambdasample;

import org.junit.Test;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * predicate util
 *
 * @author wangyuheng
 * @date 2018-12-17 21:59
 */
public class A6_PredicateReduce {

    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    @Test
    public void should_return_true_when_fuzzy_match() {
        Predicate<String> predicate = this.includedPathSelector("/c*");
        assertTrue(predicate.test("/config"));
    }

    @Test
    public void should_return_true_when_one_of_split_item_match() {
        Predicate<String> predicate = this.includedPathSelector("/config,/permission");
        assertTrue(predicate.test("/config"));
        assertTrue(predicate.test("/permission"));
    }

    @Test
    public void should_return_true_when_one_of_split_item_match_predicate_or() {
        Predicate<String> predicate1 = this.includedPathSelector("/permission");
        Predicate<String> predicate2 = this.includedPathSelector("/config");
        assertTrue(predicate1.or(predicate2).test("/config"));
        assertTrue(predicate1.or(predicate2).test("/permission"));
    }

    @Test
    public void should_return_false_when_one_of_split_item_match_predicate_and() {
        Predicate<String> predicate1 = this.includedPathSelector("/permission");
        Predicate<String> predicate2 = this.includedPathSelector("/config");
        assertFalse(predicate1.and(predicate2).test("/config"));
        assertFalse(predicate1.and(predicate2).test("/permission"));
    }


    @Test
    public void should_return_true_when_one_of_stream_match() {
        Predicate<String> predicate = this.includedPathSelector("/config,/permission");
        assertTrue(Stream.of("/config", "b").anyMatch(predicate));
    }

    @Test
    public void should_return_false_when_none_of_item_match() {
        Predicate<String> predicate = this.includedPathSelector("/config,/permission");
        assertFalse(predicate.test("/login"));
    }

    @Test
    public void should_return_false_when_urls_null(){
        Predicate<String> predicate = this.includedPathSelector("/config");
        assertFalse(predicate.test(null));
    }

    private Predicate<String> includedPathSelector(String urls) {
        if(StringUtils.isEmpty(urls)) {
            return x -> false;
        }
        return Pattern.compile(",").splitAsStream(urls)
                .filter(Objects::nonNull)
                .map(String::trim)
                .map(this::ant)
                .reduce(p -> false, Predicate::or);
    }

    private Predicate<String> ant(final String antPattern) {
        return input -> ANT_PATH_MATCHER.match(antPattern, Optional.ofNullable(input).orElse(""));
    }
}
