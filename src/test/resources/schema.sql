CREATE TABLE conversation (
  id    CHARACTER VARYING(255) NOT NULL,
  title CHARACTER VARYING(255)
);

ALTER TABLE ONLY conversation
  ADD CONSTRAINT conversation_pkey PRIMARY KEY (id);

CREATE TABLE image (
  id         CHARACTER VARYING(255) NOT NULL,
  image      BYTEA,
  message_id CHARACTER VARYING(255),
  name       CHARACTER VARYING(255),
  thumbnail  BYTEA
);

ALTER TABLE ONLY image
  ADD CONSTRAINT image_pkey PRIMARY KEY (id);


CREATE TABLE message (
  id              CHARACTER VARYING(255) NOT NULL,
  conversation_id CHARACTER VARYING(255),
  created_on      BYTEA,
  formatted       CHARACTER VARYING(255),
  icon_path       CHARACTER VARYING(255),
  reply_to        CHARACTER VARYING(255),
  rough           CHARACTER VARYING(255),
  user_name       CHARACTER VARYING(255)
);

ALTER TABLE ONLY message
  ADD CONSTRAINT message_pkey PRIMARY KEY (id);


CREATE TABLE token (
  id      CHARACTER VARYING(255) NOT NULL,
  token   CHARACTER VARYING(255),
  user_id CHARACTER VARYING(255)
);

ALTER TABLE ONLY token
  ADD CONSTRAINT token_pkey PRIMARY KEY (id);


CREATE TABLE user_credentials (
  id       CHARACTER VARYING(255) NOT NULL,
  username CHARACTER VARYING(255),
  password CHARACTER VARYING(255)
);

ALTER TABLE ONLY user_credentials
  ADD CONSTRAINT user_credentials_pkey PRIMARY KEY (id);

CREATE TABLE users (
  id       CHARACTER VARYING(255) NOT NULL,
  name     CHARACTER VARYING(255),
  username CHARACTER VARYING(255)
);

ALTER TABLE ONLY users
  ADD CONSTRAINT users_pkey PRIMARY KEY (id);