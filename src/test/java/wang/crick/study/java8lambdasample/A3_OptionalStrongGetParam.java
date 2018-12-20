package wang.crick.study.java8lambdasample;

import lombok.Data;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Optional;

import static org.junit.Assert.*;

/**
 * get().get().get()
 *
 * @author wangyuheng
 * @date 2018-12-17 21:59
 */
public class A3_OptionalStrongGetParam {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        System.out.println("--------------------");
    }


    @Test
    public void get_treasure_npe_when_session_null() {
        Exchange exchange = new Exchange();

        thrown.expect(NullPointerException.class);
        exchange.getSession().getRequest().getTreasure();
    }

    @Test
    public void get_null_treasure() {
        Exchange exchange = new Exchange();

        String result1 = getTreasureIf(exchange);
        Optional<String> result2 = getTreasureOptionalMap(exchange);
        assertNull(result1);
        assertFalse(result2.isPresent());
    }

    @Test
    public void get_treasure() {
        String treasure = "treasure";
        Request request = new Request();
        Session session = new Session();
        Exchange exchange = new Exchange();
        request.setTreasure(treasure);
        session.setRequest(request);
        exchange.setSession(session);

        String result1 = getTreasureIf(exchange);
        Optional<String> result2 = getTreasureOptionalMap(exchange);
        assertEquals(treasure, result1);
        assertEquals(treasure, result2.get());

    }

    private String getTreasureIf(Exchange exchange) {
        if (null != exchange) {
            Session session = exchange.getSession();
            if (null != session) {
                Request request = session.getRequest();
                if (null != request) {
                    return request.getTreasure();
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private Optional<String> getTreasureOptionalMap(Exchange exchange) {
        return Optional.ofNullable(exchange)
                .map(Exchange::getSession)
                .map(Session::getRequest)
                .map(Request::getTreasure);
    }

    @Data
    private static class Exchange {
        private Session session;
    }

    @Data
    private static class Session {
        private Request request;
    }

    @Data
    private static class Request {
        private String treasure;
    }


}
