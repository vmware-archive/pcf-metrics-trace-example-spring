package io.pivotal.pcfmetrics.examples.trace.orders;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class PaymentsConnector {
    private static Logger log = getLogger(PaymentsConnector.class);
    private RestTemplate restTemplate;
    private String paymentsHost;

    @Autowired
    public PaymentsConnector(
            @Value("${payments.host}") String paymentsHost,
            RestTemplate restTemplate
    ) {
        this.paymentsHost = paymentsHost;
        this.restTemplate = restTemplate;
    }

    public PaymentResponse chargeChard() {
        ResponseEntity<String> paymentResponse = request(paymentsHost, "/charge-card");
        return new PaymentResponse(paymentResponse);
    }

    private ResponseEntity<String> request(String host, String path) {
        ResponseEntity<String> response;

        final String uri = "http://" + host + path;
        log.info("sending request to " + uri);
        try {
            response = this.restTemplate.getForEntity(uri, String.class);
        } catch(RestClientException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        log.info("finished request to " + uri);
        return response;
    }
}
