package org.vitaliistf.twowheels4u.exception;

public class UserAlreadyRegisteredException extends RuntimeException {

    public UserAlreadyRegisteredException(String message) {
        super(message);
    }

    public UserAlreadyRegisteredException(String message, Exception e) {
        super(message, e);
    }

}
