CREATE TABLE message_reply (
  id          UUID PRIMARY KEY,
  message_id  UUID REFERENCES message (id),
  reply_to_id UUID REFERENCES message (id),
  key         VARCHAR(255) NOT NULL
);

INSERT INTO message_reply (id, message_id, reply_to_id, key)
  (
    SELECT
      uuid_generate_v4(),
      m.id,
      m.reply_to,
      concat('@', r.user_name)
    FROM stuha18.message m
      LEFT JOIN stuha18.message r ON m.reply_to = r.id
    WHERE m.reply_to IS NOT NULL
  );

SELECT *
FROM message_reply;