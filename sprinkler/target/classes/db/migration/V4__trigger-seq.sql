CREATE OR REPLACE TRIGGER trg_sprinkler_id
BEFORE INSERT ON tbl_sprinkler
FOR EACH ROW
BEGIN
    :new.ID := seq_sprinkler_id.NEXTVAL;
END;
/
