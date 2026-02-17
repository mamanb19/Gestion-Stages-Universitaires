package uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.modele;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uasz.sn.Universite_Stage.Universite_Stage.Authentification.modele.Utilisateur;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "id")
@Inheritance(strategy = InheritanceType.JOINED)

public class Enseignant extends Utilisateur {

    private String specialite;
    private boolean archive;
    private String matricule;
    private String grade;

}
