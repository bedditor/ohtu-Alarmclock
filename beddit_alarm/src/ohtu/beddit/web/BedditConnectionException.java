package ohtu.beddit.web;

/*
 *  Custom exception to be thrown by BedditConnector.  No added features.
 */

public class BedditConnectionException extends BedditException {

    public BedditConnectionException(String error) {
        super(error);
    }

}
