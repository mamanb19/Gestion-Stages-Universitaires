package uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.modele.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByLueFalseOrderByDateCreationDesc(); // Récupérer les notifications non lues
}