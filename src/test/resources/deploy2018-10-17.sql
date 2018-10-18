ALTER TABLE message_award
  ADD COLUMN icon_path VARCHAR(8);

ALTER TABLE message_award
  ALTER COLUMN icon_path SET NOT NULL;