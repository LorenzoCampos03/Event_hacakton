package pe.vallegrande.vgmsevents.infrastructure.client;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class InstitutionDto {
    private String institutionId;
    private String status; // puedes mapear a enum más adelante si quieres
    private InstitutionInformationDto institutionInformation;
    private AddressDto address;
    private List<ContactMethodDto> contactMethods;
    private String gradingType;
    private String classroomType;
    private List<ScheduleDto> schedules;
    private List<String> classroomIds;
    private String directorId;
    private List<String> auxiliaryIds;
    private String ugel;
    private String dre;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}

@Data
class InstitutionInformationDto {
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
class AddressDto {
    private String street;
    private String district;
    private String province;
    private String department;
    private String postalCode;
}

@Data
class ContactMethodDto {
    private String type;
    private String value;
}

@Data
class ScheduleDto {
    private String type;
    private String entryTime;
    private String exitTime;
}
