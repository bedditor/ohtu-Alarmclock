package ohtu.beddit.api.jsonclassimpl;

/**
 * Created with IntelliJ IDEA.
 * User: joosakur
 * Date: 5.6.2012
 * Time: 10:47
 * To change this template use File | Settings | File Templates.
 */
class UserData {
    private User[] users;

    public String getUsername() {
        return users[0].getUsername();
    }

    public String getFirstName() {
        return users[0].getFirst_name();
    }

    public String getLastName() {
        return users[0].getLast_name();
    }


}
