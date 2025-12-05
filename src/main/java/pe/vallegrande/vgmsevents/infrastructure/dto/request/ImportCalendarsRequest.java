package pe.vallegrande.vgmsevents.infrastructure.dto.request;

import java.util.List;

public class ImportCalendarsRequest {

    private List<AcademicCalendarRequest> academic_calendar;
    private List<EventCalendarRequest> event_calendar;

    public List<AcademicCalendarRequest> getAcademic_calendar() {
        return academic_calendar;
    }

    public void setAcademic_calendar(List<AcademicCalendarRequest> academic_calendar) {
        this.academic_calendar = academic_calendar;
    }

    public List<EventCalendarRequest> getEvent_calendar() {
        return event_calendar;
    }

    public void setEvent_calendar(List<EventCalendarRequest> event_calendar) {
        this.event_calendar = event_calendar;
    }
}
