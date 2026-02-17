package uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.modele.Administrateur;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.repository.AdministrateurRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AdministrateurService {

    @Autowired
    private AdministrateurRepository administrateurRepository;

    // Ajouter un administrateur
    public Administrateur ajouterAdministrateur(Administrateur administrateur) {
        // Vérifier si le matricule est déjà utilisé
        if (administrateurRepository.existsByMatricule(administrateur.getMatricule())) {
            throw new RuntimeException("Un administrateur avec ce matricule existe déjà.");
        }
        return administrateurRepository.save(administrateur);
    }

    // Lister tous les administrateurs
    public List<Administrateur> listerAdministrateurs() {
        return administrateurRepository.findAll();
    }

    // Trouver un administrateur par son ID
    public Optional<Administrateur> trouverAdministrateurParId(Long id) {
        return administrateurRepository.findById(id);
    }

    // Mettre à jour un administrateur
    public Administrateur mettreAJourAdministrateur(Long id, Administrateur administrateurDetails) {
        Administrateur administrateur = administrateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrateur non trouvé avec l'ID : " + id));

        // Mettre à jour les champs nécessaires
        administrateur.setNom(administrateurDetails.getNom());
        administrateur.setPrenom(administrateurDetails.getPrenom());
        administrateur.setMatricule(administrateurDetails.getMatricule());
        administrateur.setUsername(administrateurDetails.getUsername());
        administrateur.setPassword(administrateurDetails.getPassword());
        administrateur.setActive(administrateurDetails.isActive());

        return administrateurRepository.save(administrateur);
    }

    // Supprimer un administrateur
    public void supprimerAdministrateur(Long id) {
        Administrateur administrateur = administrateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrateur non trouvé avec l'ID : " + id));
        administrateurRepository.delete(administrateur);
    }

    // Trouver un administrateur par son matricule
    public Administrateur trouverAdministrateurParMatricule(String matricule) {
        return administrateurRepository.findByMatricule(matricule)
                .orElseThrow(() -> new RuntimeException("Administrateur non trouvé avec le matricule : " + matricule));
    }

}
