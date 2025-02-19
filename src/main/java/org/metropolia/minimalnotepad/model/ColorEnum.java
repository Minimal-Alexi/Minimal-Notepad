package org.metropolia.minimalnotepad.model;

public enum ColorEnum {
    WHITE("#FFFFFF"),YELLOW("fede57"),BLUE("a2e7fb"),RED("f56765");

    private final String hexCode;
    private ColorEnum(String hexCode) {
        this.hexCode = hexCode;
    }
    public String getHexCode() {
        return hexCode;
    }
}
