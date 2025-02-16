package org.metropolia.minimalnotepad.model;

public enum Colour {
    WHITE("#FFFFFF"),YELLOW("fede57"),BLUE("a2e7fb"),RED("f56765");

    private final String hexCode;
    private Colour(String hexCode) {
        this.hexCode = hexCode;
    }
    public String getHexCode() {
        return hexCode;
    }
}
