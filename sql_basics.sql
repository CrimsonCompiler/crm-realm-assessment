-- I love working with Sqlite.. So, I Choose this for this assessment. 

CREATE TABLE leads(
	ID INTEGER PRIMARY KEY AUTOINCREMENT,
  	Name VARCHAR NOT NULL,
  	Email VARCHAR NOT NULL UNIQUE,
  	Score INTEGER CHECK(Score >= 0 and Score <= 100)
);