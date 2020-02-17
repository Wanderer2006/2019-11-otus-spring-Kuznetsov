package ru.kusoft.library.service;

public interface IOService {

    void print(String formatStr, Object... values);

    void println(String formatStr, Object... values);

    String inputString();

    String inputStringWithPrompt(String promptMessage);

    Long inputLong();

    Long inputLongWithPrompt(String promptMessage);

    int inputIntWithPrompt(String promptMessage);
}
