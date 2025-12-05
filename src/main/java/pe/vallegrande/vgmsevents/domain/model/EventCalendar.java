package pe.vallegrande.vgmsevents.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("event_calendar")
public class EventCalendar {

    @Id
    @Column("event_calendar_id")
    private Integer eventCalendarId;

    @Column("calendar_id")
    private Integer calendarId;

    @Column("event_id")
    private Long eventId;

    @Column("created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
