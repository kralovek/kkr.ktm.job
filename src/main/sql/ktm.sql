CREATE TABLE KTM_TABLE_DATA (
    KTM_TS TIMESTAMP, 
    KTM_STATE VARCHAR2(20 BYTE), 
    KTM_PK_ID VARCHAR2(40 BYTE),
    PRIMARY KEY (KTM_TS)
);
/
CREATE TABLE KTM_TABLE_ERROR (
    KTM_TS TIMESTAMP, 
    KTM_STATE VARCHAR2(20 BYTE), 
    KTM_PK_ID VARCHAR2(40 BYTE),
    PRIMARY KEY (KTM_TS)
);
/
CREATE TABLE KTM_TABLE_FILE (
    KTM_TS TIMESTAMP, 
    KTM_STATE VARCHAR2(20 BYTE), 
    KTM_PK_ID VARCHAR2(40 BYTE),
    PRIMARY KEY (KTM_TS)
);
/

-----------------------------------------------------------------
CREATE OR REPLACE TRIGGER TRG_TABLE_DATA_I AFTER INSERT ON TABLE_DATA FOR EACH ROW
BEGIN 
    INSERT INTO KTM_TABLE_DATA (KTM_TS, KTM_STATE, KTM_PK_ID) VALUES (SYSTIMESTAMP, 'I', :NEW.ID);
END;
/
CREATE OR REPLACE TRIGGER TRG_TABLE_DATA_U AFTER UPDATE ON TABLE_DATA FOR EACH ROW
BEGIN 
    INSERT INTO KTM_TABLE_DATA (KTM_TS, KTM_STATE, KTM_PK_ID) VALUES (SYSTIMESTAMP, 'U', :NEW.ID);
END;
/
CREATE OR REPLACE TRIGGER TRG_TABLE_DATA_D BEFORE DELETE ON TABLE_DATA FOR EACH ROW
BEGIN 
    INSERT INTO KTM_TABLE_DATA (KTM_TS, KTM_STATE, KTM_PK_ID) VALUES (SYSTIMESTAMP, 'D', :OLD.ID);
END;
/

-----------------------------------------------------------------
CREATE OR REPLACE TRIGGER TRG_TABLE_ERROR_I AFTER INSERT ON TABLE_ERROR FOR EACH ROW
BEGIN 
    INSERT INTO KTM_TABLE_ERROR (KTM_TS, KTM_STATE, KTM_PK_ID) VALUES (SYSTIMESTAMP, 'I', :NEW.ID);
END;
/
CREATE OR REPLACE TRIGGER TRG_TABLE_ERROR_U AFTER UPDATE ON TABLE_ERROR FOR EACH ROW
BEGIN 
    INSERT INTO KTM_TABLE_ERROR (KTM_TS, KTM_STATE, KTM_PK_ID) VALUES (SYSTIMESTAMP, 'U', :NEW.ID);
END;
/
CREATE OR REPLACE TRIGGER TRG_TABLE_ERROR_D BEFORE DELETE ON TABLE_ERROR FOR EACH ROW
BEGIN 
    INSERT INTO KTM_TABLE_ERROR (KTM_TS, KTM_STATE, KTM_PK_ID) VALUES (SYSTIMESTAMP, 'D', :OLD.ID);
END;
/

-----------------------------------------------------------------
CREATE OR REPLACE TRIGGER TRG_TABLE_FILE_I AFTER INSERT ON TABLE_FILE FOR EACH ROW
BEGIN 
    INSERT INTO KTM_TABLE_FILE (KTM_TS, KTM_STATE, KTM_PK_ID) VALUES (SYSTIMESTAMP, 'I', :NEW.ID);
END;
/
CREATE OR REPLACE TRIGGER TRG_TABLE_FILE_U AFTER UPDATE ON TABLE_FILE FOR EACH ROW
BEGIN 
    INSERT INTO KTM_TABLE_FILE (KTM_TS, KTM_STATE, KTM_PK_ID) VALUES (SYSTIMESTAMP, 'U', :NEW.ID);
END;
/
CREATE OR REPLACE TRIGGER TRG_TABLE_FILE_D BEFORE DELETE ON TABLE_FILE FOR EACH ROW
BEGIN 
    INSERT INTO KTM_TABLE_FILE (KTM_TS, KTM_STATE, KTM_PK_ID) VALUES (SYSTIMESTAMP, 'D', :OLD.ID);
END;
/

