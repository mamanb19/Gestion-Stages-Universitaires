package uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
public class EtudiantController  {
    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private EtudiantService etudiantService;

    @Autowired
    private CandidatureService candidatureService;

    @Autowired
    private StageService stageService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private EnseignantService enseignantService;

    @RequestMapping(value = "/Etudiant/Accueil", method = RequestMethod.GET)
    public String acceuil_Etudiant(Model model, Principal principal) {
        Utilisateur utilisateur=utilisateurService.rechercher_Utilisateur(principal.getName());
        model.addAttribute("nom",utilisateur.getNom());
        model.addAttribute("prenom",utilisateur.getPrenom().charAt(0));
        return "template_Etudiant";
    }



    @GetMapping("/candidatures_Etudiant")
    public String listerCandidatures(Model model, Authentication authentication) {
        // Récupérer l'étudiant connecté
        String username = authentication.getName();
        Etudiant etudiant = etudiantService.trouverParUsername(username);

        // Récupérer les candidatures de l'étudiant avec le statut EN_ATTENTE
        List<Candidature> candidatures = candidatureService.listerCandidaturesParEtudiantEtStatut(etudiant.getId(), StatutCandidature.EN_ATTENTE);
        model.addAttribute("candidatures", candidatures);

        // Récupérer la liste des stages disponibles
        List<Stage> stages = stageService.trouverOffresDisponibles();
        model.addAttribute("stages", stages);

        return "candidature_etudiant";
    }



    @PostMapping("/candidatures_Etudiant/soumettre")
    public String soumettreCandidature(
            @RequestParam Long stageId,
            @RequestParam("lettreMotivation") MultipartFile lettreMotivation,
            @RequestParam("cv") MultipartFile cv,
            Model model,
            Authentication authentication) throws IOException {

        // Récupérer l'étudiant connecté
        String username = authentication.getName(); // Récupère le username de l'étudiant connecté
        Etudiant etudiant = etudiantService.trouverParUsername(username); // Méthode à implémenter dans EtudiantService

        // Appeler le service pour ajouter la candidature
        candidatureService.ajouterCandidature(etudiant.getId(), stageId, lettreMotivation, cv);

        // Ajouter un message de succès au modèle
        model.addAttribute("message", "Candidature soumise avec succès !");

        // Rediriger vers la vue des candidatures
        return "redirect:/candidatures_Etudiant";
    }

    // Traiter la modification d'une candidature
    @PostMapping("/candidatures_Etudiant/modifier")
    public String modifierCandidature(@RequestParam Long id, @RequestParam Long etudiantId, @RequestParam Long stageId, @RequestParam(value = "lettreMotivation", required = false) MultipartFile lettreMotivation, @RequestParam(value = "cv", required = false) MultipartFile cv, @RequestParam StatutCandidature statut, Model model) throws IOException {

        // Appeler le service pour modifier la candidature
        candidatureService.modifierCandidature(id, etudiantId, stageId, lettreMotivation, cv, statut);

        // Ajouter un message de succès au modèle
        model.addAttribute("message", "Candidature modifiée avec succès !");

        // Rediriger vers la vue des candidatures
        return "redirect:/candidatures_Etudiant";
    }

    // Télécharger la lettre de motivation
    @GetMapping("/candidatures_Etudiant/telecharger/lettreMotivation")
    public ResponseEntity<byte[]> telechargerLettre(@RequestParam Long id) throws IOException {
        Candidature candidature = candidatureService.trouverCandidatureParId(id);
        byte[] fileContent = fileStorageService.loadFile(candidature.getLettreMotivationPath());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + candidature.getLettreMotivationPath() + "\"")
                .body(fileContent);
    }

    // Télécharger le CV
    @GetMapping("/candidatures_Etudiant/telecharger/cv")
    public ResponseEntity<byte[]> telechargerCv(@RequestParam Long id) throws IOException {
        Candidature candidature = candidatureService.trouverCandidatureParId(id);
        byte[] fileContent = fileStorageService.loadFile(candidature.getCvPath());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + candidature.getCvPath() + "\"")
                .body(fileContent);
    }

}
