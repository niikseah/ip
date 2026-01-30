import java.nio.file.Paths;

public class Ziq {

    private static final String FILE_PATH = Paths.get(".", "data", "ziq.txt").toString();

    private Storage storage;
    private TaskList tasks;
    private Ui ui;
    private Parser parser;

    public Ziq(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (ZiqException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    public void run() {
        ui.welcomeUser();
        boolean isDone = false;

        while (!isDone) {
            try {
                String commandLine = ui.readCommand();
                isDone = Parser.executeCommand(commandLine, tasks, ui, storage);
            } catch (ZiqException e) {
                ui.diagnoseError(e.getMessage());
            }
        }
        System.out.println("buh-bye!");
    }

    public static void main(String[] args) {
        new Ziq(FILE_PATH).run();
    }
}