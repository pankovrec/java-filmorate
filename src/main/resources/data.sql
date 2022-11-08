MERGE INTO genres key (GENRE_ID) values (1, 'Комедия');
MERGE INTO genres key (GENRE_ID) values (2, 'Драма');
MERGE INTO genres key (GENRE_ID) values (3, 'Мультфильм');
MERGE INTO genres key (GENRE_ID) values (4, 'Триллер');
MERGE INTO genres key (GENRE_ID) values (5, 'Документальный');
MERGE INTO genres key (GENRE_ID) values (6, 'Боевик');

MERGE INTO MPA_RATING key (MPA_ID) values (1, 'G');
MERGE INTO MPA_RATING key (MPA_ID) values (2, 'PG');
MERGE INTO MPA_RATING key (MPA_ID) values (3, 'PG-13');
MERGE INTO MPA_RATING key (MPA_ID) values (4, 'R');
MERGE INTO MPA_RATING key (MPA_ID) values (5, 'NC-17');