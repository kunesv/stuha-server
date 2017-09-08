INSERT INTO users (id, name, username) VALUES ('eeeeeee1-bbbb-cccc-eeee-ffffffffffff', 'Test User', 'test');
INSERT INTO users (id, name, username) VALUES ('eeeeeee2-bbbb-cccc-eeee-ffffffffffff', 'Some Other User', 'other');
INSERT INTO user_credentials (id, username, password)
VALUES ('aaaaaaa1-bbbb-cccc-eeee-ffffffffffff', 'test', '$2a$10$v.0aqv0VQGL8TdLRE66t6.R5rWLxEPKJjHXQRNDnrE1BeGMOup7Hi');
INSERT INTO user_credentials (id, username, password) VALUES
  ('aaaaaaa2-bbbb-cccc-eeee-ffffffffffff', 'other', '$2a$10$nkJOaaXq5tSECIr7QxU28OIzsCfUXVZYoPRQf7suNpLkkbUBHymAO');
INSERT INTO icon (id, path, alt, user_id)
VALUES ('ccccccc1-bbbb-cccc-eeee-ffffffffffff', '2_1', ':)', 'eeeeeee1-bbbb-cccc-eeee-ffffffffffff');
INSERT INTO icon (id, path, alt, user_id)
VALUES ('ccccccc2-bbbb-cccc-eeee-ffffffffffff', '2_2', ':D', 'eeeeeee1-bbbb-cccc-eeee-ffffffffffff');
INSERT INTO icon (id, path, alt, user_id)
VALUES ('ccccccc3-bbbb-cccc-eeee-ffffffffffff', '2_3', ':O', 'eeeeeee1-bbbb-cccc-eeee-ffffffffffff');
INSERT INTO icon (id, path, alt, user_id)
VALUES ('ccccccc4-bbbb-cccc-eeee-ffffffffffff', '0_0', ':O', 'eeeeeee2-bbbb-cccc-eeee-ffffffffffff');
INSERT INTO conversation (id, title) VALUES ('ccccccc1-bbbb-cccc-eeee-ffffffffffff', 'Conversation 1');
INSERT INTO conversation (id, title) VALUES ('ccccccc2-bbbb-cccc-eeee-ffffffffffff', 'Conversation 2');
INSERT INTO user_conversation (id, user_id, conversation_id) VALUES
  ('ccccccc1-bbbb-cccc-eeee-ffffffffffff', 'eeeeeee1-bbbb-cccc-eeee-ffffffffffff',
   'ccccccc1-bbbb-cccc-eeee-ffffffffffff');
INSERT INTO user_conversation (id, user_id, conversation_id) VALUES
  ('ccccccc2-bbbb-cccc-eeee-ffffffffffff', 'eeeeeee1-bbbb-cccc-eeee-ffffffffffff',
   'ccccccc2-bbbb-cccc-eeee-ffffffffffff');
INSERT INTO user_conversation (id, user_id, conversation_id) VALUES
  ('ccccccc3-bbbb-cccc-eeee-ffffffffffff', 'eeeeeee2-bbbb-cccc-eeee-ffffffffffff',
   'ccccccc1-bbbb-cccc-eeee-ffffffffffff');
INSERT INTO token (id, auto_revalidate, created_on, token, user_id) VALUES
  ('11111111-2222-3333-4444-555555555555', TRUE, CURRENT_TIMESTAMP,
   'I5aDtwfM1OrBtgYlsoM7QvOxhqRWDSNzDqxXNWjlzacgnGxaN6EExbRK4IF13JBZp8qvcJDRjaHDxnrlhOWNpnXddSyFUSyqC2JVDDvbwCmlNM3rHqqM2OThuV03X2EP',
   'eeeeeee1-bbbb-cccc-eeee-ffffffffffff');