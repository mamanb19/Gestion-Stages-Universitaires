package uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.modele;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String description;
    private String entreprise;
    private String duree;
    private String competencesRequises;
    private LocalDate dateLimite;
    private String lieu;
    private boolean archive = false;

    @Enumerated(EnumType.STRING)
    private StatutOffre statut = StatutOffre.DISPONIBLE;
    @OneToMany(mappedBy = "offreStage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Candidature> candidatures;
}
