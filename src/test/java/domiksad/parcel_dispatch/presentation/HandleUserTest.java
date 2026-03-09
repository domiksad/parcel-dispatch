package domiksad.parcel_dispatch.presentation;

import domiksad.parcel_dispatch.domain.presentation.HandleUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HandleUserTest {
    @Test
    void testParseCommandNormalSplit(){
        String input = "create person Adam Warszawa";

        ArrayList<String> result = HandleUser.parseCommand(input);

        assertEquals(4, result.size());
        assertEquals("create", result.get(0));
        assertEquals("person", result.get(1));
        assertEquals("Adam", result.get(2));
        assertEquals("Warszawa", result.get(3));
    }

    @Test
    void testParseCommandQuotesSplit(){
        String input = "create person \"Adam Kowalski\" \"Zielona 7\"";

        ArrayList<String> result = HandleUser.parseCommand(input);

        assertEquals(4, result.size());
        assertEquals("create", result.get(0));
        assertEquals("person", result.get(1));
        assertEquals("Adam Kowalski", result.get(2));
        assertEquals("Zielona 7", result.get(3));
    }

    @Test
    void testParseCommandEmpty(){
        ArrayList<String> result = HandleUser.parseCommand("");

        assertTrue(result.isEmpty());
    }

    @Test
    void testCreateDuplicates(){
        String inputData = """
        create person abc def
        create parcel 0 0 warsaw 12.2 high
        y
        create parcel 0 0 warsaw 12.2 high
        y
        y
        """;
        Scanner testScanner = new Scanner(inputData);
        HandleUser handleUser = new HandleUser(testScanner);

        // Capture out stream (ai)
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        handleUser.run();

        // Restore out stream
        System.setOut(originalOut);

        String actualOutput = outputStream.toString();

        assertTrue(actualOutput.contains("Person added at index: 0"));
        assertTrue(actualOutput.contains("""
                Parcel details:\s
                Sender: abc:def
                Receiver: abc:def
                Delivery city: warsaw
                Weight: 12,20
                Priority: HIGH
                Create parcel? (y)es | (n)o
                Parcel created with UUID:"""));
        assertTrue(actualOutput.contains("Parcel with these details already exists. Do you wish to proceed anyways? (y)es | (n)o"));
        assertTrue(actualOutput.contains("""
                Parcel details:\s
                Sender: abc:def
                Receiver: abc:def
                Delivery city: warsaw
                Weight: 12,20
                Priority: HIGH
                Create parcel? (y)es | (n)o
                Parcel created with UUID:"""));
    }
}
