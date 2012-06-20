package ohtu.beddit.api.jsonclassimpl;

import ohtu.beddit.web.BedditException;

/**
 * Created with IntelliJ IDEA.
 * User: joosakur
 * Date: 14.6.2012
 * Time: 14:48
 * To change this template use File | Settings | File Templates.
 */
public class InvalidJsonException extends BedditException {

    public InvalidJsonException() {
        super();
    }

    public InvalidJsonException(String error) {
        super(error);
    }

}
