package it.unife.ingsw202324.MicroservizioBase.models;

import java.time.LocalDate;
//classe per la richiesta di aggiornamento dell'utente
public class UserUpdateRequest {
    private String username;
    private String email;
    private String currentPassword;
    private String newPassword;
    private LocalDate birthdate;
    private String gender;

    // Costruttore vuoto
    public UserUpdateRequest() {}

    // Costruttore con tutti i campi
    public UserUpdateRequest(String username, String email, String currentPassword, String newPassword, LocalDate birthdate, String gender) {
        this.username = username;
        this.email = email;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.birthdate = birthdate;
        this.gender = gender;
    }

    // Getter e Setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    // toString method for debugging
    @Override
    public String toString() {
        return "UserUpdateRequest{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", currentPassword='[PROTECTED]'" +
                ", newPassword='[PROTECTED]'" +
                ", birthdate=" + birthdate +
                ", gender='" + gender + '\'' +
                '}';
    }
}