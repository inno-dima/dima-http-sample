package src.main.java.com.zextras.httpserver.exception;

@SuppressWarnings("unused")
public class InternalServerException extends RuntimeException {
    public InternalServerException() {
        super();
    }

    public InternalServerException(String message) {
        super(message);
    }

    public InternalServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalServerException(Throwable cause) {
        super(cause);
    }

    protected InternalServerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
