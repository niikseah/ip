package ziq;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TaskListTest {

    @Test
    public void editabilityTest() throws ZiqException {
        TaskList list = new TaskList();
        list.add(new Todo("first"));
        list.add(new Todo("second"));
        Task removed = list.delete(0);
        assertEquals("first", removed.description());
        assertEquals(1, list.size());
    }

    @Test
    public void deleteInvalidIndex() {
        TaskList list = new TaskList();
        list.add(new Todo("only one"));
        assertThrows(ZiqException.class, () -> list.delete(5));
    }
}
