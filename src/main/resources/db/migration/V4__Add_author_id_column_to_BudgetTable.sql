ALTER TABLE budget
ADD COLUMN author_id INT REFERENCES author(id);