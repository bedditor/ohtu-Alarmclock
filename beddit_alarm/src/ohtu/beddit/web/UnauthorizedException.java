package ohtu.beddit.web;

/**
 * Exception to be thrown if API call was trying to fetch content without correct authorization.
 */
public class UnauthorizedException extends BedditException {

    public UnauthorizedException(String error) {
        super(error);
    }

}
