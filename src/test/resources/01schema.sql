CREATE TABLE users (
  id       UUID PRIMARY KEY,
  name     VARCHAR(255) UNIQUE NOT NULL,
  username VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE user_credentials (
  id       UUID PRIMARY KEY REFERENCES users (id),
  username VARCHAR(255) REFERENCES users (username) NOT NULL,
  password VARCHAR(255)                             NOT NULL
);

CREATE TABLE conversation (
  id      UUID PRIMARY KEY,
  title   VARCHAR(255) UNIQUE NOT NULL,
  no_join BOOLEAN             NOT NULL
);

CREATE TABLE icon (
  id      UUID PRIMARY KEY,
  alt     VARCHAR(4)                 NOT NULL,
  path    VARCHAR(8)                 NOT NULL,
  user_id UUID REFERENCES users (id) NOT NULL
);
CREATE INDEX ON icon (user_id);

CREATE TABLE message (
  id              UUID PRIMARY KEY,
  conversation_id UUID REFERENCES conversation (id) NOT NULL,
  created_on      TIMESTAMP                         NOT NULL,
  formatted       TEXT,
  pictures        TEXT,
  icon_path       VARCHAR(8)                        NOT NULL,
  reply_to        UUID REFERENCES message (id),
  rough           TEXT,
  user_name       VARCHAR(255)                      NOT NULL
);
CREATE INDEX ON message (conversation_id, created_on);

CREATE TABLE file (
  id              UUID PRIMARY KEY,
  conversation_id UUID REFERENCES conversation (id) NULL,
  content_type    VARCHAR(255)                      NOT NULL,
  file            BYTEA                             NOT NULL
);

CREATE TABLE picture (
  id         UUID PRIMARY KEY REFERENCES file (id),
  message_id UUID REFERENCES message (id) NULL,
  user_id    UUID REFERENCES users (id)   NULL,
  name       VARCHAR(1023)                NOT NULL,
  height     INTEGER                      NOT NULL,
  width      INTEGER                      NOT NULL
);

CREATE TABLE thumbnail (
  id              UUID PRIMARY KEY REFERENCES file (id),
  conversation_id UUID REFERENCES conversation (id) NULL,
  content_type    VARCHAR(255)                      NOT NULL,
  thumbnail       BYTEA                             NOT NULL
);

CREATE TABLE token (
  id              UUID PRIMARY KEY,
  auto_revalidate BOOLEAN                    NOT NULL,
  last_update     TIMESTAMP                  NOT NULL,
  token           VARCHAR(255)               NOT NULL,
  user_id         UUID REFERENCES users (id) NOT NULL,
  revalidated     BOOLEAN                    NOT NULL
);
CREATE INDEX ON token (user_id, token);

CREATE TABLE user_conversation (
  id              UUID PRIMARY KEY,
  user_id         UUID REFERENCES users (id)        NOT NULL,
  conversation_id UUID REFERENCES conversation (id) NOT NULL
);
CREATE UNIQUE INDEX ON user_conversation (conversation_id, user_id);

CREATE TABLE message_reply (
  id          UUID PRIMARY KEY,
  message_id  UUID REFERENCES message (id) NOT NULL,
  reply_to_id UUID REFERENCES message (id) NOT NULL,
  key         VARCHAR(255)                 NOT NULL
);

CREATE TABLE last_visit (
  id              UUID PRIMARY KEY,
  user_id         UUID REFERENCES users (id)        NOT NULL,
  conversation_id UUID REFERENCES conversation (id) NOT NULL,
  last_visit_on   TIMESTAMP                         NOT NULL
);
CREATE UNIQUE INDEX ON last_visit (user_id, conversation_id);
CREATE INDEX ON last_visit (user_id);