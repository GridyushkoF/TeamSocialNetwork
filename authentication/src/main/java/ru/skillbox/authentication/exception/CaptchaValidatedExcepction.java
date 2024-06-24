package ru.skillbox.authentication.exception;

public class CaptchaValidatedExcepction extends RuntimeException{
    public CaptchaValidatedExcepction(String message){
        super(message);
    }
}
