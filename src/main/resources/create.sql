CREATE TABLE interns (
    id BIGSERIAL PRIMARY KEY,
    firstname VARCHAR(40) NOT NULL,
    lastname VARCHAR(40) NOT NULL
);

CREATE TABLE marks (
    id BIGSERIAL PRIMARY KEY,
    subject VARCHAR(40) NOT NULL,
    mark INT NOT NULL CHECK (mark >= 1 AND mark <= 5),
    intern_id BIGINT NOT NULL,
    FOREIGN KEY (intern_id) REFERENCES interns(id) ON DELETE CASCADE
);