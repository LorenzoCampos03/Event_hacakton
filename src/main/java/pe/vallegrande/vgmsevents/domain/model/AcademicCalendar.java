package pe.vallegrande.vgmsevents.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("academic_calendar")
public class AcademicCalendar {

    @Id
    @Column("calendar_id")
    private Integer calendarId;

    @Column("institution_id")
    private String institutionId; 

    @Column("academic_year")
    private Integer academicYear;

    @Column("start_date")
    private LocalDate startDate;

    @Column("end_date")
    private LocalDate endDate;

    @Column("created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column("updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
