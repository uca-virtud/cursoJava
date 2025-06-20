package es.uca.cursojava.springavanzado.shared.notification;

import org.springframework.stereotype.Component;

@Component
public class FakeNotificationService implements NotificationService {

    public void sendNotification(String to, String subject, String body) {
        // Aquí iría la lógica para enviar un correo electrónico
        System.out.println("Enviando correo a: " + to);
        System.out.println("  Asunto: " + subject);
        System.out.println("  Cuerpo: " + body);
    }

}
