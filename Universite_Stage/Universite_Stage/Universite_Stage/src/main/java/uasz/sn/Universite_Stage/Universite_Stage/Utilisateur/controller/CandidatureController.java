package uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.controller;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.modele.*;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.repository.CandidatureRepository;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.service.*;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.modele.Stage;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/candidatures")
public class CandidatureController {

    @Autowired
    private CandidatureService candidatureService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private EtudiantService etudiantService;

    @Autowired
    private StageService stageService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CandidatureRepository candidatureRepository;
    @Autowired
    private NotificationService notificationService;
    @Value("${admin.email}")
    private String adminEmail;
    // Afficher la liste des candidatures
    @GetMapping
    public String listerCandidatures(Model model) {
        // Récupérer les candidatures avec le statut ACCEPTEE
        List<Candidature> candidatures = candidatureService.listerCandidaturesParStatut(StatutCandidature.ACCEPTEE);
        model.addAttribute("candidatures", candidatures);

        // Récupérer la liste des étudiants (si nécessaire)
        List<Etudiant> etudiants = etudiantService.lister();
        model.addAttribute("etudiants", etudiants);

        // Récupérer la liste des stages disponibles
        List<Stage> stages = stageService.trouverOffresDisponibles();
        model.addAttribute("stages", stages);

        return "candidature";
    }
    // Ajout d'une candidature
    @PostMapping("/soumettre")
    public String soumettreCandidature(@RequestParam Long etudiantId, @RequestParam Long stageId, @RequestParam("lettreMotivation") MultipartFile lettreMotivation, @RequestParam("cv") MultipartFile cv, Model model) throws IOException {

        // Appeler le service pour ajouter la candidature
        candidatureService.ajouterCandidature(etudiantId, stageId, lettreMotivation, cv);

        // Ajouter un message de succès au modèle
        model.addAttribute("message", "Candidature soumise avec succès !");

        // Rediriger vers la vue des candidatures
        return "redirect:/candidatures";
    }

    // Traiter la modification d'une candidature
    @PostMapping("/modifier")
    public String modifierCandidature(@RequestParam Long id, @RequestParam Long etudiantId, @RequestParam Long stageId, @RequestParam(value = "lettreMotivation", required = false) MultipartFile lettreMotivation, @RequestParam(value = "cv", required = false) MultipartFile cv, @RequestParam StatutCandidature statut, Model model) throws IOException {

        // Appeler le service pour modifier la candidature
        candidatureService.modifierCandidature(id, etudiantId, stageId, lettreMotivation, cv, statut);

        // Ajouter un message de succès au modèle
        model.addAttribute("message", "Candidature modifiée avec succès !");

        // Rediriger vers la vue des candidatures
        return "redirect:/candidatures";
    }

