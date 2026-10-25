public class TestLeadTracker {

    private static int passed = 0;
    private static int failed = 0;

    private static void check(String name, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("PASS  " + name);
        } else {
            failed++;
            System.out.println("FAIL  " + name);
        }
    }

    public static void main(String[] args) {
        LeadTracker tracker = new LeadTracker();

        // 1. Priority is calculated from the score.
        check("priority 85 is HOT",
                tracker.calculatePriority(85).equals("HOT")
                        && tracker.calculatePriority(60).equals("WARM")
                        && tracker.calculatePriority(20).equals("COLD"));

        // 2. A valid lead is accepted.
        check("valid lead is added", tracker.addLead("Rahim Ali", "rahim@example.com", 90));

        // 3. Empty name and email are rejected.
        check("empty name and email are rejected",
                !tracker.addLead("  ", "a@example.com", 50)
                        && !tracker.addLead("Bob", "   ", 50));

        // 4. Scores outside 0-100 are rejected.
        check("out of range score is rejected",
                !tracker.addLead("Bob", "b@example.com", -1)
                        && !tracker.addLead("Bob", "b@example.com", 101));

        System.out.println("\n=== PASS: " + passed + "  FAIL: " + failed + " ===");
        if (failed > 0) {
            System.exit(1);
        }
    }
}
