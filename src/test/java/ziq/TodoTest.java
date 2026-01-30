package ziq;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TodoTest {

    @Test
    public void todoHasCorrectDescription() {
        Todo t = new Todo("read book");
        assertEquals("read book", t.description());
    }

    @Test
    public void todoToString_showsDoneWhenMarked() {
        Todo t = new Todo("read book");
        assertEquals("[T][ ] read book", t.toString());
        t.markAsDone();
        assertEquals("[T][X] read book", t.toString());
    }
}
