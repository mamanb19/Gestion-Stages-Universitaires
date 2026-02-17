package uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.modele.Enseignant;

import java.util.Optional;

@Repository
public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {

    boolean existsByUsername(String username);


    // Rechercher un enseignant par matricule
    @Query("SELECT e FROM Enseignant e WHERE e.matricule = :matricule")
    Optional<Enseignant> findPermanentByMatricule(@Param("matricule") String matricule);


    Optional<Enseignant> findByUsername(String username);
}
