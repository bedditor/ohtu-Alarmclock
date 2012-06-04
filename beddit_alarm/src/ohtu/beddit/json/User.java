package ohtu.beddit.json;

/**
 * Created with IntelliJ IDEA.
 * User: joosakur
 * Date: 4.6.2012
 * Time: 11:11
 * To change this template use File | Settings | File Templates.
 */
public class User extends JsonObject {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
