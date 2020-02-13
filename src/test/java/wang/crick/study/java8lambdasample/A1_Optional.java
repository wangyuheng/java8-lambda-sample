package wang.crick.study.java8lambdasample;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * parse return
 * 1. default?
 * 2. illegalException?
 * 3. null?
 *
 * @author wangyuheng
 * @date 2018-12-17 21:59
 */
public class A1_Optional {

    enum BooleanEnum {
        // 封装boolean
        TRUE(1, "是"),
        FALSE(0, "否");


        private Integer key;
        private String label;

        BooleanEnum(Integer key, String label) {
            this.key = key;
            this.label = label;
        }

        public Integer getKey() {
            return key;
        }

        public String getLabel() {
            return label;
        }

        public static Optional<BooleanEnum> parse(Integer key) {
            return Arrays.stream(values()).filter(i -> i.getKey().equals(key)).findAny();
        }

        public static BooleanEnum parse(boolean bool) {
            return bool ? TRUE : FALSE;
        }

        public static boolean exist(int key) {
            return Arrays.stream(values()).anyMatch(i -> i.getKey() == key);
        }
    }

    @Before
    public void setUp() {
        System.out.println("--------------------");
    }

    @Test
    public void parse_true_optional() {
        Optional<BooleanEnum> optionalBooleanEnum = BooleanEnum.parse(1);
        assertEquals(BooleanEnum.TRUE, optionalBooleanEnum.get());
        BooleanEnum.parse(1).ifPresent(System.out::println);
    }

    @Test
    public void parse_false_optional() {
        Optional<BooleanEnum> optionalBooleanEnum = BooleanEnum.parse(0);
        assertEquals(BooleanEnum.FALSE, optionalBooleanEnum.get());
        BooleanEnum.parse(0).ifPresent(System.out::println);
    }

    @Test
    public void parse_empty_optional() {
        Optional<BooleanEnum> optionalBooleanEnum = BooleanEnum.parse(2);
        assertFalse(optionalBooleanEnum.isPresent());
        BooleanEnum.parse(2).ifPresent(System.out::println);
    }

}
