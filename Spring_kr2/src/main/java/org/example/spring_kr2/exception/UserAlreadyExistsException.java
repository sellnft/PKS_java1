package org.example.spring_kr2.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public static UserAlreadyExistsException byLogin(){
        return new UserAlreadyExistsException('Имя пользователя занято');
    }
    public static UserAlreadyExistsException byEmail(String email){
        return new UserAlreadyExistsException('Пользователь с email'+email+'уже существует');
    }
}
