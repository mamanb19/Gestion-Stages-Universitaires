package uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.modele.Candidature;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.modele.StatutCandidature;

import java.util.List;

@Repository
public interface CandidatureRepository extends JpaRepository<Candidature, Long> {
    List<Candidature> findByEtudiantIdAndStatut(Long etudiantId, StatutCandidature statut);

    // Rechercher les candidatures par statut
    List<Candidature> findByStatut(StatutCandidature statut);

}
