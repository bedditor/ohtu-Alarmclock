package ohtu.beddit.web;

public class BedditException extends Exception {

    String error;

    public BedditException() {
    }

    public BedditException(String error) {
        this.error = error;
    }

    @Override
    public String getMessage() {
        if(error == null) {
            return super.getMessage();
        }
        else {
            return error + ": " + super.getMessage();
        }
    }
}
