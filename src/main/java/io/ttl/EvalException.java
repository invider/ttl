package io.ttl;

public class EvalException extends RuntimeException {

    public EvalException() {
    }

    public EvalException(String message) {
        super(message);
    }

    public EvalException(String message, Throwable cause) {
        super(message, cause);
    }

    public EvalException(Throwable cause) {
        super(cause);
    }

    public EvalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
