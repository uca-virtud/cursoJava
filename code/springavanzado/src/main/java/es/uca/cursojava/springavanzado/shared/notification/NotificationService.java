package es.uca.cursojava.springavanzado.shared.notification;

public interface NotificationService {
    void sendNotification(String to, String subject, String body);
}
