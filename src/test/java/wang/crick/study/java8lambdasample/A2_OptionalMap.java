package wang.crick.study.java8lambdasample;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * optional if nested
 *
 * @author wangyuheng
 * @date 2018-12-17 21:59
 */
public class A2_OptionalMap {

    private String username = "zhangsan";
    private String password = "123456";
    private UserPO userPO;

    @Before
    public void setUp() {
        username = "zhangsan";
        password = "123456";
        userPO = UserPO.builder()
                .username(username)
                .password(password)
                .build();
        System.out.println("--------------------");
    }

    @Test
    public void login_fail_when_incorrect_password() {
        password = "1";
        assertFalse(login_check_by_if(username, password).isPresent());
        assertFalse(login_check_by_map(username, password).isPresent());
        System.out.println(login_check_by_if(username, password));
        System.out.println(login_check_by_map(username, password));
    }

    @Test
    public void login_success_when_incorrect_password() {
        assertTrue(login_check_by_if(username, password).isPresent());
        assertTrue(login_check_by_map(username, password).isPresent());
        System.out.println(login_check_by_if(username, password));
        System.out.println(login_check_by_map(username, password));
    }

    private Optional<User> login_check_by_if(String username, String password) {
        Optional<UserPO> optionalUserPO = findByUsername(username);
        if (optionalUserPO.isPresent()) {
            UserPO po = optionalUserPO.get();
            if (password.equals(po.getPassword())) {
                return Optional.of(convert(po));
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    private Optional<User> login_check_by_map(String username, String password) {
        return findByUsername(username)
                .filter(po -> Objects.equals(po.getPassword(), password))
                .map(this::convert);
    }


    private Optional<UserPO> findByUsername(String username) {
        return Optional.ofNullable(userPO);
    }

    private User convert(UserPO po) {
        return User.builder()
                .username(po.getUsername())
                .password(po.getPassword())
                .build();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    private static class UserPO {
        private String username;
        private String password;
    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    private static class User {
        private String username;
        private String password;
    }


}
