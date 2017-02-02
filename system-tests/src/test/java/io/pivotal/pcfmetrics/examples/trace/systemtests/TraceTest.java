package io.pivotal.pcfmetrics.examples.trace.systemtests;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class TraceTest {

    private App shoppingCart;
    private App orders;
    private App payments;

    @Before
    public void setUp() throws Exception {
        Runtime.getRuntime().exec("mkdir tmp").waitFor();

        payments = new App("payments", new HashMap<>());

        Map<String, String> ordersEnv = new HashMap<>();
        ordersEnv.put("PAYMENTS_HOST", "localhost:" + payments.port());
        orders = new App("orders", ordersEnv);

        Map<String, String> shoppingCartEnv = new HashMap<>();
        shoppingCartEnv.put("ORDERS_HOST", "localhost:" + orders.port());
        shoppingCart = new App("shopping-cart", shoppingCartEnv);
    }

    @After
    public void tearDown() throws Exception {
        shoppingCart.destroy();
        orders.destroy();
        payments.destroy();

        Runtime.getRuntime().exec("rm -rf tmp").waitFor();
    }

    @Test(timeout = 120000)
    public void testTraces() throws Exception {
        waitForShoppingCartToStart();

        String uri = "http://localhost:" + shoppingCart.port() + "/checkout";
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

        assertEquals("{\"message\":\"order processed successfully\"}", response.getBody());

        Trace shoppingCartTrace = shoppingCart.getTrace();
        Trace ordersTrace = orders.getTrace();
        Trace paymentsTrace = payments.getTrace();

        assertThat(paymentsTrace.getTraceId()).isEqualTo(shoppingCartTrace.getTraceId());
        assertThat(ordersTrace.getTraceId()).isEqualTo(shoppingCartTrace.getTraceId());

        assertThat(paymentsTrace.getParentSpanId()).isEqualTo(ordersTrace.getSpanId());
        assertThat(ordersTrace.getParentSpanId()).isEqualTo(shoppingCartTrace.getSpanId());

        assertThat(shoppingCartTrace.getSpanId()).isNotEqualTo(ordersTrace.getSpanId());
        assertThat(ordersTrace.getSpanId()).isNotEqualTo(paymentsTrace.getSpanId());
    }

    public void waitForShoppingCartToStart() throws InterruptedException {
        for (; ; ) {
            Thread.sleep(1000);
            try (Socket ignored = new Socket("localhost", shoppingCart.port())) {
                return;
            } catch (IOException ex) {
            }
        }
    }

}
