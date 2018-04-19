alter table conversation add column last_message_on TIMESTAMP;


DO language plpgsql $$
DECLARE
  row_var record;
  lmo timestamp ;
BEGIN
  FOR row_var IN SELECT id,title FROM conversation LOOP
    raise notice 'Value: %', row_var.id;
    lmo := (select max(created_on) from message where conversation_id = row_var.id);

    raise notice 'Last Message: %', lmo;

    if lmo is null then
      update conversation set last_message_on = (CURRENT_TIMESTAMP - INTERVAL '10 days')::timestamp where id = row_var.id;
    else
      update conversation set last_message_on = lmo where id = row_var.id;
    end if;
  END LOOP;
END
$$;

ALTER TABLE conversation ALTER COLUMN last_message_on SET NOT NULL;