package javafxfrontend.service;

public class ValidateException extends RuntimeException {

    public static ValidateException byLogin(){
        return new UserAlreadyExistsException('Имя пользователя занято');
    }
    public static ValidateException byEmail(String email){
        return new UserAlreadyExistsException('Пользователь с email'+email+'уже существует');
    }
}

public class RegistrationService {
    public
}
