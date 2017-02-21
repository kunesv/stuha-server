CREATE TABLE conversation (
  id    CHARACTER VARYING(36) NOT NULL,
  title CHARACTER VARYING(255)
);

CREATE TABLE icon (
  id      CHARACTER VARYING(36) NOT NULL,
  alt     CHARACTER VARYING(4),
  path    CHARACTER VARYING(8),
  user_id CHARACTER VARYING(36)
);

CREATE TABLE image (
  id         CHARACTER VARYING(36) NOT NULL,
  image      BYTEA,
  message_id CHARACTER VARYING(36),
  name       CHARACTER VARYING(255),
  thumbnail  BYTEA
);

CREATE TABLE message (
  id              CHARACTER VARYING(36)  NOT NULL,
  conversation_id CHARACTER VARYING(36)  NOT NULL,
  created_on      TIMESTAMP              NOT NULL,
  formatted       TEXT,
  icon_path       CHARACTER VARYING(8),
  reply_to        CHARACTER VARYING(36),
  rough           TEXT,
  user_id         CHARACTER VARYING(36)  NOT NULL,
  user_name       CHARACTER VARYING(255) NOT NULL
);

CREATE TABLE token (
  id              CHARACTER VARYING(36) NOT NULL,
  auto_revalidate BOOLEAN,
  created_on      TIMESTAMP,
  token           CHARACTER VARYING(255),
  user_id         CHARACTER VARYING(36) NOT NULL
);

CREATE TABLE user_credentials (
  id       CHARACTER VARYING(36) NOT NULL,
  password CHARACTER VARYING(255),
  username CHARACTER VARYING(255)
);

CREATE TABLE users (
  id       CHARACTER VARYING(36) NOT NULL,
  name     CHARACTER VARYING(255),
  username CHARACTER VARYING(255)
);

CREATE TABLE user_conversation (
  id              CHARACTER VARYING(36) NOT NULL,
  user_id         CHARACTER VARYING(36) NOT NULL,
  conversation_id CHARACTER VARYING(36) NOT NULL
);

ALTER TABLE ONLY conversation
  ADD CONSTRAINT conversation_pkey PRIMARY KEY (id);

ALTER TABLE ONLY icon
  ADD CONSTRAINT icon_pkey PRIMARY KEY (id);

ALTER TABLE ONLY image
  ADD CONSTRAINT image_pkey PRIMARY KEY (id);

ALTER TABLE ONLY message
  ADD CONSTRAINT message_pkey PRIMARY KEY (id);

ALTER TABLE ONLY token
  ADD CONSTRAINT token_pkey PRIMARY KEY (id);

ALTER TABLE ONLY user_credentials
  ADD CONSTRAINT user_credentials_pkey PRIMARY KEY (id);

ALTER TABLE ONLY users
  ADD CONSTRAINT users_pkey PRIMARY KEY (id);

ALTER TABLE ONLY user_conversation
  ADD CONSTRAINT user_conversation_pkey PRIMARY KEY (id);