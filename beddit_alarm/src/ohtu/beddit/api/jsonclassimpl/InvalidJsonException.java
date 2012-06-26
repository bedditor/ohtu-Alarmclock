package ohtu.beddit.api.jsonclassimpl;

import ohtu.beddit.web.BedditException;

/*
 * Custom exception thrown when json data from api is found invalid and parsing fails.
 */
public class InvalidJsonException extends BedditException {

    public InvalidJsonException() {
        super();
    }

    public InvalidJsonException(String error) {
        super(error);
    }

}
