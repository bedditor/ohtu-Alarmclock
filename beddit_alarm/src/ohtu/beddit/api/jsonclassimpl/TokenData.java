package ohtu.beddit.api.jsonclassimpl;

/**
 * Encapsulates access token json data as an object.
 */
public class TokenData {
    private String access_token;

    public String getToken() throws InvalidJsonException {
        if(access_token == null)
            throw new InvalidJsonException("beddit says null");
        return access_token;
    }
}
