CREATE SEQUENCE janitor_image_seq
    INCREMENT 1
    START 3
    MINVALUE 1;
ALTER TABLE JANITOR_IMAGE ALTER ID SET DEFAULT NEXTVAL('janitor_image_seq');