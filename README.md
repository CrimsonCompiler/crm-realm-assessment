# CRM Realm — Junior Software Engineer Intern Assessment

Lead tracker implemented in three parts, kept in one repository:

| Directory | Contents |
|---|---|
| `java/` | `LeadTracker.java` — console application, and `TestLeadTracker.java` |
| `javascript/` | `solution.js` — the three JavaScript problems, and `test_solution.js` |
| `sql/` | `leads_table.sql` — SQLite schema, views and sample data |

## License

[MIT](LICENSE) © 2026 Tousif Tasrik

---

## 1. How to run

### Java application

Requires JDK 11 or newer (developed on JDK 21).

```bash
# Compile
javac -d build java/LeadTracker.java

# Run the interactive console app
java -cp build LeadTracker
```

The app presents a menu — `1. Add Lead`, `2. View Leads`, `3. Exit` — and reads
from standard input, so it can be driven either interactively or piped:

```bash
printf '1\nAlice\nalice@example.com\n85\n2\n3\n' | java -cp build LeadTracker
```

### Java tests

```bash
javac -d build java/LeadTracker.java java/TestLeadTracker.java
java -cp build TestLeadTracker
```

Expected output:

```
PASS  priority 85 is HOT
PASS  valid lead is added
PASS  empty name and email are rejected
PASS  out of range score is rejected

=== PASS: 4  FAIL: 0 ===
```

### JavaScript functions and tests

Requires Node.js (developed on Node 24). No dependencies, so there is no
`npm install` step.

```bash
# Run the tests
node javascript/test_solution.js
```

Expected output:

```
PASS  firstNonRepeatingChar finds the unique character
PASS  findTwoSum returns the matching indices
PASS  findTwoSum returns null when there is no pair
PASS  countLeadsByStatus counts each status
PASS  countLeadsByStatus handles a "toString" status

=== PASS: 5  FAIL: 0 ===
```

The functions are exported from `javascript/solution.js` and can be used
directly:

```js
const { firstNonRepeatingChar, findTwoSum, countLeadsByStatus } = require('./javascript/solution.js');
```

### SQL

The script targets SQLite and is self-contained — it creates the table, both
views, and inserts sample data.

```bash
sqlite3 leads.db < sql/leads_table.sql
sqlite3 leads.db "SELECT * FROM lead_priority_counts;"
```

---

## 2. Approach to each JavaScript problem

### Problem 1 — First non-repeating character

Two passes over the string. The first pass counts occurrences of each
character in a `Map`, keyed by the lowercase form of the character. The
second pass walks the string in its original order and returns the first
character whose count is exactly one.

Using a `Map` rather than a plain object keeps the counting correct for
status-like keys such as `__proto__`. The string is split with
`Array.from()` rather than `split('')` so that characters outside the Basic
Multilingual Plane (emoji, for example) are handled as one unit instead of
two broken surrogate halves.

**Time O(n), space O(k)** where k is the number of distinct characters.

### Problem 2 — Two sum

Single pass with a `Map` that stores `value -> index` for values already
seen. For each number the complement (`target - num`) is computed; if the
complement is already in the map, the two indices are returned immediately.
Otherwise the current number is recorded.

Recording only the *first* index for each value (`if (!seen.has(num))`) means
the function returns the pair with the widest gap between indices, which is
the usual expected result for this problem.

**Time O(n), space O(n).**

### Problem 3 — Count leads by status

A `Map` accumulates a count per status, then the map is copied into the
returned object. The input array is never modified.

A status such as `toString` or `valueOf` collides with the inherited
`Object.prototype` methods. Writing such a count as a normal property would
replace the method with a number, and the returned object would then throw
`TypeError: Cannot convert object to primitive value` whenever it was coerced
to a string. Those counts are therefore not written directly onto the result;
instead the full set of counts is always available on `result.counts` under
their exact names. Normal statuses are still exposed directly on the result.

---

## 3. Test cases

### Java — `java/TestLeadTracker.java`

| # | Case | Input | Expected |
|---|---|---|---|
| 1 | Priority calculation | scores 85, 60, 20 | `HOT`, `WARM`, `COLD` |
| 2 | Valid lead is added | `Rahim Ali`, `rahim@example.com`, 90 | returns `true` |
| 3 | Empty name and email rejected | `"  "`, `"   "` | returns `false` |
| 4 | Score out of range rejected | `-1`, `101` | returns `false` |

