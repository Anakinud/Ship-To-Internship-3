package com.idemia;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class SecondTests {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Test
    public void firstEasyTest() throws IOException {
        //given
        String input = "P X\n" +
                       "P X\n" +
                       "S 2\n" +
                       "P X\n" +
                       "P X\n";

        //when
        App.testableMethod(
                "src/test/resources/input2-1.txt",
                new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)),
                new PrintStream(outContent));

        //then
        assertThat(outContent.toString()).isEqualTo("|X XX X|\n" +
                                                    "|X XX X|\n" +
                                                    "|X XO X|\n" +
                                                    "|X XX X|\n");
    }

    @Test
    public void multipleChanges() throws IOException {
        //given
        String input = "P X\n" +
                       "P X\n" +
                       "S 2\n" +
                       "P X\n" +
                       "P X\n" +
                       "S 1\n" +
                       "P X\n" +
                       "P X\n" +
                       "S 2\n" +
                       "P X\n" +
                       "P X\n" +
                       "S 1\n" +
                       "P X\n" +
                       "P X\n";

        //when
        App.testableMethod(
                "src/test/resources/input2-2.txt",
                new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)),
                new PrintStream(outContent));

        //then
        assertThat(outContent.toString()).isEqualTo("|X XX X|\n" +
                                                    "|X XX X|\n" +
                                                    "|X XX X|\n" +
                                                    "|X XX X|\n" +
                                                    "|X XO O|\n" +
                                                    "|O OO X|\n" +
                                                    "|X XX X|\n" +
                                                    "|X XX X|\n");
    }

    @Test
    public void takeShouldStillWork() throws IOException {
        //given
        String input = "P X\n" +
                       "P X\n" +
                       "S 2\n" +
                       "P X\n" +
                       "P X\n" +
                       "T 4:4\n" +
                       "S 1\n" +
                       "P X\n" +
                       "P X\n" +
                       "S 2\n" +
                       "P X\n" +
                       "T 8:4\n" +
                       "P X\n" +
                       "S 1\n" +
                       "P X\n";

        //when
        App.testableMethod(
                "src/test/resources/input2-2.txt",
                new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)),
                new PrintStream(outContent));

        //then
        assertThat(outContent.toString()).isEqualTo("|X XX X|\n" +
                                                    "|X XX X|\n" +
                                                    "|X XX X|\n" +
                                                    "|X XX O|\n" +
                                                    "|X OO O|\n" +
                                                    "|O OO O|\n" +
                                                    "|X XX X|\n" +
                                                    "|X XX X|\n");
    }
}