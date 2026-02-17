package uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.modele;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Candidature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Etudiant etudiant;

    @ManyToOne
    @JoinColumn(name = "stage_id", nullable = false)
    private Stage offreStage;

    private LocalDate dateCandidature;

    @Enumerated(EnumType.STRING)
    private StatutCandidature statut;

    private String lettreMotivationPath; // Chemin d'accès ou URL du fichier PDF de la lettre de motivation

    private String cvPath; // Chemin d'accès ou URL du fichier PDF du CV

}
