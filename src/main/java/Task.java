/**
 * Represents one task and whether the user has completed it.
 */
public class Task {
    private final String description;
    private boolean isDone;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description the work the user wants to remember
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the symbol used to display this task's completion status.
     *
     * @return {@code X} when the task is done, otherwise a blank space
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Marks this task as completed.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks this task as not yet completed.
     */
    public void markAsUndone() {
        isDone = false;
    }

    /**
     * Formats this task for display in command responses and task lists.
     *
     * @return the status icon followed by the task description
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
