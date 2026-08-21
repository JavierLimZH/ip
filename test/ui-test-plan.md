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
