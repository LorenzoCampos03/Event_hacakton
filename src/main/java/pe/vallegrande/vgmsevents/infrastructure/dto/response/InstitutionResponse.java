package pe.vallegrande.vgmsevents.infrastructure.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class InstitutionResponse {
    private String institutionId;
    private String status;
    private InstitutionInformation institutionInformation;
    private Address address;
    private List<ContactMethod> contactMethods;
    private String gradingType;
    private String classroomType;
    private List<Schedule> schedules;
    private List<String> classroomIds;
    private String directorId;
    private List<String> auxiliaryIds;
    private String ugel;
    private String dre;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    @Data
    public static class InstitutionInformation {
        private String institutionName;
        private String codeInstitution;
        private String modularCode;
        private String institutionType;
        private String institutionLevel;
        private String gender;
        private String slogan;
        private String logoUrl;
    }

    @Data
    public static class Address {
        private String department;
        private String province;
        private String district;
        private String addressDetail;
    }

    @Data
    public static class ContactMethod {
        private String type;
        private String value;
    }

    @Data
    public static class Schedule {
        private String day;
        private String startTime;
        private String endTime;
    }
}
