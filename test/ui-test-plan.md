# UI test plan

## Test: Add a to-do and list it

Aim: Confirm that a to-do is stored and displayed using the to-do type marker.

### Commands

```text
todo borrow book
list
```

### Expected outputs

```text
     Got it. I've added this task:
       [T][ ] borrow book
     Now you have 1 tasks in the list.
```

```text
     Here are the tasks in your list:
     1.[T][ ] borrow book
```

## Test: Add a deadline

Aim: Confirm that deadline dates are stored as text and displayed with the deadline marker.

### Commands

```text
deadline return book /by Sunday
```

### Expected outputs

```text
     Got it. I've added this task:
       [D][ ] return book (by: Sunday)
     Now you have 1 tasks in the list.
```

## Test: Add an event

Aim: Confirm that event start and end times are displayed with the event marker.

### Commands

```text
event project meeting /from Mon 2pm /to 4pm
```

### Expected outputs

```text
     Got it. I've added this task:
       [E][ ] project meeting (from: Mon 2pm to: 4pm)
     Now you have 1 tasks in the list.
```

## Test: Reject incorrect commands and recover

Aim: Confirm that invalid input produces a helpful error and the chatbot continues accepting commands.

### Commands

```text
todo
blah
deadline submit report
event project meeting /from Mon 2pm
mark one
unmark 1
```

### Expected outputs

```text
     Quack? Give me a todo description!
```

```text
     Quack? I don't know what that means :-(
```

```text
     Quack? Use /by to give the deadline.
```

```text
     Quack? Use /from START /to END for an event.
```

```text
     Quack? Please enter a valid task number.
```

```text
     Quack? Please enter a task number from the list.
```
