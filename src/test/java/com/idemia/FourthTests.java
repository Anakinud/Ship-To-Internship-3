package com.idemia;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class FourthTests {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Test
    public void firstEasyTest() throws IOException {
        //given
        String input = "P V\n" +
                       "P V\n" +
                       "P V\n" +
                       "P V\n" +
                       "P V\n" +
                       "P V\n" +
                       "P V\n" +
                       "P V\n" +
                       "P V\n" +
                       "P V\n" +
                       "P V\n" +
                       "P V\n" +
                       "P V\n" +
                       "P V\n" +
                       "P V\n" +
                       "P V\n" +
                       "P V\n" +
                       "P V\n" +
                       "P V\n";

        //when
        App.testableMethod(
                "src/test/resources/input4-1.txt",
                new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)),
                new PrintStream(outContent));

        //then
        assertThat(outContent.toString()).isEqualTo("|VO VOV VOV VO|\n" +
                                                    "|OO OOO OOO OO|\n" +
                                                    "|VO VOV VOV VO|\n" +
                                                    "|OO OOO OOO OO|\n" +
                                                    "|VO VOV VOV VO|\n" +
                                                    "|OO OOO OOO OO|\n");
    }

    @Test
    public void testWithPreference() throws IOException {
        //given
        String input = "P V:W\n" +
                       "P V:W\n" +
                       "P V:A\n" +
                       "P V\n" +
                       "P V:A\n" +
                       "P V:A\n" +
                       "P V:A\n" +
                       "P V:A\n" +
                       "P V:A\n" +
                       "P V:W\n" +
                       "P V:W\n" +
                       "P V:W\n" +
                       "P V:W\n";

        //when
        App.testableMethod(
                "src/test/resources/input4-2.txt",
                new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)),
                new PrintStream(outContent));

        //then
        assertThat(outContent.toString()).isEqualTo("|VO VOVO VOV OV|\n" +
                                                    "|OO OOOO OOO OO|\n" +
                                                    "|OV VOOV VOO OV|\n" +
                                                    "|OO OOOO OOO OO|\n" +
                                                    "|VO OOOO OOO OV|\n" +
                                                    "|OO OOOO OOO OO|\n");
    }

    @Test
    public void differentOrder() throws IOException {
        //given
        String input = "P V:W\n" +
                       "P V:W\n" +
                       "P V:A\n" +
                       "S 2\n" +
                       "P V\n" +
                       "P V:A\n" +
                       "P V:A\n" +
                       "P V:A\n" +
                       "P V:A\n" +
                       "S 1\n" +
                       "P V:A\n" +
                       "P V:W\n" +
                       "P V:W\n" +
                       "P V:W\n" +
                       "P V:A\n" +
                       "S 2\n" +
                       "P V:A\n" +
                       "P V:A\n" +
                       "P V:A\n";

        //when
        App.testableMethod(
                "src/test/resources/input4-2.txt",
                new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)),
                new PrintStream(outContent));

        //then
        assertThat(outContent.toString()).isEqualTo("|VO VOOV VOO OV|\n" +
                                                    "|OO OOOO OOO OO|\n" +
                                                    "|VO OOOO OOO OV|\n" +
                                                    "|OO OOOV VOV OO|\n" +
                                                    "|VO OOOO OOO OO|\n" +
                                                    "|OO VOOV VOV OV|\n");
    }

    @Test
    public void vipWithOrdinaryHoomans() throws IOException {
        //given
        String input = "P X\n" +
                       "P V\n" +
                       "P X\n" +
                       "P V\n" +
                       "P X:W\n" +
                       "P X\n" +
                       "P V:A\n" +
                       "P X\n" +
                       "S 2\n" +
                       "P X\n" +
                       "P V\n" +
                       "P X\n" +
                       "P V\n" +
                       "P X:W\n" +
                       "P X\n" +
                       "P V\n" +
                       "P X\n" +
                       "P X\n" +
                       "P V:A\n" +
                       "S 1\n" +
                       "P V\n" +
                       "P X\n" +
                       "P X\n" +
                       "P V\n";

        //when
        App.testableMethod(
                "src/test/resources/input4-2.txt",
                new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)),
                new PrintStream(outContent));

        //then
        assertThat(outContent.toString()).isEqualTo("|XX VOVO XOV XX|\n" +
                                                    "|OO OOOO XOO XO|\n" +
                                                    "|VO VOOO OOO OO|\n" +
                                                    "|OO OOOO OOV OO|\n" +
                                                    "|OO OOOO OOO OX|\n" +
                                                    "|XX OVOX VOV XX|\n");
    }

    @Test
    public void shouldParseProperly() throws IOException {
        //given
        String input = "";

        //when
        App.testableMethod(
                "src/test/resources/input4-3.txt",
                new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)),
                new PrintStream(outContent));

        //then
        assertThat(outContent.toString()).isEqualTo("|VO VOXX VOV VO|\n" +
                                                    "|OO OOOO OOO OO|\n" +
                                                    "|OX VOOO XOO OO|\n" +
                                                    "|OO OOOO OOO OO|\n" +
                                                    "|OO OOOO OOO OO|\n" +
                                                    "|OO OOOV OVO OV|\n");
    }

    @Test
    public void shouldAddProperly() throws IOException {
        //given
        String input = "P V\n" +
                       "P X:A\n" +
                       "S 2\n" +
                       "P X\n" +
                       "P X:W\n" +
                       "P V\n";

        //when
        App.testableMethod(
                "src/test/resources/input4-3.txt",
                new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)),
                new PrintStream(outContent));

        //then
        assertThat(outContent.toString()).isEqualTo("|VO VOXX VOV VO|\n" +
                                                    "|OO OOOO OOO OO|\n" +
                                                    "|OX VOVO XOX OO|\n" +
                                                    "|OO OOOO OOO OV|\n" +
                                                    "|OO OOOO OOO OO|\n" +
                                                    "|XO OXOV OVO OV|\n");
    }

    @Test
    public void shouldProperlyTakeVip() throws IOException {
        //given
        String input = "T 1:3\n" +
                       "P X\n" +
                       "P X\n" +
                       "P X\n";

        //when
        App.testableMethod(
                "src/test/resources/input4-3.txt",
                new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)),
                new PrintStream(outContent));

        //then
        assertThat(outContent.toString()).isEqualTo("|VO XXXX VOV VO|\n" +
                                                    "|OO OOXO OOO OO|\n" +
                                                    "|OX VOOO XOO OO|\n" +
                                                    "|OO OOOO OOO OO|\n" +
                                                    "|OO OOOO OOO OO|\n" +
                                                    "|OO OOOV OVO OV|\n");
    }
}