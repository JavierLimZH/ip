/**
 * Minimal entry point for the Quackers chatbot project.
 */
public class Quackers {
    public static void main(String[] args) {
        String separator = "_".repeat(60);
        String banner = "+--------------------+\n"
                + "|      QUACKERS      |\n"
                + "+--------------------+";

        System.out.println(separator);
        System.out.println(banner);
        System.out.println("Hello! I'm Quackers.");
        System.out.println("What can I do for you? Quack!");
        System.out.println(separator);
        System.out.println("Bye. Hope to see you again soon! Quack!");
        System.out.println(separator);
    }
}
