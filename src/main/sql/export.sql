--------------------------------------------------------
--  File created - Friday-July-07-2017   
--------------------------------------------------------

CREATE SEQUENCE  "KTM"."SEQ_DEPENDENCY"  MINVALUE 0 INCREMENT BY 1 START WITH 0 NOORDER  NOCYCLE ;
CREATE SEQUENCE  "KTM"."SEQ_DATA"  MINVALUE 0 INCREMENT BY 1 START WITH 0 NOORDER  NOCYCLE ;
CREATE SEQUENCE  "KTM"."SEQ_ERROR"  MINVALUE 0 INCREMENT BY 1 START WITH 0 NOORDER  NOCYCLE ;
CREATE SEQUENCE  "KTM"."SEQ_FILE"  MINVALUE 0 INCREMENT BY 1 START WITH 0 NOORDER  NOCYCLE ;



--------------------------------------------------------
--  DDL for Table KTM_TABLE_DATA
--------------------------------------------------------

  CREATE TABLE "KTM"."KTM_TABLE_DATA" 
   (	"KTM_TS" TIMESTAMP (6), 
	"KTM_STATE" VARCHAR2(20 BYTE), 
	"KTM_PK_ID" VARCHAR2(40 BYTE)
   ) ;
--------------------------------------------------------
--  DDL for Table KTM_TABLE_DEPENDENCY
--------------------------------------------------------

  CREATE TABLE "KTM"."KTM_TABLE_DEPENDENCY" 
   (	"KTM_TS" TIMESTAMP (6), 
	"KTM_STATE" VARCHAR2(20 BYTE), 
	"KTM_PK_ID" VARCHAR2(40 BYTE)
   ) ;
--------------------------------------------------------
--  DDL for Table KTM_TABLE_ERROR
--------------------------------------------------------

  CREATE TABLE "KTM"."KTM_TABLE_ERROR" 
   (	"KTM_TS" TIMESTAMP (6), 
	"KTM_STATE" VARCHAR2(20 BYTE), 
	"KTM_PK_ID" VARCHAR2(40 BYTE)
   ) ;
--------------------------------------------------------
--  DDL for Table KTM_TABLE_FILE
--------------------------------------------------------

  CREATE TABLE "KTM"."KTM_TABLE_FILE" 
   (	"KTM_TS" TIMESTAMP (6), 
	"KTM_STATE" VARCHAR2(20 BYTE), 
	"KTM_PK_ID" VARCHAR2(40 BYTE)
   );
