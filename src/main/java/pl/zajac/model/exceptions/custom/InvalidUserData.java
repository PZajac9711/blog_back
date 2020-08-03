package pl.zajac.model.exceptions.custom;

public class InvalidUserData extends RuntimeException{
    public InvalidUserData(String message) {
        super(message);
    }
}
