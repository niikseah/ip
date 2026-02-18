package ziq;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ParserTest {

    private TaskList tasks;
    private Ui ui;
    private Storage storage;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    public void setUp() {
        tasks = new TaskList();
        ui = new Ui();
        storage = new MockStorage();
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(System.out);
    }

    @Test
    public void executeCommand_bye_returnsTrue() throws ZiqException {
        assertTrue(Parser.executeCommand("bye", tasks, ui, storage));
    }

    @Test
    public void executeCommand_list_printsTasks() throws ZiqException {
        tasks.add(new Todo("task1"), new Todo("task2"));
        Parser.executeCommand("list", tasks, ui, storage);
        String output = outputStream.toString();
        assertTrue(output.contains("here is your to-do list!"));
        assertTrue(output.contains("task1"));
        assertTrue(output.contains("task2"));
    }

    @Test
    public void executeCommand_emptyInput_throwsException() {
        assertThrows(ZiqException.class, () -> Parser.executeCommand("   ", tasks, ui, storage));
    }

    @Test
    public void executeCommand_unknownCommand_throwsException() {
        assertThrows(ZiqException.class, () -> Parser.executeCommand("invalid", tasks, ui, storage));
    }

    @Test
    public void executeCommand_todo_addsTask() throws ZiqException {
        Parser.executeCommand("todo read book", tasks, ui, storage);
        assertEquals(1, tasks.size());
        assertEquals("read book", tasks.get(0).description());
    }

    @Test
    public void executeCommand_todo_emptyDescription_throwsException() {
        assertThrows(ZiqException.class, () -> Parser.executeCommand("todo", tasks, ui, storage));
    }

    @Test
    public void executeCommand_todo_onlySpaces_throwsException() {
        assertThrows(ZiqException.class, () -> Parser.executeCommand("todo   ", tasks, ui, storage));
    }

    @Test
    public void executeCommand_todo_normalizesSpaces() throws ZiqException {
        Parser.executeCommand("todo   read   book", tasks, ui, storage);
        assertEquals("read book", tasks.get(0).description());
    }

    @Test
    public void executeCommand_deadline_addsTask() throws ZiqException {
        Parser.executeCommand("deadline submit /by 22/2/2022 1200", tasks, ui, storage);
        assertEquals(1, tasks.size());
        assertTrue(tasks.get(0) instanceof Deadline);
    }

    @Test
    public void executeCommand_deadline_missingBy_throwsException() {
        assertThrows(ZiqException.class, () -> Parser.executeCommand("deadline submit", tasks, ui, storage));
    }

    @Test
    public void executeCommand_deadline_duplicateBy_throwsException() {
        assertThrows(ZiqException.class, () -> Parser.executeCommand(
                "deadline submit /by 22/2/2022 1200 /by 23/2/2022 1200", tasks, ui, storage));
    }

    @Test
    public void executeCommand_deadline_invalidDate_throwsException() {
        assertThrows(ZiqException.class, () -> Parser.executeCommand(
                "deadline submit /by 32/1/2022 1200", tasks, ui, storage));
    }

    @Test
    public void executeCommand_deadline_emptyDescription_throwsException() {
        assertThrows(ZiqException.class, () -> Parser.executeCommand("deadline /by 22/2/2022 1200", tasks, ui, storage));
    }

    @Test
    public void executeCommand_event_addsTask() throws ZiqException {
        Parser.executeCommand("event meeting /from 22/2/2022 1200 /to 22/2/2022 1400", tasks, ui, storage);
        assertEquals(1, tasks.size());
        assertTrue(tasks.get(0) instanceof Event);
    }

    @Test
    public void executeCommand_event_startAfterEnd_throwsException() {
        assertThrows(ZiqException.class, () -> Parser.executeCommand(
                "event meeting /from 22/2/2022 1400 /to 22/2/2022 1200", tasks, ui, storage));
    }

    @Test
    public void executeCommand_event_startEqualsEnd_throwsException() {
        assertThrows(ZiqException.class, () -> Parser.executeCommand(
                "event meeting /from 22/2/2022 1200 /to 22/2/2022 1200", tasks, ui, storage));
    }

    @Test
    public void executeCommand_event_missingFrom_throwsException() {
        assertThrows(ZiqException.class, () -> Parser.executeCommand(
                "event meeting /to 22/2/2022 1400", tasks, ui, storage));
    }

    @Test
    public void executeCommand_event_duplicateFrom_throwsException() {
        assertThrows(ZiqException.class, () -> Parser.executeCommand(
                "event meeting /from 22/2/2022 1200 /from 22/2/2022 1300 /to 22/2/2022 1400", tasks, ui, storage));
    }

    @Test
    public void executeCommand_mark_validIndex() throws ZiqException {
        tasks.add(new Todo("task"));
        Parser.executeCommand("mark 1", tasks, ui, storage);
        assertTrue(tasks.get(0).getStatus().equals("X"));
    }

    @Test
    public void executeCommand_mark_missingNumber_throwsException() {
        tasks.add(new Todo("task"));
        assertThrows(ZiqException.class, () -> Parser.executeCommand("mark", tasks, ui, storage));
    }

    @Test
    public void executeCommand_mark_invalidIndex_throwsException() {
        tasks.add(new Todo("task"));
        // mark 0 is rejected before reaching TaskList.get (index < 1 check)
        assertThrows(ZiqException.class, () -> Parser.executeCommand("mark 0", tasks, ui, storage));
        // mark 2 causes IndexOutOfBoundsException in TaskList.get which is caught and rethrown as ZiqException
        assertThrows(ZiqException.class, () -> Parser.executeCommand("mark 2", tasks, ui, storage));
    }

    @Test
    public void executeCommand_mark_nonNumeric_throwsException() {
        tasks.add(new Todo("task"));
        assertThrows(ZiqException.class, () -> Parser.executeCommand("mark abc", tasks, ui, storage));
    }

    @Test
    public void executeCommand_mark_normalizesSpaces() throws ZiqException {
        tasks.add(new Todo("task"));
        Parser.executeCommand("mark   1", tasks, ui, storage);
        assertTrue(tasks.get(0).getStatus().equals("X"));
    }

    @Test
    public void executeCommand_unmark_validIndex() throws ZiqException {
        tasks.add(new Todo("task"));
        tasks.get(0).markAsDone();
        Parser.executeCommand("unmark 1", tasks, ui, storage);
        assertTrue(tasks.get(0).getStatus().equals(" "));
    }

    @Test
    public void executeCommand_delete_validIndex() throws ZiqException {
        tasks.add(new Todo("task"));
        Parser.executeCommand("delete 1", tasks, ui, storage);
        assertEquals(0, tasks.size());
    }

    @Test
    public void executeCommand_delete_missingNumber_throwsException() {
        tasks.add(new Todo("task"));
        assertThrows(ZiqException.class, () -> Parser.executeCommand("delete", tasks, ui, storage));
    }

    @Test
    public void executeCommand_delete_invalidIndex_throwsException() {
        tasks.add(new Todo("task"));
        assertThrows(ZiqException.class, () -> Parser.executeCommand("delete 0", tasks, ui, storage));
        assertThrows(ZiqException.class, () -> Parser.executeCommand("delete 2", tasks, ui, storage));
    }

    @Test
    public void executeCommand_find_matchingTasks() throws ZiqException {
        tasks.add(new Todo("read book"), new Todo("write book"), new Todo("exercise"));
        Parser.executeCommand("find book", tasks, ui, storage);
        String output = outputStream.toString();
        assertTrue(output.contains("matching tasks"));
        assertTrue(output.contains("read book"));
        assertTrue(output.contains("write book"));
        assertFalse(output.contains("exercise"));
    }

    @Test
    public void executeCommand_find_emptyKeyword_listsAll() throws ZiqException {
        tasks.add(new Todo("task1"), new Todo("task2"));
        Parser.executeCommand("find", tasks, ui, storage);
        String output = outputStream.toString();
        assertTrue(output.contains("here is your to-do list!"));
    }

    @Test
    public void executeCommand_schedule_validDate() throws ZiqException {
        LocalDateTime by = LocalDateTime.of(2022, 2, 22, 12, 0);
        tasks.add(new Deadline("submit", by));
        Parser.executeCommand("schedule 22/2/2022", tasks, ui, storage);
        String output = outputStream.toString();
        assertTrue(output.contains("schedule"));
        assertTrue(output.contains("submit"));
    }

    @Test
    public void executeCommand_schedule_invalidDate_throwsException() {
        assertThrows(ZiqException.class, () -> Parser.executeCommand("schedule 32/1/2022", tasks, ui, storage));
    }

    @Test
    public void executeCommand_schedule_missingDate_throwsException() {
        assertThrows(ZiqException.class, () -> Parser.executeCommand("schedule", tasks, ui, storage));
    }

    @Test
    public void executeCommand_duplicateTask_throwsException() throws ZiqException {
        Parser.executeCommand("todo read book", tasks, ui, storage);
        assertThrows(ZiqException.class, () -> Parser.executeCommand("todo read book", tasks, ui, storage));
    }

    @Test
    public void executeCommand_duplicateDeadline_throwsException() throws ZiqException {
        Parser.executeCommand("deadline submit /by 22/2/2022 1200", tasks, ui, storage);
        assertThrows(ZiqException.class, () -> Parser.executeCommand(
                "deadline submit /by 22/2/2022 1200", tasks, ui, storage));
    }

    @Test
    public void executeCommand_duplicateEvent_throwsException() throws ZiqException {
        Parser.executeCommand("event meeting /from 22/2/2022 1200 /to 22/2/2022 1400", tasks, ui, storage);
        assertThrows(ZiqException.class, () -> Parser.executeCommand(
                "event meeting /from 22/2/2022 1200 /to 22/2/2022 1400", tasks, ui, storage));
    }

    @Test
    public void executeCommand_help_displaysCommands() throws ZiqException {
        Parser.executeCommand("help", tasks, ui, storage);
        String output = outputStream.toString();
        assertTrue(output.contains("commands available"));
        assertTrue(output.contains("todo"));
        assertTrue(output.contains("deadline"));
        assertTrue(output.contains("event"));
    }

    private static class MockStorage extends Storage {
        public MockStorage() {
            super("test.txt", new Ui());
        }

        @Override
        public void save(java.util.ArrayList<Task> list) throws ZiqException {
            // Mock: do nothing
        }
    }
}
