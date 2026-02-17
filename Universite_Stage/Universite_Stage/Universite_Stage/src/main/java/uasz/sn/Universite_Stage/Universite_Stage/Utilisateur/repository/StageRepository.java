package uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.modele.Stage;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.modele.StatutOffre;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StageRepository extends JpaRepository<Stage, Long> {
    // Récupérer les stages dont la dateLimite est antérieure à une date donnée
    List<Stage> findByDateLimiteBefore(LocalDate date);
    List<Stage> findByStatut(StatutOffre statut);
}