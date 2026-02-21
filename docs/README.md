# ziq user guide

<p align="center">
  <img src="Ui.png" width="400" alt="Product screenshot" />
</p>

ziq is a user-friendly task management chatbot that helps you keep track of your todos, deadlines, and events. 

> <span style="color:#0d6efd">**Commands**</span> · <span style="color:#b45309">**Parameters**</span>

## Quick Start

1. Launch the application
2. Type a command in the input field
3. Press Enter or click Send
4. ziq will update you on the system status

## Features

### Adding Tasks

| <span style="color:#0d6efd">**todo**</span> — Add a todo task |
|:--|
| Add a simple todo task without any date or time. |
| **Format:** <code><span style="color:#0d6efd">todo</span> <span style="color:#b45309">&lt;description&gt;</span></code> |
| **Example:** <code><span style="color:#0d6efd">todo</span> <span style="color:#b45309">read book</span></code> |

| <span style="color:#0d6efd">**deadline**</span> — Add a deadline task |
|:--|
| Add a deadline task with a due date. Time is optional - if not specified, it defaults to midnight (00:00). |
| **Format:** <code><span style="color:#0d6efd">deadline</span> <span style="color:#b45309">&lt;description&gt;</span> /by <span style="color:#b45309">DDMMYYYY</span> [<span style="color:#b45309">HHmm</span>]</code> |
| **Examples:** <code><span style="color:#0d6efd">deadline</span> <span style="color:#b45309">submit report</span> /by <span style="color:#b45309">22022022 1200</span></code> · <code><span style="color:#0d6efd">deadline</span> <span style="color:#b45309">finish assignment</span> /by <span style="color:#b45309">15032022</span></code> |

| <span style="color:#0d6efd">**event**</span> — Add an event task |
|:--|
| Add an event task with a start and end time. |
| **Format:** <code><span style="color:#0d6efd">event</span> <span style="color:#b45309">&lt;description&gt;</span> /from <span style="color:#b45309">DDMMYYYY HHmm</span> /to <span style="color:#b45309">DDMMYYYY HHmm</span></code> |
| **Example:** <code><span style="color:#0d6efd">event</span> <span style="color:#b45309">meeting</span> /from <span style="color:#b45309">22022022 1200</span> /to <span style="color:#b45309">22022022 1400</span></code> |

### Viewing Tasks

| <span style="color:#0d6efd">**list**</span> — List all tasks |
|:--|
| View all tasks in your list. |
| **Format:** <code><span style="color:#0d6efd">list</span></code> |

### Managing Tasks

| <span style="color:#0d6efd">**mark**</span> — Mark a task as done |
|:--|
| Mark a task as completed. |
| **Format:** <code><span style="color:#0d6efd">mark</span> <span style="color:#b45309">&lt;index&gt;</span></code> |
| **Example:** <code><span style="color:#0d6efd">mark</span> <span style="color:#b45309">1</span></code> |

| <span style="color:#0d6efd">**unmark**</span> — Mark a task as not done |
|:--|
| Mark a completed task as not done. |
| **Format:** <code><span style="color:#0d6efd">unmark</span> <span style="color:#b45309">&lt;index&gt;</span></code> |
| **Example:** <code><span style="color:#0d6efd">unmark</span> <span style="color:#b45309">1</span></code> |

| <span style="color:#0d6efd">**delete**</span> — Remove a task |
|:--|
| Remove a task from your list. |
| **Format:** <code><span style="color:#0d6efd">delete</span> <span style="color:#b45309">&lt;index&gt;</span></code> |
| **Example:** <code><span style="color:#0d6efd">delete</span> <span style="color:#b45309">2</span></code> |

| <span style="color:#0d6efd">**clear**</span> — Remove all tasks |
|:--|
| Remove all tasks from your list at once. |
| **Format:** <code><span style="color:#0d6efd">clear</span></code> |

### Finding Tasks

| <span style="color:#0d6efd">**find**</span> — Search tasks by keyword |
|:--|
| Find tasks that contain a specific keyword in their description. |
| **Format:** <code><span style="color:#0d6efd">find</span> <span style="color:#b45309">&lt;keyword&gt;</span></code> |
| **Example:** <code><span style="color:#0d6efd">find</span> <span style="color:#b45309">book</span></code> |

### Tagging Tasks

| <span style="color:#0d6efd">**tag**</span> — Add a tag to a task |
|:--|
| Add a tag to any task (todo, deadline, or event) for better organization. |
| **Format:** <code><span style="color:#0d6efd">tag</span> <span style="color:#b45309">&lt;index&gt;</span> <span style="color:#b45309">&lt;tag&gt;</span></code> |
| **Example:** <code><span style="color:#0d6efd">tag</span> <span style="color:#b45309">1 work</span></code> |

### Organizing Tasks

| <span style="color:#0d6efd">**organise tag**</span> — Group tasks by tag |
|:--|
| Group and display tasks organized by their tags. Tasks with the same tag are grouped together, and untagged tasks are shown separately. |
| **Format:** <code><span style="color:#0d6efd">organise tag</span></code> |
| **Example:** <code><span style="color:#0d6efd">organise tag</span></code> |

| <span style="color:#0d6efd">**organise deadline**</span> — Order tasks by deadline |
|:--|
| Display tasks organized by deadline, with deadlines sorted by due date, followed by events sorted by start date, and finally todos. |
| **Format:** <code><span style="color:#0d6efd">organise deadline</span></code> |
| **Example:** <code><span style="color:#0d6efd">organise deadline</span></code> |

### Viewing Schedule

