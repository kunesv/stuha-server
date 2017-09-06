CREATE TABLE users (
  id       UUID PRIMARY KEY,
  name     VARCHAR(255) UNIQUE NOT NULL,
  username VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE user_credentials (
  id       UUID PRIMARY KEY,
  username VARCHAR(255) REFERENCES users (username),
  password VARCHAR(255) NOT NULL
);

CREATE TABLE conversation (
  id    UUID PRIMARY KEY,
  title VARCHAR(255)
);

CREATE TABLE icon (
  id      UUID PRIMARY KEY,
  alt     VARCHAR(4),
  path    VARCHAR(8),
  user_id UUID REFERENCES users (id)
);

CREATE TABLE message (
  id              UUID PRIMARY KEY,
  conversation_id UUID REFERENCES conversation (id),
  created_on      TIMESTAMP    NOT NULL,
  formatted       TEXT,
  image_ids       TEXT,
  icon_path       VARCHAR(8),
  reply_to        UUID REFERENCES message (id),
  rough           TEXT,
  user_name       VARCHAR(255) NOT NULL
);
CREATE INDEX ON message (conversation_id, created_on);

CREATE TABLE file (
  id              UUID PRIMARY KEY,
  conversation_id UUID REFERENCES conversation (id) NULL,
  content_type    VARCHAR(255)                      NOT NULL,
  file            BYTEA                             NOT NULL
);

CREATE TABLE image (
  id         UUID PRIMARY KEY REFERENCES file (id),
  message_id UUID REFERENCES message (id) NULL,
  user_id    UUID REFERENCES users (id)   NULL,
  name       VARCHAR(1023)                NOT NULL
);

CREATE TABLE thumbnail (
  id              UUID PRIMARY KEY REFERENCES file (id),
  conversation_id UUID REFERENCES conversation (id) NULL,
  content_type    VARCHAR(255)                      NOT NULL,
  thumbnail       BYTEA                             NOT NULL
);

CREATE TABLE token (
  id              UUID PRIMARY KEY,
  auto_revalidate BOOLEAN   NOT NULL,
  created_on      TIMESTAMP NOT NULL,
  token           VARCHAR(255),
  user_id         UUID REFERENCES users (id)
);


CREATE TABLE user_conversation (
  id              UUID PRIMARY KEY,
  user_id         UUID REFERENCES users (id),
  conversation_id UUID REFERENCES conversation (id)
);

CREATE TABLE message_reply (
  id          UUID PRIMARY KEY,
  message_id  UUID REFERENCES message (id),
  reply_to_id UUID REFERENCES message (id),
  key         VARCHAR(255) NOT NULL
);

CREATE TABLE last_visit (
  id              UUID PRIMARY KEY,
  user_id         UUID REFERENCES users (id),
  conversation_id UUID REFERENCES conversation (id),
  last_visit_on   TIMESTAMP NOT NULL
);
CREATE UNIQUE INDEX ON last_visit (user_id, conversation_id);