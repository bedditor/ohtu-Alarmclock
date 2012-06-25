package ohtu.beddit.api.jsonclassimpl;

/**
 * Encapsulates json data of a single user as an object.
 */
class User {
    private String username;
    private String url;
    private String first_name;
    private String last_name;
    private String email;

    public String getUsername() {
        return username;
    }

    public String getUrl() {
        return url;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }
}
