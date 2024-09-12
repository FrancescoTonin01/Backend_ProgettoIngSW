package it.unife.ingsw202324.MicroservizioBase.api;

import it.unife.ingsw202324.MicroservizioBase.models.UserUpdateRequest;
import it.unife.ingsw202324.MicroservizioBase.models.User;
import it.unife.ingsw202324.MicroservizioBase.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

//controller per la gestione degli utenti
@RestController
@RequestMapping("/api")
public class MainController {

    @Autowired
    private UserService userService;

    //login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        //controlla se l'username esiste già e se la password è corretta
        try {
            User existingUser = userService.findByUsername(user.getUsername());
            if (existingUser != null && userService.checkPassword(user.getPassword(), existingUser.getPassword())) {
                return ResponseEntity.ok(existingUser);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenziali non valide");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante il login: " + e.getMessage());
        }
    }

    //register
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            // Verifica se l'username esiste già
            if (userService.findByUsername(user.getUsername()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Errore durante la registrazione: Il nome utente è già in uso");
            }
            
            // Verifica se l'email esiste già
            if (userService.findByEmail(user.getEmail()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Errore durante la registrazione: L'indirizzo email è già in uso");
            }

            //salva l'utente
            userService.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (IllegalArgumentException e) {
            //se l'username o l'email non sono validi
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Errore durante la registrazione: " + e.getMessage());
        } catch (Exception e) {
            //se c'è un errore interno
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Errore interno durante la registrazione: " + e.getMessage());
        }
    }

    
    @PutMapping("/update-user")
    public ResponseEntity<?> updatedata(@RequestBody UserUpdateRequest updateRequest) {
        try {
            //trova l'utente con la mail (se esiste)
            User existingUser = userService.findByEmail(updateRequest.getEmail());
            if (existingUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente non trovato");
            }
            //controlla se la password corrente è corretta
            if (!userService.checkPassword(updateRequest.getCurrentPassword(), existingUser.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Password corrente non valida");
            }
            //controlla se l'username esiste già
            if (!existingUser.getUsername().equals(updateRequest.getUsername()) && 
                userService.existsByUsername(updateRequest.getUsername())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Errore durante l'aggiornamento: Il nome utente è già in uso");
            }
            //controlla se la nuova password è stata cambiata e in caso la cripta e procede a cambiarla
            if (updateRequest.getNewPassword() != null && !updateRequest.getNewPassword().isEmpty()) {
                String encodedPassword = userService.encodePassword(updateRequest.getNewPassword());
                existingUser.setPassword(encodedPassword);
            }
            existingUser.setUsername(updateRequest.getUsername());
            existingUser.setBirthdate(updateRequest.getBirthdate());
            existingUser.setGender(updateRequest.getGender());
            //aggiorna l'utente
            userService.updateUser(existingUser);
            return ResponseEntity.ok(existingUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errore durante l'aggiornamento: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore interno durante l'aggiornamento: " + e.getMessage());
        }
    }
}
