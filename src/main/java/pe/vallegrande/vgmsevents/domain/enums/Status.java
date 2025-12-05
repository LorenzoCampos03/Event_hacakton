package pe.vallegrande.vgmsevents.domain.enums;

public enum Status {
    ACTIVE("ACTIVE", "Active"),
    INACTIVE("INACTIVE", "Inactive");

    private final String code;
    private final String description;

    Status(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static Status fromString(String value) {
        for (Status status : Status.values()) {
            if (status.code.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status: " + value);
    }
}
