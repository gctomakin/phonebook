 # --- !Ups
CREATE TABLE "CONTACTS" (
  "ID" BIGSERIAL NOT NULL PRIMARY KEY,
  "FULLNAME" VARCHAR(255) NOT NULL,
  "NUMBER" VARCHAR(255) NOT NULL
);


 # --- !Downs
DROP TABLE "CONTACTS";
