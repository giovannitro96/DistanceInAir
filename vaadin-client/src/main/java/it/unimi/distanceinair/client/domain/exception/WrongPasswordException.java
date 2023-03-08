package it.unimi.distanceinair.client.domain.exception;

public class WrongPasswordException extends Exception {
    public WrongPasswordException() {
        super("Wrong password provided");
    }
}
