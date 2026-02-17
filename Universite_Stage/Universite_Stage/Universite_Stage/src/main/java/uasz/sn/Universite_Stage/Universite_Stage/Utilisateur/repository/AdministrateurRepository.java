package uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.modele.Administrateur;

import java.util.Optional;

@Repository
public interface AdministrateurRepository extends JpaRepository<Administrateur, Long> {

    // Retourne un Optional<Administrateur>
    Optional<Administrateur> findByMatricule(String matricule);


    // Méthode pour vérifier si un administrateur existe par son matricule
    boolean existsByMatricule(String matricule);



}
