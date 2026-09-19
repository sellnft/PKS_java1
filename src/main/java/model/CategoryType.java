package model;

public enum CategoryType {
    DEVELOPMENT_REQUEST("Заявка на разработку"),
    SERVICE_REQUEST("Заявка на обслуживание");

    private final String valueToDisplay;

    CategoryType(String valueToDisplay) {
        this.valueToDisplay = valueToDisplay;
    }

    public String getValueToDisplay() {
        return valueToDisplay;
    }

}
