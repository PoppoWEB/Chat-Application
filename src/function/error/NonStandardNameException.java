package function.error;

public class NonStandardNameException extends Exception {
    public NonStandardNameException() {
        super();
    }

    public NonStandardNameException(String msg) {
        super(msg);
    }
}
