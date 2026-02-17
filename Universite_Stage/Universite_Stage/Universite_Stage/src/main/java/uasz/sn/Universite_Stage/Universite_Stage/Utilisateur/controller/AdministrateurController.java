package uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import uasz.sn.Universite_Stage.Universite_Stage.Authentification.modele.Role;
import uasz.sn.Universite_Stage.Universite_Stage.Authentification.modele.Utilisateur;
import uasz.sn.Universite_Stage.Universite_Stage.Authentification.repository.RoleRepository;
import uasz.sn.Universite_Stage.Universite_Stage.Authentification.repository.UtilisateurRepository;
import uasz.sn.Universite_Stage.Universite_Stage.Authentification.service.UtilisateurService;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.modele.Enseignant;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.modele.Etudiant;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.service.EnseignantService;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.service.EtudiantService;

import java.security.Principal;
import java.util.Date;
import java.util.List;
@Controller
public class AdministrateurController {

    @Autowired
    private UtilisateurService utilisateurService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private EnseignantService enseignantService;

    @Autowired
    private EtudiantService etudiantService;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;


    @RequestMapping(value = "/Administrateur/Accueil", method = RequestMethod.GET)
    public String accueil_Administrateur(Model model, Principal principal){
        Utilisateur utilisateur=utilisateurService.rechercher_Utilisateur(principal.getName());
        model.addAttribute("nom",utilisateur.getNom());
        model.addAttribute("prenom",utilisateur.getPrenom().charAt(0));
        return "template_Administrateur";
    }


    @RequestMapping(value = "/Administrateur/Enseignant", method = RequestMethod.GET)
    public  String Administrateur_Utilisateur(Model model, Principal principal){
        List<Enseignant> enseignants=enseignantService.lister();
        model.addAttribute("enseignants", enseignants);
        List<Etudiant> etudiants=etudiantService.lister();
        model.addAttribute("etudiants",etudiants);

        List<Role> roles = roleRepository.findAll();
        model.addAttribute("roles", roles);

        Utilisateur utilisateur=utilisateurService.rechercher_Utilisateur(principal.getName());
        model.addAttribute("nom",utilisateur.getNom());
        model.addAttribute("prenom",utilisateur.getPrenom().charAt(0));
        return "utilisateur_templates";
    }


    @RequestMapping(value = "/Administrateur/ajouterEnseignant", method = RequestMethod.POST)
    public String ajouter_Permanent(Enseignant enseignant) {
        // Encodage du mot de passe
        String password = passwordEncoder.encode("Passer123");
        enseignant.setPassword(password);

        // Configuration des propriétés de l'enseignant
        enseignant.setDateCreation(new Date());
        enseignant.setActive(true);

        // Récupération ou création du rôle "Enseignant"
        Role role = utilisateurService.ajouter_Role(new Role("Enseignant"));

        // Ajout du rôle à l'enseignant
        enseignant.getRoles().add(role);

        // Sauvegarde de l'enseignant
        enseignantService.ajouter(enseignant);

        return "redirect:/Administrateur/Enseignant";
    }



    @RequestMapping(value = "/Administrateur/modifierEnseignant", method = RequestMethod.POST)
    public String modifier_Permanent(Enseignant enseignant){
        Enseignant per_modif=enseignantService.rechercher(enseignant.getId());
        per_modif.setMatricule(enseignant.getMatricule());
        per_modif.setNom(enseignant.getNom());
        per_modif.setPrenom(enseignant.getPrenom());
        per_modif.setSpecialite(enseignant.getSpecialite());
        per_modif.setGrade(enseignant.getGrade());
        enseignantService.modifier(per_modif);
        return "redirect:/Administrateur/Enseignant";
    }

    @RequestMapping(value = "/Administrateur/archiverEnseignant", method = RequestMethod.POST)
    public String archiver_Permanent(Long id){
        enseignantService.archiver(id);
        return "redirect:/Administrateur/Enseignant";
    }



    @RequestMapping(value = "/Administrateur/ajouterEtudiant", method = RequestMethod.POST)
    public String ajouter_Etudiant(Etudiant etudiant) {
        // Encodage du mot de passe
        String password = passwordEncoder.encode("Passer123");
        etudiant.setPassword(password);

        // Configuration des propriétés de l'étudiant
        etudiant.setDateCreation(new Date());
        etudiant.setActive(true);

        // Récupération ou création du rôle "Etudiant"
        Role role = utilisateurService.ajouter_Role(new Role("Etudiant"));

        // Ajout du rôle à l'étudiant
        etudiant.getRoles().add(role);

        // Sauvegarde de l'étudiant
        etudiantService.ajouter(etudiant);

        return "redirect:/Administrateur/Enseignant";
    }



    @RequestMapping(value = "/Administrateur/modifierEtudiant", method = RequestMethod.POST)
    public String modifier_Etudiant(Etudiant etudiant){
        Etudiant etu_modif=etudiantService.rechercher(etudiant.getId());
        etu_modif.setMatricule(etudiant.getMatricule());
        etu_modif.setNom(etudiant.getNom());
        etu_modif.setPrenom(etudiant.getPrenom());
        etu_modif.setAdresse(etudiant.getAdresse());
        etu_modif.setTelephone(etudiant.getTelephone());
        etudiantService.modifier(etu_modif);
        return "redirect:/Administrateur/Enseignant";
    }

    @RequestMapping(value = "/Administrateur/archiverEtudiant", method = RequestMethod.POST)
    public String archiver_Etudiant(Long id){
        etudiantService.archiver(id);
        return "redirect:/Administrateur/Enseignant";
    }


}
