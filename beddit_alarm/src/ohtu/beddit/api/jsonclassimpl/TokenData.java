package ohtu.beddit.api.jsonclassimpl;

public class TokenData {
    private String access_token;

    public String getToken() throws InvalidJsonException {
        if(access_token == null)
            throw new InvalidJsonException("beddit says null");
        return access_token;
    }
}
