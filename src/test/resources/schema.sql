CREATE TABLE race
(
    id   identity primary key,
    name varchar NOT NULL UNIQUE
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
    id   identity primary key,
    name varchar NOT NULL UNIQUE
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
    id               identity PRIMARY KEY,
    name             varchar(12)                       NOT NULL,
    title            varchar(30)                       NOT NULL,
    race_id          int REFERENCES race (id),
    profession_id    int REFERENCES profession (id),
    experience       int CHECK (experience < 10000000) NOT NULL,
    level            int                               NOT NULL,
    until_next_level int                               NOT NULL,
    birthday         date                              NOT NULL,
    banned           boolean                           NOT NULL
);