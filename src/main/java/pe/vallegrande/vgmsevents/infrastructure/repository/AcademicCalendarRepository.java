package pe.vallegrande.vgmsevents.infrastructure.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import pe.vallegrande.vgmsevents.domain.model.AcademicCalendar;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



@Repository
public interface AcademicCalendarRepository extends ReactiveCrudRepository<AcademicCalendar, Integer> {

    Flux<AcademicCalendar> findByInstitutionId(String  institutionId);

    Mono<Boolean> existsByInstitutionIdAndAcademicYear(String  institutionId, Integer academicYear);
}
