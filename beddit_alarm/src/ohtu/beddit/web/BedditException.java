package ohtu.beddit.web;

/*
 * Custom exception to be thrown, if Beddit API sends invalid information.
 * This is a super class to other custom beddit exceptions.
 */
public class BedditException extends Exception {

    protected BedditException() {
        super();
    }

    public BedditException(String error) {
        super(error);
    }
}
