package uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.modele.Stage;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.modele.StatutCandidature;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.modele.StatutOffre;
import uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.service.StageService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/stages")
public class StageController {

    @Autowired
    private StageService stageService;

    // Afficher la vue principale des stages


    @GetMapping
    public String afficherListeStages(Model model) {
        List<Stage> stages = stageService.trouverOffresDisponibles(); // Récupère uniquement les stages disponibles
        model.addAttribute("stages", stages);
        model.addAttribute("stage", new Stage());
        return "stage";
    }
    @PostMapping("/ajouter")
    public String ajouterStage(@ModelAttribute Stage stage) {
        stageService.ajouterStage(stage);
        return "redirect:/stages";
    }
    @GetMapping("/stages/modifier/{id}")
    public String afficherFormulaireModification(@PathVariable("id") Long id, Model model) {
        Stage stage = stageService.getStageById(id); // Récupérer le stage par son ID
        model.addAttribute("stage", stage); // Passer l'objet stage à la vue
        return "stage"; // Retourner la vue du formulaire
    }
    @PostMapping("/modifier")
    public String modifierStage(@ModelAttribute("stage") Stage stage) {
        stageService.modifierStage(stage.getId(),stage);
        return "redirect:/stages";
    }

    @PostMapping("/supprimer")
    public String supprimerStage(@RequestParam("id") Long id) {
        stageService.supprimerStage(id);
        return "redirect:/stages"; // Redirige vers la liste des stages après suppression
    }

    @GetMapping("/offres_stage")
    public String consulterOffresStage(Model model) {
        // Récupérer toutes les offres de stage depuis le service
        model.addAttribute("stages", stageService.trouverOffresDisponibles());
        return "offres_stage"; // Nom de la vue qui contient la liste des stages
    }
    @GetMapping("/archivees")
    public String afficherOffresArchivees(Model model) {
        List<Stage> offresArchivees = stageService.trouverOffresArchivees();
        List<Stage> offresValidees = stageService.trouverOffresValidees();

        // Combinez les listes si nécessaire
        List<Stage> offres = new ArrayList<>();
        offres.addAll(offresArchivees);
        offres.addAll(offresValidees);

        model.addAttribute("offres", offres);
        return "offres_archivees";
    }
    @PostMapping("/reactiver/{id}")
    public String reactiverOffre(@PathVariable Long id) {
        Stage offre = stageService.getStageById(id);
        if (offre != null && offre.getStatut() == StatutOffre.ARCHIVEE) {
            offre.setStatut(StatutOffre.DISPONIBLE); // Réactiver l'offre
            stageService.ajouterStage(offre);
        }
        return "redirect:/stages/archivees";
    }
    @PostMapping("/archiver")
    public String archiverStage(@RequestParam("id") Long id) {
        Stage stage = stageService.getStageById(id);
        if (stage != null && stage.getStatut() != StatutOffre.ARCHIVEE) {
            // Vérifier si l'offre a des candidatures en cours
            if (stage.getCandidatures().stream().anyMatch(c -> c.getStatut() == StatutCandidature.EN_ATTENTE)) {
                // Ne pas archiver si des candidatures sont en attente
                return "redirect:/stages?error=Impossible d'archiver une offre avec des candidatures en attente";
            }
            stage.setStatut(StatutOffre.ARCHIVEE); // Définir le statut comme archivé
            stageService.ajouterStage(stage); // Sauvegarder les modifications
        }
        return "redirect:/stages";
    }
}