| <span style="color:#0d6efd">**schedule**</span> — View tasks on a date |
|:--|
| View all tasks (deadlines and events) scheduled for a particular date. |
| **Format:** <code><span style="color:#0d6efd">schedule</span> <span style="color:#b45309">DDMMYYYY</span></code> (or <code><span style="color:#b45309">DD</span></code> for day only, <code><span style="color:#b45309">DDMM</span></code> for day/month only) |
| **Example:** <code><span style="color:#0d6efd">schedule</span> <span style="color:#b45309">22022022</span></code> or <code><span style="color:#0d6efd">schedule</span> <span style="color:#b45309">22</span></code> or <code><span style="color:#0d6efd">schedule</span> <span style="color:#b45309">2202</span></code> |

### Getting Help

| <span style="color:#0d6efd">**help**</span> — Show available commands |
|:--|
| Display a list of all available commands. |
| **Format:** <code><span style="color:#0d6efd">help</span></code> |

### Exiting the Application

| <span style="color:#0d6efd">**bye**</span> — Exit the application |
|:--|
| Close the application. |
| **Format:** <code><span style="color:#0d6efd">bye</span></code> |

## Command Summary

| <span style="color:#0d6efd">Command</span> | Format | Description |
|---------|--------|-------------|
| <span style="color:#0d6efd">todo</span> | <code><span style="color:#0d6efd">todo</span> <span style="color:#b45309">&lt;description&gt;</span></code> | Add a todo task |
| <span style="color:#0d6efd">deadline</span> | <code><span style="color:#0d6efd">deadline</span> <span style="color:#b45309">&lt;description&gt;</span> /by <span style="color:#b45309">DDMMYYYY</span> [<span style="color:#b45309">HHmm</span>]</code> | Add a deadline task |
| <span style="color:#0d6efd">event</span> | <code><span style="color:#0d6efd">event</span> <span style="color:#b45309">&lt;description&gt;</span> /from <span style="color:#b45309">DDMMYYYY HHmm</span> /to <span style="color:#b45309">DDMMYYYY HHmm</span></code> | Add an event task |
| <span style="color:#0d6efd">list</span> | <code><span style="color:#0d6efd">list</span></code> | List all tasks |
| <span style="color:#0d6efd">mark</span> | <code><span style="color:#0d6efd">mark</span> <span style="color:#b45309">&lt;index&gt;</span></code> | Mark a task as done |
| <span style="color:#0d6efd">unmark</span> | <code><span style="color:#0d6efd">unmark</span> <span style="color:#b45309">&lt;index&gt;</span></code> | Mark a task as not done |
| <span style="color:#0d6efd">delete</span> | <code><span style="color:#0d6efd">delete</span> <span style="color:#b45309">&lt;index&gt;</span></code> | Delete a task |
| <span style="color:#0d6efd">find</span> | <code><span style="color:#0d6efd">find</span> <span style="color:#b45309">&lt;keyword&gt;</span></code> | Find tasks by keyword |
| <span style="color:#0d6efd">schedule</span> | <code><span style="color:#0d6efd">schedule</span> <span style="color:#b45309">DDMMYYYY</span></code> (or <span style="color:#b45309">DD</span>, <span style="color:#b45309">DDMM</span>) | View tasks on a date |
| <span style="color:#0d6efd">tag</span> | <code><span style="color:#0d6efd">tag</span> <span style="color:#b45309">&lt;index&gt;</span> <span style="color:#b45309">&lt;tag&gt;</span></code> | Add a tag to a task |
| <span style="color:#0d6efd">organise</span> | <code><span style="color:#0d6efd">organise tag</span></code> or <code><span style="color:#0d6efd">organise deadline</span></code> | Organize tasks by tag or deadline |
| <span style="color:#0d6efd">clear</span> | <code><span style="color:#0d6efd">clear</span></code> | Remove all tasks |
| <span style="color:#0d6efd">help</span> | <code><span style="color:#0d6efd">help</span></code> | Show help message |
| <span style="color:#0d6efd">bye</span> | <code><span style="color:#0d6efd">bye</span></code> | Exit the application |

## Notes

- Task indices start from 1 (not 0)
- Dates use <code><span style="color:#b45309">DDMMYYYY</span></code> format with no slashes (e.g., <code><span style="color:#b45309">22022022</span></code> for February 22, 2022)
- For the <code><span style="color:#0d6efd">schedule</span></code> command, you can use partial dates:
  - <code><span style="color:#b45309">DD</span></code> - uses current month and year (e.g., <code><span style="color:#b45309">22</span></code> for the 22nd of the current month)
  - <code><span style="color:#b45309">DDMM</span></code> - uses current year (e.g., <code><span style="color:#b45309">2202</span></code> for February 22 of the current year)
  - <code><span style="color:#b45309">DDMMYYYY</span></code> - full date (e.g., <code><span style="color:#b45309">22022022</span></code>)
- Time format is <code><span style="color:#b45309">HHmm</span></code> (24-hour format, e.g., <code><span style="color:#b45309">1200</span></code> for 12:00 PM)
- If no time is specified for a <code><span style="color:#0d6efd">deadline</span></code>, only the date is displayed 
- Tasks are automatically saved to <code><span style="color:#b45309">data/ziq.txt</span></code>
- Duplicate tasks (same description and details) are not allowed
- Tags are color-coded for easy identification
- Command keywords in your input are color-coded: <code><span style="color:#0d6efd">todo</span></code>=green, <code><span style="color:#0d6efd">deadline</span></code>=red, <code><span style="color:#0d6efd">event</span></code>=yellow, <code><span style="color:#0d6efd">tag</span></code>=purple, <code><span style="color:#0d6efd">organise</span></code>=orange
