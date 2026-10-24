-- I love working with Sqlite.. So, I Choose this for this assessment. 

CREATE TABLE leads(
	ID INTEGER PRIMARY KEY AUTOINCREMENT,
  	Name VARCHAR NOT NULL,
  	Email VARCHAR NOT NULL UNIQUE,
  	Score INTEGER CHECK(Score >= 0 and Score <= 100)
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