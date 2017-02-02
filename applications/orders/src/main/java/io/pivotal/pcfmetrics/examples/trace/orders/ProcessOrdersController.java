package io.pivotal.pcfmetrics.examples.trace.orders;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProcessOrdersController {
    private static Logger log = org.slf4j.LoggerFactory.getLogger(ProcessOrdersController.class);

    @Autowired
    private PaymentsConnector payments;

    @RequestMapping("/process-order")
    public ResponseEntity<ResponseMessage> tracePath() {
        log.info("/process-order called");

        PaymentResponse response = payments.chargeChard();
        int status = response.isSuccess() ? 200 : 503;
        return ResponseEntity.status(status).body(new ResponseMessage(response.getMessage()));
    }
}
