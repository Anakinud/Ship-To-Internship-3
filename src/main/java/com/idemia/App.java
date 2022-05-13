package com.idemia;

import com.idemia.model.Plane;
import com.idemia.parser.StringParser;
import com.idemia.transformer.StringRetrieveCommand;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String... args) throws IOException {
        //readFromFile
        testableMethod(args[0], System.in, System.out);
    }

    protected static void testableMethod(String arg, InputStream inputStream, PrintStream outputStream) throws IOException {
        List<String> input = Files.readAllLines(Paths.get(arg));
        StringParser parser = new StringParser();
        Plane plane = parser.parse(input);
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if ("EXIT".equals(line)) {
                break;
            }
            plane.execute(parser.parseCommand(line));
        }
        outputStream.print(plane.execute(new StringRetrieveCommand()));
    }

}
