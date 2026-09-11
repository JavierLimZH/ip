package quackers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class ParserTest {
    @Test
    void parseCommandType_supportedCommands_returnsMatchingTypes() throws QuackersException {
        assertEquals(CommandType.BYE, Parser.parseCommandType("bye"));
        assertEquals(CommandType.LIST, Parser.parseCommandType("list"));
        assertEquals(CommandType.FIND, Parser.parseCommandType("find book"));
        assertEquals(CommandType.MARK, Parser.parseCommandType("mark 1"));
        assertEquals(CommandType.UNMARK, Parser.parseCommandType("unmark 1"));
        assertEquals(CommandType.DELETE, Parser.parseCommandType("delete 1"));
        assertEquals(CommandType.TODO, Parser.parseCommandType("todo read book"));
        assertEquals(CommandType.DEADLINE,
                Parser.parseCommandType("deadline return book /by 2027-02-28"));
        assertEquals(CommandType.EVENT,
                Parser.parseCommandType("event meeting /from 2pm /to 4pm"));
    }

    @Test
    void parseCommandType_unknownCommand_exceptionThrown() {
        QuackersException error = assertThrows(
                QuackersException.class, () -> Parser.parseCommandType("quack loudly"));

        assertEquals("Quack? I don't know what that means :-(", error.getMessage());
    }

    @Test
    void parseDeadline_validIsoDate_returnsTypedDeadline() throws QuackersException {
        Deadline deadline = Parser.parseDeadline("deadline submit report /by 2027-02-28");

        assertEquals(LocalDate.of(2027, 2, 28), deadline.getDueDate());
        assertEquals("[D][ ] submit report (by: Feb 28 2027)", deadline.toString());
    }

    @Test
    void parseDeadline_invalidDate_exceptionThrown() {
        QuackersException error = assertThrows(
                QuackersException.class, () -> Parser.parseDeadline("deadline submit report /by 2027-02-29"));

        assertEquals("Quack? Use yyyy-MM-dd for the deadline date.", error.getMessage());
    }

    @Test
    void parseTaskIndex_nonNumericIndex_exceptionThrown() {
        QuackersException error = assertThrows(
                QuackersException.class, () -> Parser.parseTaskIndex("delete first", "delete"));

        assertEquals("Quack? Please enter a valid task number.", error.getMessage());
    }

    @Test
    void parseFindKeyword_missingKeyword_exceptionThrown() {
        QuackersException error = assertThrows(
                QuackersException.class, () -> Parser.parseFindKeyword("find"));

        assertEquals("Quack? Give me a keyword to find!", error.getMessage());
    }
}
