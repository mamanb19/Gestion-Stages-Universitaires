package uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.modele;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uasz.sn.Universite_Stage.Universite_Stage.Authentification.modele.Utilisateur;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "id")
public class Administrateur extends Utilisateur {

    private String matricule;
    private boolean archive;
}
