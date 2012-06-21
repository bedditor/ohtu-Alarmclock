package ohtu.beddit.api.jsonclassimpl;

/**
 * Created with IntelliJ IDEA.
 * User: joosakur
 * Date: 21.6.2012
 * Time: 11:50
 * To change this template use File | Settings | File Templates.
 */
public class TokenData {
    private String access_token;

    public String getToken() throws InvalidJsonException {
        if(access_token == null)
            throw new InvalidJsonException("beddit says null");
        return access_token;
    }
}
