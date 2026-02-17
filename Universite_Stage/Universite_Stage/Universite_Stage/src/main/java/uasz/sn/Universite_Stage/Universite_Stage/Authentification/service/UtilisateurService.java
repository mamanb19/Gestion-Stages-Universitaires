package uasz.sn.Universite_Stage.Universite_Stage.Authentification.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uasz.sn.Universite_Stage.Universite_Stage.Authentification.modele.Role;
import uasz.sn.Universite_Stage.Universite_Stage.Authentification.modele.Utilisateur;
import uasz.sn.Universite_Stage.Universite_Stage.Authentification.repository.RoleRepository;
import uasz.sn.Universite_Stage.Universite_Stage.Authentification.repository.UtilisateurRepository;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.modele.Enseignant;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.repository.EnseignantRepository;

import java.util.Date;

@Service
@Transactional
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    // Méthode pour ajouter un rôle
    public Role ajouter_Role(Role role) {
        Role existingRole = roleRepository.findRoleByRole(role.getRole());
        if (existingRole == null) {
            return roleRepository.save(role);
        }
        return existingRole;
    }

    public Utilisateur ajouter_Utilisateur(Utilisateur utilisateur) {
        if (utilisateurRepository.findUtilisateurByUsername(utilisateur.getUsername()) != null) {
            throw new IllegalArgumentException("Un utilisateur avec ce username existe déjà.");
        }

        // Cryptage du mot de passe et ajout de la date de création
        utilisateur.setPassword(passwordEncoder.encode(utilisateur.getPassword()));
        utilisateur.setDateCreation(new Date());

        // Sauvegarde de l'utilisateur

        return utilisateurRepository.save(utilisateur);
    }

    // Méthode pour ajouter un rôle à un utilisateur existant
    public void ajouter_UtilisateurRoles(Utilisateur utilisateur, Role role) {
        if (utilisateur != null && role != null) {
            utilisateur.getRoles().add(role);
            utilisateurRepository.save(utilisateur);
        } else {
            System.out.println("Utilisateur ou rôle non trouvé.");
        }
    }



    // Méthode pour rechercher un utilisateur par nom d'utilisateur
    public Utilisateur rechercher_Utilisateur(String username) {
        return utilisateurRepository.findUtilisateurByUsername(username);
    }

    public String obtenirRoleUtilisateur(Long utilisateurId) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId).orElse(null);
        if (utilisateur != null && !utilisateur.getRoles().isEmpty()) {
            return utilisateur.getRoles().iterator().next().getRole(); // Retourne le premier rôle
        }
        return "Aucun rôle";
    }


}
