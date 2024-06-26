DROP TABLE IF EXISTS USERS CASCADE;
DROP TABLE IF EXISTS ITEMS CASCADE;
DROP TABLE IF EXISTS BOOKINGS CASCADE;
DROP TABLE IF EXISTS COMMENTS CASCADE;
DROP TABLE IF EXISTS REQUESTS CASCADE;

CREATE TABLE IF NOT EXISTS USERS(
  ID BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
  NAME VARCHAR(256) NOT NULL,
  EMAIL VARCHAR(512) NOT NULL,
  CONSTRAINT PK_USER PRIMARY KEY (ID),
  CONSTRAINT UQ_USER_EMAIL UNIQUE (EMAIL)
);

CREATE TABLE IF NOT EXISTS REQUESTS (
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    DESCRIPTION VARCHAR (500) NOT NULL,
    REQUESTER_ID BIGINT,
    CREATED TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT PK_REQUESTS PRIMARY KEY (ID),
    CONSTRAINT FK_REQUESTS_USERS FOREIGN KEY (REQUESTER_ID) REFERENCES USERS (ID)
    );

CREATE TABLE IF NOT EXISTS ITEMS(
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    NAME VARCHAR(256) NOT NULL,
    DESCRIPTION VARCHAR(500),
    AVAILABLE BOOLEAN,
    OWNER BIGINT,
    REQUEST_ID BIGINT,
    CONSTRAINT PK_ITEM PRIMARY KEY (ID),
    CONSTRAINT FK_ITEMS_USERS FOREIGN KEY (OWNER) REFERENCES USERS (ID),
    CONSTRAINT FK_ITEMS_REQUESTS FOREIGN KEY (REQUEST_ID) REFERENCES REQUESTS (ID)
);

CREATE TABLE IF NOT EXISTS BOOKINGS (
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    START_DATE TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    END_DATE TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    ITEM_ID BIGINT,
    BOOKER_ID BIGINT,
    STATUS VARCHAR(20),
    CONSTRAINT PK_BOOKING PRIMARY KEY (ID),
    CONSTRAINT FK_BOOKINGS_USERS FOREIGN KEY (BOOKER_ID) REFERENCES USERS (ID),
    CONSTRAINT FK_BOOKINGS_ITEMS FOREIGN KEY (ITEM_ID) REFERENCES ITEMS (ID)
);

CREATE TABLE IF NOT EXISTS COMMENTS (
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    TEXT VARCHAR(512),
    ITEM_ID BIGINT,
    AUTHOR_ID BIGINT,
    CREATED TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT PK_COMMENTS PRIMARY KEY (ID),
    CONSTRAINT FK_COMMENTS_USERS FOREIGN KEY (AUTHOR_ID) REFERENCES USERS (ID),
    CONSTRAINT FK_COMMENTS_ITEMS FOREIGN KEY (ITEM_ID) REFERENCES ITEMS (ID)
);