    // Télécharger la lettre de motivation
    @GetMapping("/telecharger/lettreMotivation")
    public ResponseEntity<byte[]> telechargerLettre(@RequestParam Long id) throws IOException {
        Candidature candidature = candidatureService.trouverCandidatureParId(id);
        byte[] fileContent = fileStorageService.loadFile(candidature.getLettreMotivationPath());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + candidature.getLettreMotivationPath() + "\"")
                .body(fileContent);
    }

    // Télécharger le CV
    @GetMapping("/telecharger/cv")
    public ResponseEntity<byte[]> telechargerCv(@RequestParam Long id) throws IOException {
        Candidature candidature = candidatureService.trouverCandidatureParId(id);
        byte[] fileContent = fileStorageService.loadFile(candidature.getCvPath());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + candidature.getCvPath() + "\"")
                .body(fileContent);
    }

    // Supprimer une candidature
    @GetMapping("/supprimer")
    public String supprimerCandidature(@RequestParam Long id) {
        candidatureService.supprimerCandidature(id);
        return "redirect:/candidatures";
    }
    // Afficher les candidatures en attente
    @GetMapping("/en-attente")
    public String listerCandidaturesEnAttente(Model model) {
        List<Candidature> candidaturesEnAttente = candidatureService.listerCandidaturesParStatut(StatutCandidature.EN_ATTENTE);
        model.addAttribute("candidatures", candidaturesEnAttente);
        return "candidatures_en_attente"; // Assure-toi d'avoir une vue correspondante
    }

    @PostMapping("/valider")
    public String validerCandidature(@RequestParam Long id) {
        // Récupérer la candidature validée
        Candidature candidature = candidatureService.trouverCandidatureParId(id);

        // Changer le statut de la candidature
        candidatureService.changerStatutCandidature(id, StatutCandidature.ACCEPTEE);

        // Créer une notification pour l'administrateur
        String message = "La candidature de " + candidature.getEtudiant().getPrenom() + " " + candidature.getEtudiant().getNom() + " pour le stage '" + candidature.getOffreStage().getTitre() + "' a été acceptée.";
        notificationService.creerNotification(message);

        // Optionnel : Marquer l'offre de stage comme "OCCUPEE" si nécessaire
        Stage stage = candidature.getOffreStage();
        if (stage.getStatut() != StatutOffre.DISPONIBLE) {
            stage.setStatut(StatutOffre.DISPONIBLE); // Marquer l'offre comme occupée
            stageService.ajouterStage(stage); // Sauvegarder les modifications
        }
        // Envoyer un email à l'administrateur
        String adminsubject = "Nouvelle notification : Candidature acceptée";
        String emailText = "Bonjour Admin,\n\n" +
                "Une nouvelle candidature a été acceptée :\n\n" +
                "Étudiant : " + candidature.getEtudiant().getPrenom() + " " + candidature.getEtudiant().getNom() + "\n" +
                "Stage : " + candidature.getOffreStage().getTitre() + "\n" +
                "Statut : Acceptée\n\n" +
                "Cordialement,\nL'équipe de gestion des stages";
        emailService.sendEmail(adminEmail, adminsubject, emailText);
        // Envoyer un email à l'étudiant
        String subject = "Votre candidature a été validée";
        String text = "Bonjour " + candidature.getEtudiant().getPrenom() + " " + candidature.getEtudiant().getNom() + ",\n\n" +
                "Félicitations ! Votre candidature pour le stage intitulé '" + stage.getTitre() + "' a été validée.\n\n" +
                "Nous vous contactons pour la suite du processus.\n\n" +
                "Cordialement,\nL'équipe de gestion des stages";

        emailService.sendEmail(candidature.getEtudiant().getUsername(), subject, text);

        return "redirect:/candidatures/en-attente";
    }

    @PostMapping("/refuser")
    public String refuserCandidature(@RequestParam Long id) {
        // Récupérer la candidature refusée
        Candidature candidature = candidatureService.trouverCandidatureParId(id);

        // Changer le statut de la candidature
        candidatureService.changerStatutCandidature(id, StatutCandidature.REFUSEE);

        // Créer une notification pour l'administrateur
        String message = "La candidature de " + candidature.getEtudiant().getPrenom() + " " + candidature.getEtudiant().getNom() + " pour le stage '" + candidature.getOffreStage().getTitre() + "' a été refusée.";
        notificationService.creerNotification(message);
        // Ne pas archiver l'offre de stage automatiquement
        // L'offre reste disponible pour d'autres candidats
        // Envoyer un email à l'administrateur
        String adminsubject = "Nouvelle notification : Candidature refusée";
        String emailText = "Bonjour Admin,\n\n" +
                "Une nouvelle candidature a été refusée :\n\n" +
                "Étudiant : " + candidature.getEtudiant().getPrenom() + " " + candidature.getEtudiant().getNom() + "\n" +
                "Stage : " + candidature.getOffreStage().getTitre() + "\n" +
                "Statut : Refusée\n\n" +
                "Cordialement,\nL'équipe de gestion des stages";
        emailService.sendEmail(adminEmail, adminsubject, emailText);
        // Envoyer un email à l'étudiant
        String subject = "Votre candidature a été refusée";
        String text = "Bonjour " + candidature.getEtudiant().getPrenom() + " " + candidature.getEtudiant().getNom() + ",\n\n" +
                "Nous regrettons de vous informer que votre candidature pour le stage intitulé '" + candidature.getOffreStage().getTitre() + "' a été refusée.\n\n" +
                "Nous vous remercions pour votre intérêt et vous souhaitons bonne chance pour vos futures candidatures.\n\n" +
                "Cordialement,\nL'équipe de gestion des stages";

        emailService.sendEmail(candidature.getEtudiant().getUsername(), subject, text);

        return "redirect:/candidatures/en-attente";
    }
}