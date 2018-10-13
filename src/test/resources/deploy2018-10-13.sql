CREATE TABLE award_type (
  name VARCHAR(15) PRIMARY KEY
);

INSERT INTO award_type (name) VALUES ('RANICEK');

CREATE TABLE message_award (
  id         UUID PRIMARY KEY,
  award_type VARCHAR(15) REFERENCES award_type (name) NOT NULL,
  message_id UUID REFERENCES message (id)             NOT NULL,
  user_name  VARCHAR(255)                             NOT NULL,
  created_on TIMESTAMP                                NOT NULL
);