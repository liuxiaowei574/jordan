package com.nuctech.ls.model.util;

public enum OrderType {

    ASC("asc"), DESC("desc");

    private String text;

    private OrderType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
