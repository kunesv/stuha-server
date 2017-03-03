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
  icon_path       VARCHAR(8),
  reply_to        UUID REFERENCES message (id),
  rough           TEXT,
  user_name       VARCHAR(255) NOT NULL
);

CREATE TABLE image (
  id         UUID PRIMARY KEY,
  image      BYTEA        NOT NULL,
  message_id UUID REFERENCES message (id),
  name       VARCHAR(255) NOT NULL,
  thumbnail  BYTEA        NOT NULL
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