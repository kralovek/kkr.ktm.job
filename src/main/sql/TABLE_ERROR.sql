  CREATE TABLE "KKR"."TABLE_ERROR" 
   (    "DATE" TIMESTAMP (6), 
    "FILE" VARCHAR2(1000 BYTE), 
    "LINE" NUMBER, 
    "ITEM" VARCHAR2(100 BYTE), 
    "VALUE" VARCHAR2(1000 BYTE), 
    "REASON" VARCHAR2(1000 BYTE), 
    "BATCH_ID" VARCHAR2(1000 BYTE) NOT NULL ENABLE
   );