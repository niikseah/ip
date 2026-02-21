# ziq user guide

<p align="center">
  <img src="Ui.png" width="400" alt="Product screenshot" />
</p>

ziq is a user-friendly task management chatbot that helps you keep track of your todos, deadlines, and events. 

## Quick Start

1. Launch the application
2. Type a command in the input field
3. Press Enter or click Send
4. ziq will update you on the system status

## Features

### Adding Tasks

<details>
<summary><strong>todo</strong> — Add a todo task</summary>

Add a simple todo task without any date or time.

**Format:** `todo <description>`

**Example:** `todo read book`

</details>

<details>
<summary><strong>deadline</strong> — Add a deadline task</summary>

Add a deadline task with a due date. Time is optional - if not specified, it defaults to midnight (00:00).

**Format:** `deadline <description> /by DDMMYYYY [HHmm]`

**Examples:**
- `deadline submit report /by 22022022 1200`
- `deadline finish assignment /by 15032022`

</details>

<details>
<summary><strong>event</strong> — Add an event task</summary>

Add an event task with a start and end time.

**Format:** `event <description> /from DDMMYYYY HHmm /to DDMMYYYY HHmm`

**Example:** `event meeting /from 22022022 1200 /to 22022022 1400`

</details>

### Viewing Tasks

<details>
<summary><strong>list</strong> — List all tasks</summary>

View all tasks in your list.

**Format:** `list`

</details>

### Managing Tasks

<details>
<summary><strong>mark</strong> — Mark a task as done</summary>

Mark a task as completed.

**Format:** `mark <index>`

**Example:** `mark 1`

</details>

<details>
<summary><strong>unmark</strong> — Mark a task as not done</summary>

Mark a completed task as not done.

**Format:** `unmark <index>`

**Example:** `unmark 1`

</details>

<details>
<summary><strong>delete</strong> — Remove a task</summary>

Remove a task from your list.

**Format:** `delete <index>`

**Example:** `delete 2`

</details>

<details>
<summary><strong>clear</strong> — Remove all tasks</summary>

Remove all tasks from your list at once.

**Format:** `clear`

</details>

### Finding Tasks

<details>
<summary><strong>find</strong> — Search tasks by keyword</summary>

Find tasks that contain a specific keyword in their description.

**Format:** `find <keyword>`

**Example:** `find book`

</details>

### Tagging Tasks

<details>
<summary><strong>tag</strong> — Add a tag to a task</summary>

Add a tag to any task (todo, deadline, or event) for better organization.

**Format:** `tag <index> <tag>`

**Example:** `tag 1 work`

</details>

### Organizing Tasks

<details>
<summary><strong>organise tag</strong> — Group tasks by tag</summary>

Group and display tasks organized by their tags. Tasks with the same tag are grouped together, and untagged tasks are shown separately.

**Format:** `organise tag`

**Example:** `organise tag`

</details>

<details>
<summary><strong>organise deadline</strong> — Order tasks by deadline</summary>

Display tasks organized by deadline, with deadlines sorted by due date, followed by events sorted by start date, and finally todos.

**Format:** `organise deadline`

**Example:** `organise deadline`

</details>

### Viewing Schedule

<details>
<summary><strong>schedule</strong> — View tasks on a date</summary>

View all tasks (deadlines and events) scheduled for a particular date.

**Format:** `schedule DDMMYYYY` (or `DD` for day only, `DDMM` for day/month only)

**Example:** `schedule 22022022` or `schedule 22` or `schedule 2202`

</details>

### Getting Help

<details>
<summary><strong>help</strong> — Show available commands</summary>

Display a list of all available commands.

**Format:** `help`

</details>

### Exiting the Application

<details>
<summary><strong>bye</strong> — Exit the application</summary>

Close the application.

**Format:** `bye`

</details>

## Command Summary

| Command | Format | Description |
|---------|--------|-------------|
| `todo` | `todo <description>` | Add a todo task |
| `deadline` | `deadline <description> /by DDMMYYYY [HHmm]` | Add a deadline task |
| `event` | `event <description> /from DDMMYYYY HHmm /to DDMMYYYY HHmm` | Add an event task |
| `list` | `list` | List all tasks |
| `mark` | `mark <index>` | Mark a task as done |
| `unmark` | `unmark <index>` | Mark a task as not done |
| `delete` | `delete <index>` | Delete a task |
| `find` | `find <keyword>` | Find tasks by keyword |
| `schedule` | `schedule DDMMYYYY` (or `DD`, `DDMM`) | View tasks on a date |
| `tag` | `tag <index> <tag>` | Add a tag to a task |
| `organise` | `organise tag` or `organise deadline` | Organize tasks by tag or deadline |
| `clear` | `clear` | Remove all tasks |
| `help` | `help` | Show help message |
| `bye` | `bye` | Exit the application |

## Notes

- Task indices start from 1 (not 0)
- Dates use `DDMMYYYY` format with no slashes (e.g., `22022022` for February 22, 2022)
- For the `schedule` command, you can use partial dates:
  - `DD` - uses current month and year (e.g., `22` for the 22nd of the current month)
  - `DDMM` - uses current year (e.g., `2202` for February 22 of the current year)
  - `DDMMYYYY` - full date (e.g., `22022022`)
- Time format is `HHmm` (24-hour format, e.g., `1200` for 12:00 PM)
- If no time is specified for a deadline, only the date is displayed 
- Tasks are automatically saved to `data/ziq.txt`
- Duplicate tasks (same description and details) are not allowed
- Tags are color-coded for easy identification
- Command keywords in your input are color-coded to match their task types (todo=green, deadline=red, event=yellow, tag=purple, organise=orange)
