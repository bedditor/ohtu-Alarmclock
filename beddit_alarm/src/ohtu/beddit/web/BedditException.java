package ohtu.beddit.web;

public class BedditException extends Exception {

    protected BedditException() {
        super();
    }

    public BedditException(String error) {
        super(error);
    }
}
