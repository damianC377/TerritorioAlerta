package TerritorioAlerta.adapter.rest.response;

import java.time.LocalDateTime;

/** Representación REST pública de un usuario. */
public class UserResponse {

    private Long id_user;
    private String name;
    private String lastname;
    private String email;
    private String commune;
    private String neighborhood;
    private String role;
    private LocalDateTime creation_date;

    public Long getId_user() { return id_user; }
    public void setId_user(Long id_user) { this.id_user = id_user; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCommune() { return commune; }
    public void setCommune(String commune) { this.commune = commune; }

    public String getNeighborhood() { return neighborhood; }
    public void setNeighborhood(String neighborhood) { this.neighborhood = neighborhood; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public LocalDateTime getCreation_date() { return creation_date; }
    public void setCreation_date(LocalDateTime creation_date) { this.creation_date = creation_date; }
}

