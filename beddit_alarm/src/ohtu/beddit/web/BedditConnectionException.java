package ohtu.beddit.web;

public class BedditConnectionException extends BedditException {

    public BedditConnectionException(){
    }

    public BedditConnectionException(String error) {
        super(error);
    }

}
