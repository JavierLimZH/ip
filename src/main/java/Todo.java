/**
 * Represents a task with no associated date or time.
 */
public class Todo extends Task {

    /**
     * Creates an incomplete to-do task.
     *
     * @param description the task description
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Formats this to-do with its type and completion status.
     *
     * @return the formatted to-do
     */
    @Override
    public String toString() {
        return "[T][" + getStatusIcon() + "] " + getDescription();
    }
}
