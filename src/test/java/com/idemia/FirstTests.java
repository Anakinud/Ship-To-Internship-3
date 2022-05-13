package com.idemia;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class FirstTests {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Test
    public void firstEasyTest() throws IOException {
        //given
        String input = "P X\n" +
                       "P X";

        //when
        App.testableMethod(
                "src/test/resources/input1-1.txt",
                new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)),
                new PrintStream(outContent));

        //then
        assertThat(outContent.toString()).isEqualTo("|X XO O|\n" +
                                                    "|O OO O|\n" +
                                                    "|O OO O|\n" +
                                                    "|O OO O|\n");
    }

    @Test
    public void partiallyFilledPlane() throws IOException {
        //given
        String input = "P X\n" +
                       "P X\n" +
                       "P X\n" +
                       "P X\n" +
                       "P X";

        //when
        App.testableMethod(
                "src/test/resources/input1-2.txt",
                new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)),
                new PrintStream(outContent));

        //then
        assertThat(outContent.toString()).isEqualTo("|X XX X|\n" +
                                                    "|X XX X|\n" +
                                                    "|X XX X|\n" +
                                                    "|X XX X|\n");
    }

    @Test
    public void take() throws IOException {
        //given
        String input = "T 4:3\n" +
                       "T 4:4\n" +
                       "P X\n" +
                       "T 2:2\n";

        //when
        App.testableMethod(
                "src/test/resources/input1-2.txt",
                new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)),
                new PrintStream(outContent));

        //then
        assertThat(outContent.toString()).isEqualTo("|X XX O|\n" +
                                                    "|X OX X|\n" +
                                                    "|X XO X|\n" +
                                                    "|O OO O|\n");
    }

    @Test
    public void takeLongAisle() throws IOException {
        //given
        String input = "T 4:3\n" +
                       "P X\n" +
                       "P X\n" +
                       "T 1:3\n";

        //when
        App.testableMethod(
                "src/test/resources/input1-3.txt",
                new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)),
                new PrintStream(outContent));

        //then
        assertThat(outContent.toString()).isEqualTo("|X X  O|\n" +
                                                    "|X X  X|\n" +
                                                    "|X X  X|\n" +
                                                    "|O O  O|\n");
    }

    @Test
    public void probablyFailure() throws IOException {
        //given
        String input = "P X\n" +
                       "P X\n" +
                       "T 1:1\n" +
                       "T 1:1\n" +
                       "P X\n" +
                       "P X\n";

        //when
        App.testableMethod(
                "src/test/resources/input1-4.txt",
                new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)),
                new PrintStream(outContent));

        //then
        assertThat(outContent.toString()).isEqualTo("|X X|\n" +
                                                    "|X X|\n");
    }
}