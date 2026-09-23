package TerritorioAlerta.adapter.out.persistence;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import TerritorioAlerta.domain.model.Accident_report;
import TerritorioAlerta.domain.port.Accident_reportPort;
import TerritorioAlerta.infrastructure.persistence.entities.Accident_reportEntity;
import TerritorioAlerta.infrastructure.persistence.mapper.AccidentReportMapper;
import TerritorioAlerta.infrastructure.persistence.repository.Accident_reportRepository;

@Service
/** Adapta las operaciones de reportes del dominio al repositorio JPA. */
public class AccidentReportAdapter implements Accident_reportPort {
    
    private final Accident_reportRepository accident_reportRepository;

    /** Construye el adapter con el repositorio JPA de reportes. */
    public AccidentReportAdapter(Accident_reportRepository accident_reportRepository) {
        this.accident_reportRepository = accident_reportRepository;
    }

    @Override
    /** Busca un reporte por ID y convierte la entidad al modelo de dominio. */
    public Accident_report findById(Long id_accident_report) {
        Accident_reportEntity entity = accident_reportRepository.findById(id_accident_report).orElse(null);
        return AccidentReportMapper.toDomain(entity);
    }

    @Override 
    /** Persiste un reporte de dominio y devuelve el reporte resultante. */
    public Accident_report save(Accident_report accident_report){
        Accident_reportEntity saved = accident_reportRepository.save(AccidentReportMapper.toEntity(accident_report));
        return AccidentReportMapper.toDomain(saved);
    }

    @Override
    /** Obtiene y convierte los reportes asociados a un usuario. */
    public List<Accident_report> findByIdUserList(Long id_user) {
    return accident_reportRepository.findAllByIdUser(id_user).stream()
            .map(AccidentReportMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    /** Obtiene y convierte todos los reportes almacenados. */
    public List<Accident_report> findAll() {
        return accident_reportRepository.findAll().stream()
                .map(AccidentReportMapper::toDomain)
                .collect(Collectors.toList());
    }

}
