import java.util.ArrayList;

public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public void add(Task t) {
        tasks.add(t);
    }

    public ArrayList<Task> getTaskList() {
        return this.tasks;
    }

    public Task delete(int index) throws ZiqException {
        if (index < 0 || index >= tasks.size())
        {
            throw new ZiqException("task does not exist");
        }
        return tasks.remove(index);
    }

    public Task get(int index) {
        return tasks.get(index);
    }

    public int size() {
        return tasks.size();
    }
}