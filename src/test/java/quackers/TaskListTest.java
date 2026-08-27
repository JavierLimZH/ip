package quackers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

class TaskListTest {
    @Test
    void find_mixedCaseKeyword_returnsMatchingDescriptionsInOriginalOrder() {
        Todo readBook = new Todo("read book");
        Event bookClub = new Event("attend Book club", "2pm", "4pm");
        TaskList tasks = new TaskList(List.of(
                readBook,
                new Deadline("return notes", LocalDate.of(2027, 6, 6)),
                bookClub));

        assertEquals(List.of(readBook, bookClub), tasks.find("BOOK"));
    }

    @Test
    void find_keywordOnlyInTaskDetails_doesNotMatch() {
        Deadline deadline = new Deadline("return novel", LocalDate.of(2027, 6, 6));
        Event event = new Event("meeting", "book room", "4pm");
        TaskList tasks = new TaskList(List.of(deadline, event));

        assertEquals(List.of(), tasks.find("book"));
    }
}
