package uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uasz.sn.Universite_Stage.Universite_Stage.Authentification.modele.Utilisateur;
import uasz.sn.Universite_Stage.Universite_Stage.Authentification.service.UtilisateurService;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.modele.Candidature;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.modele.Etudiant;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.modele.Stage;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.modele.StatutCandidature;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.service.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
public class EnseignantController {

    @Autowired
    private UtilisateurService utilisateurService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private EnseignantService enseignantService;

    @Autowired
    private EtudiantService etudiantService;

    @Autowired
    private CandidatureService candidatureService;

    @Autowired
    private FileStorageService fileStorageService;


    @Autowired
    private StageService stageService;

    @RequestMapping(value = "/Enseignant/Accueil", method = RequestMethod.GET)
    public String accueil_Enseignant(Model model, Principal principal){
        Utilisateur utilisateur=utilisateurService.rechercher_Utilisateur(principal.getName());
        model.addAttribute("nom",utilisateur.getNom());
        model.addAttribute("prenom",utilisateur.getPrenom().charAt(0));
        return "template_Enseignant";
    }




    // Afficher la liste des candidatures
    @GetMapping("/candidature_Enseignant")
    public String listerCandidatures(Model model) {
        List<Candidature> candidatures = candidatureService.listerCandidatures();
        model.addAttribute("candidatures", candidatures);

        List<Etudiant> etudiants =etudiantService.lister();
        model.addAttribute("etudiants", etudiants);

        List<Stage> stages=stageService.getAllStages();
        model.addAttribute("stages", stages);
        return "candidature_enseignant";
    }

    // Traiter la modification d'une candidature
    @PostMapping("/candidature_Enseignant/modifier")
    public String modifierCandidature(@RequestParam Long id, @RequestParam Long etudiantId, @RequestParam Long stageId, @RequestParam(value = "lettreMotivation", required = false) MultipartFile lettreMotivation, @RequestParam(value = "cv", required = false) MultipartFile cv, @RequestParam StatutCandidature statut, Model model) throws IOException {

        // Appeler le service pour modifier la candidature
        candidatureService.modifierCandidature(id, etudiantId, stageId, lettreMotivation, cv, statut);

        // Ajouter un message de succès au modèle
        model.addAttribute("message", "Candidature modifiée avec succès !");

        // Rediriger vers la vue des candidatures
        return "redirect:/candidature_Enseignant";
    }

    // Télécharger la lettre de motivation
    @GetMapping("/candidature_Enseignant/telecharger/lettreMotivation")
    public ResponseEntity<byte[]> telechargerLettre(@RequestParam Long id) throws IOException {
        Candidature candidature = candidatureService.trouverCandidatureParId(id);
        byte[] fileContent = fileStorageService.loadFile(candidature.getLettreMotivationPath());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + candidature.getLettreMotivationPath() + "\"")
                .body(fileContent);
    }

    // Télécharger le CV
    @GetMapping("/candidature_Enseignant/telecharger/cv")
    public ResponseEntity<byte[]> telechargerCv(@RequestParam Long id) throws IOException {
        Candidature candidature = candidatureService.trouverCandidatureParId(id);
        byte[] fileContent = fileStorageService.loadFile(candidature.getCvPath());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + candidature.getCvPath() + "\"")
                .body(fileContent);
    }

    // Supprimer une candidature
    @GetMapping("/candidature_Enseignant/supprimer")
    public String supprimerCandidature(@RequestParam Long id) {
        candidatureService.supprimerCandidature(id);
        return "redirect:/candidature_Enseignant";
    }


}
