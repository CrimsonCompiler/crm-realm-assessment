import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class LeadModel {
    private String name;
    private String email;
    private int score;

    public LeadModel(String name, String email, int score) {
        this.name = name;
        this.email = email;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getScore() {
        return score;
    }
}

public class LeadTracker {

    private static final int MIN_SCORE = 0;
    private static final int MAX_SCORE = 100;

    private List<LeadModel> leads = new ArrayList<>();

    public String calculatePriority(int score) {
        if (score >= 80 && score <= 100) {
            return "HOT";
        } else if (score >= 50 && score <= 79) {
            return "WARM";
        } else {
            return "COLD";
        }
    }

    /**
     * Validates an email address. Requires a single "@" separating a non-empty
     * local part from a non-empty domain that contains a "." and has no
     * whitespace or leading/trailing dots around the domain labels.
     */
    private static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        if (email.contains(" ")) {
            return false;
        }

        int at = email.indexOf('@');
        if (at != email.lastIndexOf('@') || at <= 0 || at == email.length() - 1) {
            return false;
        }

        String local = email.substring(0, at);
        String domain = email.substring(at + 1);

        if (local.startsWith(".") || local.endsWith(".") || local.contains("..")) {
            return false;
        }
        if (domain.startsWith(".") || domain.startsWith("-") || domain.endsWith(".")
                || domain.endsWith("-") || domain.contains("..")) {
            return false;
        }

        int dot = domain.indexOf('.');
        if (dot <= 0 || dot == domain.length() - 1) {
            return false;
        }

        // TLD must be alphabetic and at least two characters.
        String tld = domain.substring(domain.lastIndexOf('.') + 1);
        if (tld.length() < 2) {
            return false;
        }
        for (int i = 0; i < tld.length(); i++) {
            if (!Character.isLetter(tld.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Emails are unique per lead (mirrors the UNIQUE constraint in the SQL
     * schema). Comparison is case-insensitive because email local parts are
     * case-insensitive in practice.
     */
    private boolean emailExists(String email) {
        for (LeadModel lead : leads) {
            if (lead.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    public boolean addLead(String name, String email, int score) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Error: Name cannot be empty.");
            return false;
        } else if (email == null || email.trim().isEmpty()) {
            System.out.println("Error: Email cannot be empty.");
            return false;
        } else if (!isValidEmail(email.trim())) {
            System.out.println("Error: '" + email.trim() + "' is not a valid email address.");
            return false;
        } else if (emailExists(email.trim())) {
            System.out.println("Error: A lead with email '" + email.trim() + "' already exists.");
            return false;
        } else if (score < MIN_SCORE || score > MAX_SCORE) {
            System.out.println("Error: Score must be between " + MIN_SCORE + " and " + MAX_SCORE + ".");
            return false;
        }

        LeadModel lead = new LeadModel(name.trim(), email.trim(), score);
        leads.add(lead);
        System.out.println("Success: Lead added successfully");

        return true;
    }

    public void viewLeads() {
        if (leads.isEmpty()) {
            System.out.println("No leads available.");
            return;
        }

        System.out.println("\n--- Leads List ---");
        for (int i = 0; i < leads.size(); i++) {
            LeadModel lead = leads.get(i);
            String priority = calculatePriority(lead.getScore());
            System.out.println((i + 1) + ". Name: " + lead.getName() +
                    " | Email: " + lead.getEmail() +
                    " | Score: " + lead.getScore() +
                    " | Priority: " + priority);
        }
        System.out.println("-----------------\n");
    }

    public static void main(String[] args) {
        LeadTracker tracker = new LeadTracker();
        Scanner scanner = new Scanner(System.in);
        int choice = 0;

        System.out.println("Welcome to Lead Tracker!");

        while (choice != 3) {
            System.out.println("\nMenu:");
            System.out.println("1. Add Lead");
            System.out.println("2. View Leads");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter lead name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter lead email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter lead score (0-100): ");

                    if (scanner.hasNextInt()) {
                        int score = scanner.nextInt();
                        scanner.nextLine();
                        tracker.addLead(name, email, score);
                    } else {
                        System.out.println("Error: Score must be an integer.");
                        scanner.nextLine();
                    }
                    break;
                case 2:
                    tracker.viewLeads();
                    break;
                case 3:
                    System.out.println("Exiting Lead Tracker. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }
}
