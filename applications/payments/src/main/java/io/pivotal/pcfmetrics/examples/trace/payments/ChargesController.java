package io.pivotal.pcfmetrics.examples.trace.payments;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.*;

@RestController
public class ChargesController {
    private static Logger log = org.slf4j.LoggerFactory.getLogger(ChargesController.class);

    @RequestMapping("/charge-card")
    public ResponseMessage chargeCard() {
        log.info("/charge-card called");
        doCharge();

        return new ResponseMessage("card successfully charged!");
    }

    private void doCharge() {
        try {
            Thread.sleep((int) (100 * Math.random()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
