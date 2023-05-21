CREATE TABLE IF NOT EXISTS ROLE_AUTHORITY(
    ROLE_ID BIGINT NOT NULL,
    AUTHORITY_ID BIGINT NOT NULL,

    PRIMARY KEY (ROLE_ID, AUTHORITY_ID),

    CONSTRAINT fk_role_authority_account_role
        FOREIGN KEY (ROLE_ID)
            REFERENCES ACCOUNT_USER (ID),

    CONSTRAINT fk_role_authority_authority
        FOREIGN KEY (AUTHORITY_ID )
            REFERENCES AUTHORITY (ID)
);