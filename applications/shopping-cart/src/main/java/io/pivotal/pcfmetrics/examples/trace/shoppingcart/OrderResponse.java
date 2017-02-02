package io.pivotal.pcfmetrics.examples.trace.shoppingcart;

import org.springframework.http.ResponseEntity;

public class OrderResponse {
    private final boolean success;
    private final String message;

    public OrderResponse(ResponseEntity<String> orderResponse) {
        this.success = orderResponse.getStatusCode().is2xxSuccessful();
        if (this.success) {
            this.message = "order processed successfully";
        } else {
            this.message = "unable to process order, please try again.";
        }
    }

    public OrderResponse(boolean success, String message) {
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

        OrderResponse that = (OrderResponse) o;

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
        return "OrderResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}
