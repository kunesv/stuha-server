ALTER TABLE message_award
  ADD COLUMN conversation_id UUID REFERENCES conversation (id);

ALTER TABLE message_award
  ALTER COLUMN conversation_id SET NOT NULL;