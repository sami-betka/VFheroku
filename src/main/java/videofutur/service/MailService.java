package videofutur.service;

import java.security.Principal;

import videofutur.model.Mail;

public interface MailService {
    public void sendEmail(Mail mail, Principal principal);
}
