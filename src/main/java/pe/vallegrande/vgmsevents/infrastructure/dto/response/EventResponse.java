package pe.vallegrande.vgmsevents.infrastructure.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;


import pe.vallegrande.vgmsevents.domain.model.Event;

public class EventResponse {

    private Long eventId;
    private String institutionId;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String eventType;
    private Boolean isHoliday;
    private Boolean isRecurring;
    private Boolean isNational;
    private String status; // ✅ CAMBIADO A STRING
    private Boolean affectsClasses;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String institutionName;

    public EventResponse() {
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public EventResponse(Event event) {
        this.eventId = event.getEventId();
        this.institutionId = event.getInstitutionId() != null ? event.getInstitutionId().toString() : null;
        this.title = event.getTitle();
        this.description = event.getDescription();
        this.startDate = event.getStartDate();
        this.endDate = event.getEndDate();
        this.eventType = event.getEventType();
        this.isHoliday = event.getIsHoliday();
        this.isRecurring = event.getIsRecurring();
        this.isNational = event.getIsNational();
        this.status = event.getStatus();
        this.affectsClasses = event.getAffectsClasses();
        this.createdBy = event.getCreatedBy();
        this.createdAt = event.getCreatedAt();
        this.updatedAt = event.getUpdatedAt();
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(String institutionId) {
        this.institutionId = institutionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Boolean getIsHoliday() {
        return isHoliday;
    }

    public void setIsHoliday(Boolean isHoliday) {
        this.isHoliday = isHoliday;
    }

    public Boolean getIsRecurring() {
        return isRecurring;
    }

    public void setIsRecurring(Boolean isRecurring) {
        this.isRecurring = isRecurring;
    }

    public Boolean getIsNational() {
        return isNational;
    }

    public void setIsNational(Boolean isNational) {
        this.isNational = isNational;
    }

    public String getStatus() { // ✅ ahora String
        return status;
    }

    public void setStatus(String status) { // ✅ ahora String
        this.status = status;
    }

    public Boolean getAffectsClasses() {
        return affectsClasses;
    }

    public void setAffectsClasses(Boolean affectsClasses) {
        this.affectsClasses = affectsClasses;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }
}
