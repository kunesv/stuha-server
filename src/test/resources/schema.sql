CREATE TABLE conversation (
  id    UUID NOT NULL,
  title CHARACTER VARYING(255),
  PRIMARY KEY (id)
);

CREATE TABLE icon (
  id      UUID NOT NULL,
  alt     CHARACTER VARYING(4),
  path    CHARACTER VARYING(8),
  user_id UUID NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE image (
  id         UUID                   NOT NULL,
  image      BYTEA                  NOT NULL,
  message_id UUID                   NOT NULL,
  name       CHARACTER VARYING(255) NOT NULL,
  thumbnail  BYTEA                  NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE message (
  id              UUID                   NOT NULL,
  conversation_id UUID                   NOT NULL,
  created_on      TIMESTAMP              NOT NULL,
  formatted       TEXT,
  icon_path       CHARACTER VARYING(8),
  reply_to        UUID,
  rough           TEXT,
  user_id         UUID                   NOT NULL,
  user_name       CHARACTER VARYING(255) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE token (
  id              UUID NOT NULL,
  auto_revalidate BOOLEAN,
  created_on      TIMESTAMP,
  token           CHARACTER VARYING(255),
  user_id         UUID NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE user_credentials (
  id       UUID NOT NULL,
  password CHARACTER VARYING(255),
  username CHARACTER VARYING(255),
  PRIMARY KEY (id)
);

CREATE TABLE users (
  id       UUID NOT NULL,
  name     CHARACTER VARYING(255),
  username CHARACTER VARYING(255),
  PRIMARY KEY (id)
);

CREATE TABLE user_conversation (
  id              UUID NOT NULL,
  user_id         UUID NOT NULL,
  conversation_id UUID NOT NULL,
  PRIMARY KEY (id)
);