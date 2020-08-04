package pl.zajac.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.zajac.services.MailService;

import javax.mail.MessagingException;

@RestController
public class MailController {
    private MailService mailService;

    @Autowired
    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @GetMapping(value = "/api/generateResetPasswordMail")
    public ResponseEntity<Void> generateResetPasswordMail(@RequestParam String email) throws MessagingException {
        mailService.generateResetPasswordMail(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
