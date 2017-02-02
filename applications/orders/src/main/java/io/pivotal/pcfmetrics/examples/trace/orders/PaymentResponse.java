package io.pivotal.pcfmetrics.examples.trace.orders;

import org.springframework.http.ResponseEntity;

public class PaymentResponse {
    private final boolean success;
    private final String message;

    public PaymentResponse(ResponseEntity<String> paymentResponse) {
        this.success = paymentResponse.getStatusCode().is2xxSuccessful();
        if (this.success) {
            this.message = "order processed successfully";
        } else {
            this.message = "unable to process order, please try again.";
        }
    }

    public PaymentResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PaymentResponse that = (PaymentResponse) o;

        if (success != that.success) return false;
        return message != null ? message.equals(that.message) : that.message == null;

    }

    @Override
    public int hashCode() {
        int result = (success ? 1 : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PaymentResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}
