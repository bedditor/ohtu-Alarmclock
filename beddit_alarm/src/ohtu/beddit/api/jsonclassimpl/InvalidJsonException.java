package ohtu.beddit.api.jsonclassimpl;

import ohtu.beddit.web.BedditException;

public class InvalidJsonException extends BedditException {

    public InvalidJsonException() {
        super();
    }

    public InvalidJsonException(String error) {
        super(error);
    }

}
