package uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.modele;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private LocalDateTime dateCreation;
    private boolean lue = false; // Indique si la notification a été lue
    // Constructeur par défaut
    public Notification() {
        this.dateCreation = LocalDateTime.now();
    }

    // Constructeur avec un paramètre String
    public Notification(String message) {
        this.message = message;
        this.dateCreation = LocalDateTime.now();
    }
}
