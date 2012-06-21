package ohtu.beddit.api.jsonclassimpl;

/**
 * Created with IntelliJ IDEA.
 * User: joosakur
 * Date: 5.6.2012
 * Time: 10:47
 * To change this template use File | Settings | File Templates.
 */
public class UserData {
    private User[] users;

    public String getUsername() throws InvalidJsonException {
        if(users[0].getUsername() == null)
            throw new InvalidJsonException("beddit says null");
        return users[0].getUsername();
    }

    public String getFirstName() throws InvalidJsonException {
        if(users[0].getUsername() == null)
            throw new InvalidJsonException("beddit says null");
        return users[0].getFirst_name();
    }

    public String getLastName() throws InvalidJsonException {
        if(users[0].getUsername() == null)
            throw new InvalidJsonException("beddit says null");
        return users[0].getLast_name();
    }


}