--------------------------------------------------------
--  DDL for Table TABLE_DATA
--------------------------------------------------------

  CREATE TABLE "KTM"."TABLE_DATA" 
   (	"COLUMN_INTEGER" NUMBER, 
	"COLUMN_STRING" VARCHAR2(1000 BYTE), 
	"COLUMN_DOUBLE" NUMBER, 
	"COLUMN_DATE" TIMESTAMP (6), 
	"BATCH_ID" VARCHAR2(100 BYTE), 
	"DATE" TIMESTAMP (6), 
	"ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table TABLE_DEPENDENCY
--------------------------------------------------------

  CREATE TABLE "KTM"."TABLE_DEPENDENCY" 
   (	"COLUMN_INTEGER" NUMBER, 
	"COLUMN_STRING" VARCHAR2(1000 BYTE), 
	"COLUMN_INTEGER_FK" NUMBER, 
	"BATCH_ID" VARCHAR2(100 BYTE), 
	"DATE" TIMESTAMP (6), 
	"ID" NUMBER
   ) ;
--------------------------------------------------------
--  DDL for Table TABLE_ERROR
--------------------------------------------------------

  CREATE TABLE "KTM"."TABLE_ERROR" 
   (	"DATE" TIMESTAMP (6), 
	"FILE" VARCHAR2(1000 BYTE), 
	"LINE" NUMBER, 
	"ITEM" VARCHAR2(100 BYTE), 
	"VALUE" VARCHAR2(1000 BYTE), 
	"REASON" VARCHAR2(1000 BYTE), 
	"BATCH_ID" VARCHAR2(1000 BYTE), 
	"ID" NUMBER
   )  ;
--------------------------------------------------------
--  DDL for Table TABLE_FILE
--------------------------------------------------------

  CREATE TABLE "KTM"."TABLE_FILE" 
   (	"DATE" TIMESTAMP (6), 
	"FILE" VARCHAR2(1000 BYTE), 
	"COUNT_LINES" NUMBER, 
	"COUNT_LINES_ERROR" NUMBER, 
	"BATCH_ID" VARCHAR2(100 BYTE), 
	"ID" NUMBER
   )  ;
--------------------------------------------------------
--  DDL for Trigger TRG_TABLE_DATA_D
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "KTM"."TRG_TABLE_DATA_D" BEFORE DELETE ON TABLE_DATA FOR EACH ROW
BEGIN 
    INSERT INTO KTM_TABLE_DATA (KTM_TS, KTM_STATE, KTM_PK_ID) VALUES (SYSTIMESTAMP, 'D', :OLD.ID);
END;
/
ALTER TRIGGER "KTM"."TRG_TABLE_DATA_D" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRG_TABLE_DATA_I
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "KTM"."TRG_TABLE_DATA_I" AFTER INSERT ON TABLE_DATA FOR EACH ROW
BEGIN 
    INSERT INTO KTM_TABLE_DATA (KTM_TS, KTM_STATE, KTM_PK_ID) VALUES (SYSTIMESTAMP, 'I', :NEW.ID);
END;
/
ALTER TRIGGER "KTM"."TRG_TABLE_DATA_I" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRG_TABLE_DATA_U
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "KTM"."TRG_TABLE_DATA_U" AFTER UPDATE ON TABLE_DATA FOR EACH ROW
BEGIN 
    INSERT INTO KTM_TABLE_DATA (KTM_TS, KTM_STATE, KTM_PK_ID) VALUES (SYSTIMESTAMP, 'U', :NEW.ID);
END;
/
ALTER TRIGGER "KTM"."TRG_TABLE_DATA_U" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRG_TABLE_DEPENDENCY
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "KTM"."TRG_TABLE_DEPENDENCY" AFTER INSERT ON TABLE_DEPENDENCY FOR EACH ROW
BEGIN 
    INSERT INTO KTM_TABLE_DEPENDENCY (KTM_TS, KTM_STATE, KTM_PK_ID) VALUES (SYSTIMESTAMP, 'I', :NEW.ID);
END;
/
ALTER TRIGGER "KTM"."TRG_TABLE_DEPENDENCY" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRG_TABLE_DEPENDENCY_D
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "KTM"."TRG_TABLE_DEPENDENCY_D" BEFORE DELETE ON TABLE_DEPENDENCY FOR EACH ROW
BEGIN 
    INSERT INTO KTM_TABLE_DEPENDENCY (KTM_TS, KTM_STATE, KTM_PK_ID) VALUES (SYSTIMESTAMP, 'D', :OLD.ID);
END;
/
ALTER TRIGGER "KTM"."TRG_TABLE_DEPENDENCY_D" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRG_TABLE_DEPENDENCY_U
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "KTM"."TRG_TABLE_DEPENDENCY_U" AFTER UPDATE ON TABLE_DEPENDENCY FOR EACH ROW
BEGIN 
    INSERT INTO KTM_TABLE_DEPENDENCY (KTM_TS, KTM_STATE, KTM_PK_ID) VALUES (SYSTIMESTAMP, 'U', :NEW.ID);
END;
/
ALTER TRIGGER "KTM"."TRG_TABLE_DEPENDENCY_U" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRG_TABLE_ERROR_D
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "KTM"."TRG_TABLE_ERROR_D" BEFORE DELETE ON TABLE_ERROR FOR EACH ROW
BEGIN 
    INSERT INTO KTM_TABLE_ERROR (KTM_TS, KTM_STATE, KTM_PK_ID) VALUES (SYSTIMESTAMP, 'D', :OLD.ID);
END;
/
ALTER TRIGGER "KTM"."TRG_TABLE_ERROR_D" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRG_TABLE_ERROR_I
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "KTM"."TRG_TABLE_ERROR_I" AFTER INSERT ON TABLE_ERROR FOR EACH ROW
BEGIN 
    INSERT INTO KTM_TABLE_ERROR (KTM_TS, KTM_STATE, KTM_PK_ID) VALUES (SYSTIMESTAMP, 'I', :NEW.ID);
END;
/
ALTER TRIGGER "KTM"."TRG_TABLE_ERROR_I" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRG_TABLE_ERROR_U
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "KTM"."TRG_TABLE_ERROR_U" AFTER UPDATE ON TABLE_ERROR FOR EACH ROW
BEGIN 
    INSERT INTO KTM_TABLE_ERROR (KTM_TS, KTM_STATE, KTM_PK_ID) VALUES (SYSTIMESTAMP, 'U', :NEW.ID);
END;
/
ALTER TRIGGER "KTM"."TRG_TABLE_ERROR_U" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRG_TABLE_FILE_D
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "KTM"."TRG_TABLE_FILE_D" BEFORE DELETE ON TABLE_FILE FOR EACH ROW
BEGIN 
    INSERT INTO KTM_TABLE_FILE (KTM_TS, KTM_STATE, KTM_PK_ID) VALUES (SYSTIMESTAMP, 'D', :OLD.ID);
END;
/
ALTER TRIGGER "KTM"."TRG_TABLE_FILE_D" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRG_TABLE_FILE_I
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "KTM"."TRG_TABLE_FILE_I" AFTER INSERT ON TABLE_FILE FOR EACH ROW
BEGIN 
    INSERT INTO KTM_TABLE_FILE (KTM_TS, KTM_STATE, KTM_PK_ID) VALUES (SYSTIMESTAMP, 'I', :NEW.ID);
END;
/
ALTER TRIGGER "KTM"."TRG_TABLE_FILE_I" ENABLE;
--------------------------------------------------------
--  DDL for Trigger TRG_TABLE_FILE_U
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "KTM"."TRG_TABLE_FILE_U" AFTER UPDATE ON TABLE_FILE FOR EACH ROW
BEGIN 
    INSERT INTO KTM_TABLE_FILE (KTM_TS, KTM_STATE, KTM_PK_ID) VALUES (SYSTIMESTAMP, 'U', :NEW.ID);
END;
/
ALTER TRIGGER "KTM"."TRG_TABLE_FILE_U" ENABLE;
