package pe.vallegrande.vgmsevents.infrastructure.dto.request;

import java.time.LocalDate;

public class EventCreateRequest {

    private String institutionId;  // 🔹 tipo String
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String eventType;
    private Boolean isHoliday;
    private Boolean isRecurring;
    private Boolean isNational;
    private Boolean affectsClasses;
    private String createdBy;

    // 🔹 Getters y setters deben ser String
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
}
