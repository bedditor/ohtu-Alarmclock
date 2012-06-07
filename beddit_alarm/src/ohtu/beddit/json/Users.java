package ohtu.beddit.json;

/**
 * Created with IntelliJ IDEA.
 * User: joosakur
 * Date: 5.6.2012
 * Time: 10:47
 * To change this template use File | Settings | File Templates.
 */
public class Users extends JsonObject {
    private User[] users;

    public int getUsercount(){
        return users.length;
    }

    public String getUsername(int userIndex){
        return users[userIndex].getUsername();
    }

    public String getFirstName(int userIndex){
        return users[userIndex].getFirst_name();
    }

    public String getLastName(int userIndex){
        return users[userIndex].getLast_name();
    }



}
