package io.pivotal.pcfmetrics.examples.trace.shoppingcart;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class OrdersConnector {
    private static Logger log = getLogger(OrdersConnector.class);
    private RestTemplate restTemplate;
    private String ordersHost;

    @Autowired
    public OrdersConnector(
            @Value("${orders.host}") String ordersHost,
            RestTemplate restTemplate
    ) {
        this.ordersHost = ordersHost;
        this.restTemplate = restTemplate;
    }

    public OrderResponse chargeChard() {
        ResponseEntity<String> orderResponse = request(ordersHost, "/process-order");
        return new OrderResponse(orderResponse);
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
