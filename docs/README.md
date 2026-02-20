# Ziq User Guide

// Product screenshot goes here

Ziq is a friendly task management chatbot that helps you keep track of your todos, deadlines, and events. With a simple chat interface, you can add, manage, and organize your tasks effortlessly.

## Quick Start

1. Launch the application
2. Type a command in the input field
3. Press Enter or click Send
4. Ziq will respond and help you manage your tasks!

## Features

### Adding Tasks

#### Adding a Todo

Add a simple todo task without any date or time.

**Format:** `todo <description>`

**Example:** `todo read book`

**Expected output:**
```
task added:
  [ ] read book
now you have 1 task(s) in the list.
```

#### Adding a Deadline

Add a deadline task with a due date. Time is optional - if not specified, it defaults to midnight (00:00).

**Format:** `deadline <description> /by DD/MM/YYYY [HHmm]`

**Examples:**
- `deadline submit report /by 22/02/2022 1200`
- `deadline finish assignment /by 15/03/2022`

**Expected output:**
```
task added:
  [ ] submit report (by: Feb 22 2022, 12:00 PM)
now you have 2 task(s) in the list.
```

#### Adding an Event

Add an event task with a start and end time.

**Format:** `event <description> /from d/M/yyyy HHmm /to d/M/yyyy HHmm`

**Example:** `event meeting /from 2/22/2022 1200 /to 2/22/2022 1400`

**Expected output:**
```
task added:
  [ ] meeting (from: Feb 22 2022, 12:00 PM to: Feb 22 2022, 2:00 PM)
now you have 3 task(s) in the list.
```

### Viewing Tasks

#### Listing All Tasks

View all tasks in your list.

**Format:** `list`

**Expected output:**
```
here is your to-do list!
1. [ ] read book
2. [ ] submit report (by: Feb 22 2022, 12:00 PM)
3. [ ] meeting (from: Feb 22 2022, 12:00 PM to: Feb 22 2022, 2:00 PM)
```

If your list is empty:
```
you don't have anything on your list right now!
```

### Managing Tasks

#### Marking a Task as Done

Mark a task as completed.

**Format:** `mark <index>`

**Example:** `mark 1`

**Expected output:**
```
task marked as done:
  [✅] read book
```

#### Unmarking a Task

Mark a completed task as not done.

**Format:** `unmark <index>`

**Example:** `unmark 1`

**Expected output:**
```
task marked as not done:
  [ ] read book
```

#### Deleting a Task

Remove a task from your list.

**Format:** `delete <index>`

**Example:** `delete 2`

**Expected output:**
```
task removed:
  [ ] submit report (by: Feb 22 2022, 12:00 PM)
now you have 2 task(s) in the list.
```

#### Clearing All Tasks

Remove all tasks from your list at once.

**Format:** `clear`

**Expected output:**
```
all tasks cleared! (3 task(s) removed)
```

### Finding Tasks

#### Searching by Keyword

Find tasks that contain a specific keyword in their description.

**Format:** `find <keyword>`

**Example:** `find book`

**Expected output:**
```
here are the matching tasks in your list:
1. [✅] read book
```

### Viewing Schedule

#### Viewing Tasks on a Specific Date

View all tasks (deadlines and events) scheduled for a particular date.

**Format:** `schedule DD/MM/YYYY`

**Example:** `schedule 22/02/2022`

**Expected output:**
```
schedule for Feb 22 2022:
1. [ ] submit report (by: Feb 22 2022, 12:00 PM)
2. [ ] meeting (from: Feb 22 2022, 12:00 PM to: Feb 22 2022, 2:00 PM)
```

If no tasks are scheduled for that date:
```
schedule for Feb 22 2022:
  (no tasks on this date)
```

### Getting Help

#### Viewing Available Commands

Display a list of all available commands.

**Format:** `help`

**Expected output:**
```
here are the commands available:
todo <description> - add a todo task

deadline <description> /by DD/MM/YYYY [HHmm] - add a deadline task (time is optional)

event <description> /from <date> /to <date> - add an event task

mark <index> - mark a task as done

unmark <index> - mark a task as not done

delete <index> - delete a task

find <keyword> - find tasks by keyword

schedule <date> - view tasks on a specific date

clear - remove all tasks

help - display this list of commands

bye - terminate Ziq
```

### Exiting the Application

#### Saying Goodbye

Close the application.

**Format:** `bye`

**Expected output:**
```
Bye. Hope to see you again!
```

## Command Summary

| Command | Format | Description |
|---------|--------|-------------|
| `todo` | `todo <description>` | Add a todo task |
| `deadline` | `deadline <description> /by DD/MM/YYYY [HHmm]` | Add a deadline task |
| `event` | `event <description> /from d/M/yyyy HHmm /to d/M/yyyy HHmm` | Add an event task |
| `list` | `list` | List all tasks |
| `mark` | `mark <index>` | Mark a task as done |
| `unmark` | `unmark <index>` | Mark a task as not done |
| `delete` | `delete <index>` | Delete a task |
| `find` | `find <keyword>` | Find tasks by keyword |
| `schedule` | `schedule DD/MM/YYYY` | View tasks on a date |
| `clear` | `clear` | Remove all tasks |
| `help` | `help` | Show help message |
| `bye` | `bye` | Exit the application |

## Notes

- Task indices start from 1 (not 0)
- Dates for deadlines use `DD/MM/YYYY` format (e.g., `22/02/2022`)
- Dates for events use `d/M/yyyy` format (e.g., `2/22/2022`)
- Time format is `HHmm` (24-hour format, e.g., `1200` for 12:00 PM)
- If no time is specified for a deadline, it defaults to midnight (00:00)
- Tasks are automatically saved to `data/ziq.txt`
- Duplicate tasks (same description and details) are not allowed
