/**
 * Represents a task that must be completed by a specified time.
 */
public class Deadline extends Task {
    private final String by;

    /**
     * Creates an incomplete deadline task.
     *
     * @param description the task description
     * @param by the deadline, kept as text
     */
    public Deadline(String description, String by) {
        super(TaskType.DEADLINE, description);
        this.by = by;
    }

    public String getBy() {
        return by;
    }

    /**
     * Formats this deadline with its type, completion status, and due time.
     *
     * @return the formatted deadline
     */
    @Override
    public String toString() {
        return super.toString() + " (by: " + by + ")";
    }
}
