package pl.zajac.services.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.zajac.model.entities.User;
import pl.zajac.model.repository.UserRepository;
import pl.zajac.model.security.jwt.JwtGenerate;
import pl.zajac.services.MailService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Service
@EnableAsync
public class MailServiceImp implements MailService {
    private JavaMailSender javaMailSender;
    private TemplateEngine templateEngine;
    private UserRepository userRepository;
    private JwtGenerate jwtGenerate;

    @Autowired
    public MailServiceImp(JavaMailSender javaMailSender, TemplateEngine templateEngine, UserRepository userRepository, JwtGenerate jwtGenerate) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.userRepository = userRepository;
        this.jwtGenerate = jwtGenerate;
    }

    @Async
    @Override
    public void generateResetPasswordMail(String email) throws MessagingException {
        //If there's no user with this mail still return status 200 nobody need to know if it's actually not exist :)
        Optional<User> userOptional = userRepository.findUserByEmail(email);
        if(userOptional.isPresent()){
            String token = jwtGenerate.generateForgotPasswordToken(email);
            sendEmail(email, token);
        }
    }
    private void sendEmail(String email, String token) throws MessagingException {
        Map<String, Object> variables = new HashMap<>();
        variables.put("token",token);
        final String templateFileName = "email";
        String output = this.templateEngine.process(templateFileName, new Context(Locale.getDefault(), variables));
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setText(output, true);
        helper.setTo(email);
        helper.setSubject("Reset Password Request");
        helper.setFrom("zajac5569@gmail.com");

        javaMailSender.send(mimeMessage);
    }
}
