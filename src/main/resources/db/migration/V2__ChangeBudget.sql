-- Create a temporary table to hold the updated values
CREATE TABLE temp_budget AS
SELECT
    id, year, month, amount,
    CASE
        WHEN type = 'Комиссия' THEN 'Расход'
        ELSE type
    END AS updated_type
FROM
    budget;

-- Drop the existing table
DROP TABLE budget;

-- Create a new table with the updated enum values
CREATE TABLE budget (
    id SERIAL PRIMARY KEY,
    year INT NOT NULL,
    month INT NOT NULL,
    amount INT NOT NULL,
    type VARCHAR(255) NOT NULL
);

-- Copy data from the temporary table to the new table
INSERT INTO budget (id, year, month, amount, type)
SELECT id, year, month, amount, updated_type FROM temp_budget;

-- Drop the temporary table
DROP TABLE temp_budget;
