package pe.vallegrande.vgmsevents.infrastructure.dto.response;

import pe.vallegrande.vgmsevents.domain.model.AcademicCalendar;
import pe.vallegrande.vgmsevents.domain.model.EventCalendar;

import java.util.List;

public class ImportResult {
    private List<AcademicCalendar> academicCalendars;
    private List<EventCalendar> eventCalendars;

    public ImportResult() {}

    public ImportResult(List<AcademicCalendar> academicCalendars, List<EventCalendar> eventCalendars) {
        this.academicCalendars = academicCalendars;
        this.eventCalendars = eventCalendars;
    }

    public List<AcademicCalendar> getAcademicCalendars() {
        return academicCalendars;
    }

    public void setAcademicCalendars(List<AcademicCalendar> academicCalendars) {
        this.academicCalendars = academicCalendars;
    }

    public List<EventCalendar> getEventCalendars() {
        return eventCalendars;
    }

    public void setEventCalendars(List<EventCalendar> eventCalendars) {
        this.eventCalendars = eventCalendars;
    }
}
