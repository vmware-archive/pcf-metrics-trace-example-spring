package io.pivotal.pcfmetrics.examples.trace.shoppingcart;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class OrdersConnectorTest {
    private OrdersConnector subject;
    private String orders = "orders-host";

    @Mock
    private RestTemplate restTemplate = Mockito.mock(RestTemplate.class);

    @Before
    public void setUp() throws Exception {
        this.subject = new OrdersConnector(orders, restTemplate);
    }

    @Test
    public void pingReturnsSuccess_whenSecondTraceSucceeds() throws Exception {
        doReturn(ResponseEntity.ok().build()).when(restTemplate).getForEntity("http://" + orders + "/process-order", String.class);

        OrderResponse response = this.subject.chargeChard();

        OrderResponse expectedResponse = new OrderResponse(true, "order processed successfully");
        assertThat(response, equalTo(expectedResponse));
        verify(restTemplate).getForEntity("http://" + orders + "/process-order", String.class);
    }

    @Test
    public void pingReturnsFailure_whenSecondTraceFails() throws Exception {
        doReturn(ResponseEntity.badRequest().body("invalid content")).when(restTemplate).getForEntity("http://" + orders + "/process-order", String.class);

        OrderResponse response = this.subject.chargeChard();

        OrderResponse expectedResponse = new OrderResponse(false, "unable to process order, please try again.");
        assertThat(response, equalTo(expectedResponse));
        verify(restTemplate).getForEntity("http://" + orders + "/process-order", String.class);
    }
}
