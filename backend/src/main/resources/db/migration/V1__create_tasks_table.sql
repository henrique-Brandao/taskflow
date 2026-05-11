CREATE TABLE Tasks (
                       id BIGSERIAL PRIMARY KEY,
                       title VARCHAR(120) NOT NULL,
                       description TEXT,
                       completed BOOLEAN NOT NULL DEFAULT FALSE,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);