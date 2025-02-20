package org.metropolia.minimalnotepad.model;

public enum ColorEnum {
    WHITE("#FFFFFF"),YELLOW("FEDE57"),BLUE("A2E7FB"),RED("F56765");

    private final String hexCode;
    private ColorEnum(String hexCode) {
        this.hexCode = hexCode;
    }
    public String getHexCode() {
        return hexCode;
    }
}
