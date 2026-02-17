package uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.modele.Notification;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.repository.NotificationRepository;

import java.util.List;

@Service
@Transactional
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    // Créer une notification
    public void creerNotification(String message) {
        Notification notification = new Notification(message);
        notificationRepository.save(notification);
    }

    // Récupérer les notifications non lues
    public List<Notification> getNotificationsNonLues() {
        return notificationRepository.findByLueFalseOrderByDateCreationDesc();
    }

    // Marquer une notification comme lue
    public void marquerCommeLue(Long id) {
        Notification notification = notificationRepository.findById(id).orElse(null);
        if (notification != null) {
            notification.setLue(true);
            notificationRepository.save(notification);
        }
    }
}