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
@AllArgsConstructor
@NoArgsConstructor
@Table("event")
public class Event {

    @Id
    @Column("event_id")

    private Long eventId; 

    @Column("institution_id")
    private String institutionId;

    @Column("title")
    private String title;

    @Column("description")
    private String description;

    @Column("start_date")
    private LocalDate startDate;

    @Column("end_date")
    private LocalDate endDate;

    @Column("event_type")
    private String eventType;

    @Column("is_holiday")
    private Boolean isHoliday = false;

    @Column("is_recurring")
    private Boolean isRecurring = false;

    @Column("is_national")
    private Boolean isNational = false;

    @Column("status")
    private String status = "ACTIVE";

    @Column("affects_classes")
    private Boolean affectsClasses = false;

    @Column("created_by")
    private String createdBy;

    @Column("created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column("updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
