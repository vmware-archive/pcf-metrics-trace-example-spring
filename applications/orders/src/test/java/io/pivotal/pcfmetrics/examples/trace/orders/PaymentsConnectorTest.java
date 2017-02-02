package io.pivotal.pcfmetrics.examples.trace.orders;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class PaymentsConnectorTest {
    private PaymentsConnector subject;
    private String payments = "payments-host";

    @Mock
    private RestTemplate restTemplate = Mockito.mock(RestTemplate.class);

    @Before
    public void setUp() throws Exception {
        this.subject = new PaymentsConnector(payments, restTemplate);
    }

    @Test
    public void pingReturnsSuccess_whenSecondTraceSucceeds() throws Exception {
        doReturn(ResponseEntity.ok().build()).when(restTemplate).getForEntity("http://" + payments + "/charge-card", String.class);

        PaymentResponse response = this.subject.chargeChard();

        PaymentResponse expectedResponse = new PaymentResponse(true, "order processed successfully");
        assertThat(response, equalTo(expectedResponse));
        verify(restTemplate).getForEntity("http://" + payments + "/charge-card", String.class);
    }

    @Test
    public void pingReturnsFailure_whenSecondTraceFails() throws Exception {
        doReturn(ResponseEntity.badRequest().body("invalid content")).when(restTemplate).getForEntity("http://" + payments + "/charge-card", String.class);

        PaymentResponse response = this.subject.chargeChard();

        PaymentResponse expectedResponse = new PaymentResponse(false, "unable to process order, please try again.");
        assertThat(response, equalTo(expectedResponse));
        verify(restTemplate).getForEntity("http://" + payments + "/charge-card", String.class);
    }
}
