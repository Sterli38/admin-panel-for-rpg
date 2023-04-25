DROP TABLE if EXISTS players;
DROP TABLE if EXISTS race;
DROP TABLE if EXISTS profession;

CREATE TABLE race
(
    id  serial primary key,
    name varchar NOT NULL
);

INSERT INTO race(name)
values ('HUMAN'),
       ('DWARF'),
       ('ELF'),
       ('GIANT'),
       ('ORC'),
       ('TROLL'),
       ('HOBBIT');

CREATE TABLE profession
(
    id   serial primary key,
    name varchar NOT NULL
);

INSERT INTO profession(name)
values ('WARRIOR'),
       ('ROGUE'),
       ('SORCERER'),
       ('CLERIC'),
       ('PALADIN'),
       ('NAZGUL'),
       ('WARLOCK'),
       ('DRUID');

CREATE TABLE players
(
    id               serial PRIMARY KEY,
    name             varchar(12)                       NOT NULL,
    title            varchar(30)                       NOT NULL,
    race_id          int REFERENCES race (id)          NOT NULL,
    profession_id    int REFERENCES profession (id)    NOT NULL,
    experience       int CHECK (experience < 10000000) NOT NULL,
    level            int                               NOT NULL,
    until_next_level int                               NOT NULL,
    birthday         date                              NOT NULL,
    banned           boolean                           NOT NULL
);