### JavaScript — `javascript/test_solution.js`

| # | Case | Input | Expected |
|---|---|---|---|
| 1 | First non-repeating character | `"swiss"` / `"aAbBcC"` | `"w"` / `null` |
| 2 | Two sum finds the pair | `[2,7,11,15]` target 9 / `[3,2,4]` target 6 | `[0,1]` / `[1,2]` |
| 3 | Two sum with no pair | `[1,2]` target 100 | `null` |
| 4 | Count leads by status | `NEW, NEW, OLD` | `{ NEW: 2, OLD: 1 }` |
| 5 | Status colliding with a prototype method | `toString` | `counts.toString === 1`, result still coercible to a string |

### Boundary values and invalid input

These were verified during development and are the cases that drove the
implementation decisions:

- **Boundary scores** — `0` and `100` are accepted; `-1` and `101` are
  rejected. `80` is `HOT` and `79` is `WARM`, so the priority bands are
  inclusive at the top and exclusive at the bottom.
- **Empty and whitespace-only name/email** — rejected. A name of `"  "` is
  trimmed and found to be empty.
- **Non-integer score** — the console app checks `scanner.hasNextInt()`
  before parsing, so entering `abc` prints an error instead of throwing.
- **Non-string input to `firstNonRepeatingChar`** — returns `null` rather
  than throwing.
- **Empty string** — returns `null` (no character occurs exactly once).
- **A lead with no status** — counted under an explicit `UNKNOWN` key rather
  than the literal string `"undefined"`.
- **A status named `toString` or `valueOf`** — the count is stored under
  `counts.toString` and the result can still be coerced to a string. This was
  a real defect found by testing.
- **Duplicate email** — the SQL schema enforces `UNIQUE` on `Email`, so a
  duplicate insert is rejected by the database. The Java app keeps its leads
  in memory and does not currently enforce this itself; see section 5.
- **`NULL` score in SQL** — SQLite evaluates a `CHECK` constraint to `NULL`
  for a `NULL` value and treats it as satisfied, so `Score` is declared
  `NOT NULL` to stop a `NULL` bypassing the 0–100 range check and being
  silently counted as `COLD`.

---

## 4. One decision or assumption

**Spaces and punctuation are treated as ordinary characters in Problem 1.**

The brief did not specify how to handle them, so `firstNonRepeatingChar`
counts them exactly like letters: in `"Ab a"` the space occurs once and is
therefore a valid answer, and in `"swiss"` the space is skipped because it
occurs twice.

I chose this because the task is about *finding the first character that
occurs exactly once*, and silently dropping spaces would make the function
answer a different question than the one asked. It also keeps the function's
contract easy to state and to test.

A closely related assumption: **counting is case-insensitive** (`"aAbBcC"`
returns `null`), while the character returned preserves its original case.
This treats `"a"` and `"A"` as the same character, which matches how the
problem is usually framed.

---

## 5. One thing I would improve with more time

**Replace the hand-written validation with a proper persistence layer and a
real test framework.**

The two deliverables currently validate in slightly different ways. The Java
app checks that a name and email are non-empty, but it does not check that
the email is well-formed, so `"not-an-email"` is accepted — even though the
SQL schema enforces a unique, non-null email. The two layers agree on the
score range but not on email validity.

With more time I would:

1. **Introduce a small data-access layer** so the Java app talks to the
   SQLite database directly instead of only validating in memory. That
   removes the duplication between the two implementations of "what a valid
   lead is" and makes the `UNIQUE` constraint on `Email` authoritative rather
   than something the Java code has to remember to mirror.
2. **Move to JUnit 5** for the Java tests. The current `TestLeadTracker`
   uses a hand-rolled `check()` helper so that it runs with plain `javac`
   and no dependencies, which keeps the submission easy to run but is less
   standard than a reviewer would expect from a Java project. JUnit would
   also give per-test isolation and better failure reporting.
3. **Add a `package.json` test script** so `npm test` works, and widen the
   JavaScript suite back out to cover the boundary and invalid-input cases
   listed in section 3 individually.
