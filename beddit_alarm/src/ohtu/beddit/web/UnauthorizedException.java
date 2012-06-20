package ohtu.beddit.web;

/**
 * Created with IntelliJ IDEA.
 * User: joosakur
 * Date: 18.6.2012
 * Time: 12:58
 * To change this template use File | Settings | File Templates.
 */
public class UnauthorizedException extends BedditException {

    public UnauthorizedException(){
        super();
    }

    public UnauthorizedException(String error) {
        super(error);
    }

}
