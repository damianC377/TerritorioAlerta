package TerritorioAlerta.domain.port;

import java.util.List;

import TerritorioAlerta.domain.model.Accident_report;

/** Puerto de salida para consultar y persistir reportes de accidentes. */
public interface Accident_reportPort {
    
    /** Busca un reporte por identificador o devuelve null si no existe. */
    Accident_report findById(Long id_accident_report);

    /** Guarda un reporte y devuelve su representación persistida. */
    Accident_report save(Accident_report accident_report);
    
    /** Obtiene los reportes asociados al usuario indicado. */
    List<Accident_report> findByIdUserList(Long id_user);

    /** Obtiene todos los reportes disponibles. */
    List<Accident_report> findAll();
}
