package com.idemia;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class App {

    public static void main(String... args) throws IOException {
        List<String> input = Files.readAllLines(Paths.get(args[0]));
        for (var line : input) {
            //Remove me and start development
            System.out.println(line);
        }

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if ("EXIT".equals(line)) {
                break;
            }
            System.out.println(line);
        }
    }

}