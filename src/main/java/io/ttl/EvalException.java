package io.ttl;

public class EvalException extends RuntimeException {

    private String src;

    public EvalException() {
    }

    public EvalException(String message) {
        super(message);
    }

    public EvalException(String message, String src) {
        super(message);
        this.src = src;
    }

    public EvalException(String message, Throwable cause) {
        super(message, cause);
    }

    public EvalException(String message, String src, Throwable cause) {
        super(message, cause);
        this.src = src;
    }

    public EvalException(Throwable cause) {
        super(cause);
    }

    public EvalException(Throwable cause, String src) {
        super(cause);
        this.src = src;
    }

    public EvalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
