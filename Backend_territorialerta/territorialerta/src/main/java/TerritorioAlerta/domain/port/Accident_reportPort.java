package TerritorioAlerta.domain.port;

import java.util.List;

import TerritorioAlerta.domain.model.Accident_report;

public interface Accident_reportPort {
    
    Accident_report findById(Long id_accident_report);

    Accident_report save(Accident_report accident_report);

    Accident_report findByIdUser(int id_user);
    
    List<Accident_report> findByIdUserList(int id_user);

    List<Accident_report> findAll();
}
