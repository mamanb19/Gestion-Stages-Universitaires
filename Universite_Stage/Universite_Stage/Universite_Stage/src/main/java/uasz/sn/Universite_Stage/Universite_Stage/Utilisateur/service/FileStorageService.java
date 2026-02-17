package uasz.sn.Universite_Stage.Universite_Stage.Utilisateur.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Transactional
public class FileStorageService {

    private final Path rootLocation = Paths.get("uploads");

    public String storeFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("Le fichier est vide.");
        }

        // Créez le dossier s'il n'existe pas
        if (!Files.exists(rootLocation)) {
            Files.createDirectories(rootLocation);
        }

        // Générer un nom de fichier unique
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // Enregistrer le fichier
        Path destinationFile = rootLocation.resolve(Paths.get(fileName)).normalize().toAbsolutePath();
        Files.copy(file.getInputStream(), destinationFile);

        return fileName; // Retourne le nom du fichier enregistré
    }

    public byte[] loadFile(String fileName) throws IOException {
        Path filePath = rootLocation.resolve(fileName).normalize().toAbsolutePath();
        return Files.readAllBytes(filePath);
    }
}
