package com.idemia;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class ThirdTests {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Test
    public void firstEasyTest() throws IOException {
        //given
        String input = "P X:W\n" +
                       "P X:A\n";

        //when
        App.testableMethod(
                "src/test/resources/input3-1.txt",
                new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)),
                new PrintStream(outContent));

        //then
        assertThat(outContent.toString()).isEqualTo("|X XX X|\n" +
                                                    "|X XX X|\n" +
                                                    "|X XO X|\n" +
                                                    "|O OX X|\n" +
                                                    "|X OO O|\n" +
                                                    "|O OO O|\n" +
                                                    "|X XO X|\n" +
                                                    "|O OX X|\n");
    }

    @Test
    public void firstDifferentOrder() throws IOException {
        //given
        String input = "P X:A\n" +
                       "P X:W\n";

        //when
        App.testableMethod(
                "src/test/resources/input3-2.txt",
                new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)),
                new PrintStream(outContent));

        //then
        assertThat(outContent.toString()).isEqualTo("|X XX X|\n" +
                                                    "|X XX X|\n" +
                                                    "|X XX X|\n" +
                                                    "|X OX X|\n" +
                                                    "|X OO X|\n" +
                                                    "|O OO O|\n" +
                                                    "|X XO X|\n" +
                                                    "|O OX X|\n");
    }

    @Test
    public void differentOrder() throws IOException {
        //given
        String input = "S 2\n" +
                       "P X:A\n" +
                       "P X:W\n" +
                       "S 1\n" +
                       "P X:A\n" +
                       "P X:W\n";

        //when
        App.testableMethod(
                "src/test/resources/input3-3.txt",
                new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)),
                new PrintStream(outContent));

        //then
        assertThat(outContent.toString()).isEqualTo("|XX XXO OOO OX|\n" +
                                                    "|OO OOO OOO OO|\n" +
                                                    "|OX OXO OOO OX|\n" +
                                                    "|OX OXO OOO OX|\n" +
                                                    "|XX XXX OOO XX|\n" +
                                                    "|XX OXO OOO XX|\n");
    }

    @Test
    public void noPlaceForPreference() throws IOException {
        //given
        String input = "P X:W\n" +
                       "P X:W\n" +
                       "P X:W\n" +
                       "P X:W\n" +
                       "P X:W\n" +
                       "P X:W\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                        //Below commands should ignore preference
                       "P X:A\n" +
                       "P X:A\n";

        //when
        App.testableMethod(
                "src/test/resources/input3-3.txt",
                new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)),
                new PrintStream(outContent));

        //then
        assertThat(outContent.toString()).isEqualTo("|XX XXX XXX XX|\n" +
                                                    "|XX XXX XOX XX|\n" +
                                                    "|XX XXX XOX XX|\n" +
                                                    "|XX XXX XOX XX|\n" +
                                                    "|XX XXX XOX XX|\n" +
                                                    "|XX XXX XOX XX|\n");
    }

    @Test
    public void noPlaceForPreferenceDifferentOrder() throws IOException {
        //given
        String input = "S 2\n" +
                       "P X:W\n" +
                       "P X:W\n" +
                       "P X:W\n" +
                       "P X:W\n" +
                       "P X:W\n" +
                       "P X:W\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       "P X:A\n" +
                       //Below commands should ignore preference
                       "P X:A\n" +
                       "P X:A\n";

        //when
        App.testableMethod(
                "src/test/resources/input3-3.txt",
                new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)),
                new PrintStream(outContent));

        //then
        assertThat(outContent.toString()).isEqualTo("|XX XXX XOX XX|\n" +
                                                    "|XX XOX XOX XX|\n" +
                                                    "|XX XXX XOX XX|\n" +
                                                    "|XX XXX XOX XX|\n" +
                                                    "|XX XXX XXX XX|\n" +
                                                    "|XX XXX XXX XX|\n");
    }
}