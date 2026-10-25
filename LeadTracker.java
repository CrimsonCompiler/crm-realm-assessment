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

public class LeadTracker extends LeadModel {

    public LeadTracker(String name, String email, int score) {
        super(name, email, score);
    }

    private List<LeadModel> leads = new ArrayList<>();

    public String calculatePriority(int score) {
        if (score >= 80 && score <= 100) {
            return "HOT";
        } else if (score >= 50 && score < 79) {
            return "WARM";
        } else {
            return "COLD";
        }
    }

    public boolean addLead(String name, String email, int score) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Error: Name cannot be empty.");
            return false;
        } else if (email == null || email.trim().isEmpty()) {
            System.out.println("Error: Email cannot be empty.");
            return false;
        } else if (score < 0 || score > 100) {
            System.out.println("Error: Score must be between 0 and 100.");
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
        LeadTracker tracker = new LeadTracker("John Doe", "john.doe@example.com", 85);
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
