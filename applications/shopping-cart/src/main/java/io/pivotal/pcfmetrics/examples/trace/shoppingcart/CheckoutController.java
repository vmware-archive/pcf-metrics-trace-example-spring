package io.pivotal.pcfmetrics.examples.trace.shoppingcart;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CheckoutController {
    private static Logger log = org.slf4j.LoggerFactory.getLogger(CheckoutController.class);

    @Autowired
    private OrdersConnector payments;

    @RequestMapping("/checkout")
    public ResponseEntity<ResponseMessage> tracePath() {
        log.info("/checkout called");

        OrderResponse response = payments.chargeChard();
        int status = response.isSuccess() ? 200 : 503;
        return ResponseEntity.status(status).body(new ResponseMessage(response.getMessage()));
    }
}
