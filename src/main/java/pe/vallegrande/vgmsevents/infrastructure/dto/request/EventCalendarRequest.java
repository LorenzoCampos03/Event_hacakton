package pe.vallegrande.vgmsevents.infrastructure.dto.request;

public class EventCalendarRequest {

    private Integer eventCalendarId;
    private Integer calendarId;
    private Long eventId;

    public Integer getEventCalendarId() {
        return eventCalendarId;
    }

    public void setEventCalendarId(Integer eventCalendarId) {
        this.eventCalendarId = eventCalendarId;
    }

    public Integer getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(Integer calendarId) {
        this.calendarId = calendarId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }
}
