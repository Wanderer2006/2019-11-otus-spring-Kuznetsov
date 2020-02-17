package ru.kusoft.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kusoft.library.config.ApplicationSettings;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

@Service
public class IOServiceImpl implements IOService{

    private final InputStream is;
    private final PrintStream ps;
    private final Scanner sc;

    @Autowired
    public IOServiceImpl(ApplicationSettings settings) {
        this.is = settings.getInputStream();
        this.ps = settings.getPrintStream();
        this.sc = new Scanner(is);
    }

    @Override
    public void print(String formatStr, Object... values) {
        ps.printf(formatStr, values);
    }

    @Override
    public void println(String formatStr, Object... values) {
        print(formatStr, values);
        ps.println();
    }

    @Override
    public String inputString() {
        return sc.nextLine().trim();
    }

    @Override
    public String inputStringWithPrompt(String promptMessage) {
        print(promptMessage);
        return inputString();
    }

    @Override
    public Long inputLong() {
        long number = 0;
        if (sc.hasNextInt()) {
            number = sc.nextLong();
        }
        return number;
    }

    @Override
    public Long inputLongWithPrompt(String promptMessage) {
        print(promptMessage);
        return inputLong();
    }

    @Override
    public int inputIntWithPrompt(String promptMessage) {
        print(promptMessage);
        return inputLong().intValue();
    }
}
