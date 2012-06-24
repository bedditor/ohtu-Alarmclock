package ohtu.beddit.web;

/**
 * Exception to be thrown if Beddit sleep data was not found.
 */
public class NoSleepDataException extends BedditException {

    public NoSleepDataException(String error) {
        super(error);
    }

}
