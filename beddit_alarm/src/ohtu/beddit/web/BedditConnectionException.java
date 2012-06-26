package ohtu.beddit.web;

/*
 *  Custom exception thrown when connection to beddit server fails somehow.
 */
public class BedditConnectionException extends BedditException {

    public BedditConnectionException(String error) {
        super(error);
    }

}
