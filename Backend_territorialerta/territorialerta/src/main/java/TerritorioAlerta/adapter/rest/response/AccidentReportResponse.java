package TerritorioAlerta.adapter.rest.response;

import java.time.LocalDateTime;

/** Representación REST pública de un reporte de accidente. */
public class AccidentReportResponse {

    private Long id_accident_report;
    private Long id_user;
    private LocalDateTime date;
    private String type_report;
    private String commune;
    private String neighborhood;
    private String address;
    private String image;
    private String description;
    private String status;
    private LocalDateTime creation_date;

    public Long getId_accident_report() { return id_accident_report; }
    public void setId_accident_report(Long id_accident_report) { this.id_accident_report = id_accident_report; }

    public Long getId_user() { return id_user; }
    public void setId_user(Long id_user) { this.id_user = id_user; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public String getType_report() { return type_report; }
    public void setType_report(String type_report) { this.type_report = type_report; }

    public String getCommune() { return commune; }
    public void setCommune(String commune) { this.commune = commune; }

    public String getNeighborhood() { return neighborhood; }
    public void setNeighborhood(String neighborhood) { this.neighborhood = neighborhood; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreation_date() { return creation_date; }
    public void setCreation_date(LocalDateTime creation_date) { this.creation_date = creation_date; }
    
}
