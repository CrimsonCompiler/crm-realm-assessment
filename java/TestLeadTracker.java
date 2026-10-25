import java.lang.reflect.Method;

public class TestLeadTracker {
    static int pass = 0, fail = 0;

    static boolean add(LeadTracker t, String n, String e, int s) {
        // addLead prints; capture by temporarily redirecting stdout is messy, so just call it.
        try {
            Method m = LeadTracker.class.getDeclaredMethod("addLead", String.class, String.class, int.class);
            m.setAccessible(true);
            return (boolean) m.invoke(t, n, e, s);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    static void check(String label, boolean actual, boolean expected) {
        if (actual == expected) {
            pass++;
            System.out.println("PASS  " + label);
        } else {
            fail++;
            System.out.println("FAIL  " + label + " (expected=" + expected + " actual=" + actual + ")");
        }
    }

    public static void main(String[] args) {
        System.out.println("--- valid emails accepted ---");
        String[] valid = {
            "rahim@example.com", "a.b@sub.domain.co", "user+tag@example.org",
            "x@y.io", "name@my-domain.com", "UPPER@Example.COM"
        };
        for (String e : valid) {
            LeadTracker t = new LeadTracker();
            check("accept '" + e + "'", add(t, "N", e, 50), true);
        }

        System.out.println("\n--- invalid emails rejected ---");
        String[] invalid = {
            "not-an-email", "plainaddress", "@example.com", "user@", "user@@example.com",
            "user@example", "user@.com", "user@example.", "user name@example.com",
            "user@exam ple.com", "user@example..com", ".user@example.com",
            "user.@example.com", "user@-example.com", "user@example.c", "user@example.1a"
        };
        for (String e : invalid) {
            LeadTracker t = new LeadTracker();
            check("reject '" + e + "'", add(t, "N", e, 50), false);
        }

        System.out.println("\n--- other validation ---");
        LeadTracker t = new LeadTracker();
        check("reject null name", add(t, null, "a@b.com", 50), false);
        check("reject empty name", add(t, "   ", "a@b.com", 50), false);
        check("reject null email", add(t, "N", null, 50), false);
        check("reject empty email", add(t, "N", "  ", 50), false);
        check("reject score -1", add(t, "N", "a@b.com", -1), false);
        check("reject score 101", add(t, "N", "a@b.com", 101), false);
        check("accept score 0", add(t, "N", "a@b.com", 0), true);
        check("accept score 100", add(t, "N", "a2@b.com", 100), true);

        System.out.println("\n--- duplicate email ---");
        LeadTracker d = new LeadTracker();
        check("first add ok", add(d, "A", "dup@example.com", 50), true);
        check("duplicate rejected", add(d, "B", "dup@example.com", 50), false);
        check("duplicate rejected (case-insensitive)", add(d, "B", "DUP@Example.com", 50), false);
        check("different email accepted", add(d, "C", "other@example.com", 50), true);

        System.out.println("\n--- priority calculation ---");
        LeadTracker p = new LeadTracker();
        check("score 80 -> HOT", p.calculatePriority(80).equals("HOT"), true);
        check("score 100 -> HOT", p.calculatePriority(100).equals("HOT"), true);
        check("score 79 -> WARM", p.calculatePriority(79).equals("WARM"), true);
        check("score 50 -> WARM", p.calculatePriority(50).equals("WARM"), true);
        check("score 49 -> COLD", p.calculatePriority(49).equals("COLD"), true);
        check("score 0 -> COLD", p.calculatePriority(0).equals("COLD"), true);

        System.out.println("\n=== PASS: " + pass + "  FAIL: " + fail + " ===");
        if (fail > 0) System.exit(1);
    }
}
