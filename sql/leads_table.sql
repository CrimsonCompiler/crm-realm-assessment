-- I love working with Sqlite.. So, I Choose this for this assessment.

CREATE TABLE leads(
	ID INTEGER PRIMARY KEY AUTOINCREMENT,
  	Name VARCHAR NOT NULL,
  	Email VARCHAR NOT NULL UNIQUE,
  	-- Score is explicitly NOT NULL: in SQLite a CHECK constraint evaluates
  	-- to NULL for a NULL value and therefore passes, so a nullable Score
  	-- would bypass the 0-100 range check and be counted as COLD.
  	Score INTEGER NOT NULL CHECK(Score >= 0 and Score <= 100)
);

-- Write a query to retrieve all leads whose priority is HOT. A HOT lead has a score of 80 or higher.
CREATE VIEW hot_leads AS
SELECT *, 'HOT' as priority
FROM leads
WHERE Score >= 80;

-- Sample Data 1
INSERT INTO leads (Name, Email, Score)
VALUES ('Rahim Ali', 'rahim@example.com', 85);

-- Sample Data 2
INSERT INTO leads (Name, Email, Score)
VALUES ('Sumaiya Khan', 'sumaiya@example.com', 45);

-- Sample Data 3
INSERT INTO leads (Name, Email, Score)
VALUES ('Tanvir Ahmed', 'tanvir@example.com', 95);

-- Retrieving from the table
SELECT * FROM hot_leads;


-- Write a query or a suitable SQL expression that reports the number of leads in each priority group: HOT, WARM, and COLD.

CREATE VIEW lead_priority_counts AS
SELECT
    CASE
        WHEN Score >= 80 AND Score <= 100 THEN 'HOT'
        WHEN Score >= 50 AND Score <= 79 THEN 'WARM'
        ELSE 'COLD'
    END AS priority_group,
    COUNT(*) AS lead_count
FROM leads
GROUP BY priority_group
ORDER BY lead_count DESC;

SELECT * FROM lead_priority_counts;

-- Example of a query  to test

INSERT INTO leads (Name, Email, Score)
VALUES ('Ayesha Rahman', 'ayesha12@gmail.com', 70);

SELECT * FROM lead_priority_counts;
