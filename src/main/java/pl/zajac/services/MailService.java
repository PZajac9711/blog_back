package pl.zajac.services;

import javax.mail.MessagingException;

public interface MailService {
    void generateResetPasswordMail(String email) throws MessagingException;
}
