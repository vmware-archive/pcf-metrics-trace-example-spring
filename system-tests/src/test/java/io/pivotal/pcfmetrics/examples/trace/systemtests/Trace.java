package io.pivotal.pcfmetrics.examples.trace.systemtests;

/**
 * Created by pivotal on 2/1/17.
 */
public class Trace {
    private final String traceId;
    private final String spanId;
    private final String parentSpanId;

    public Trace(String traceId, String spanId, String parentSpanId) {
        this.traceId = traceId;
        this.spanId = spanId;
        this.parentSpanId = parentSpanId;
    }

    public String getTraceId() {
        return traceId;
    }

    public String getSpanId() {
        return spanId;
    }

    public String getParentSpanId() {
        return parentSpanId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Trace trace = (Trace) o;

        if (traceId != null ? !traceId.equals(trace.traceId) : trace.traceId != null) return false;
        if (spanId != null ? !spanId.equals(trace.spanId) : trace.spanId != null) return false;
        return parentSpanId != null ? parentSpanId.equals(trace.parentSpanId) : trace.parentSpanId == null;
    }

    @Override
    public int hashCode() {
        int result = traceId != null ? traceId.hashCode() : 0;
        result = 31 * result + (spanId != null ? spanId.hashCode() : 0);
        result = 31 * result + (parentSpanId != null ? parentSpanId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Trace{" +
                "traceId='" + traceId + '\'' +
                ", spanId='" + spanId + '\'' +
                ", parentSpanId='" + parentSpanId + '\'' +
                '}';
    }
}
