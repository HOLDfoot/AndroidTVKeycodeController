package com.myt.messagesender;

public class TextKeyCodeModel {

    public TextKeyCodeModel(boolean enable, String text, int keyCode) {
        this.enable = enable;
        this.text = text;
        this.keyCode = keyCode;
    }

    boolean enable;
    String text;
    int keyCode;
}