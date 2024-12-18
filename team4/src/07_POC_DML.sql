INSERT INTO players(player_name, player_country)
VALUES  ('Anna','Ukraine'),
        ('michael','Germany'),
        ('Artem','Ukraine'),
        ('Adewale','Belgium'),
        ('nasir','Germany');

INSERT INTO game_info(player_id, start_time, end_time, game_outcome, score, session_duration)
VALUES  (1,TO_TIMESTAMP('2024/12/11 10:23:11', 'YYYY/MM/DD HH24:MI:SS'),
         TO_TIMESTAMP('2024/12/11 10:24:43', 'YYYY/MM/DD HH24:MI:SS'),'Win',98,92),
        (1,TO_TIMESTAMP('2024/12/11 10:24:51', 'YYYY/MM/DD HH24:MI:SS'),
         TO_TIMESTAMP('2024/12/11 10:27:53', 'YYYY/MM/DD HH24:MI:SS'),'Win',78,182),
        (1,TO_TIMESTAMP('2024/12/12 16:26:11', 'YYYY/MM/DD HH24:MI:SS'),
         TO_TIMESTAMP('2024/12/12 16:26:23', 'YYYY/MM/DD HH24:MI:SS'),'Lose',12,12),
        (1,TO_TIMESTAMP('2024/12/12 16:26:31', 'YYYY/MM/DD HH24:MI:SS'),
         TO_TIMESTAMP('2024/12/12 16:34:57', 'YYYY/MM/DD HH24:MI:SS'),'Win',76,506),
        (1,TO_TIMESTAMP('2024/12/13 08:42:01', 'YYYY/MM/DD HH24:MI:SS'),
         TO_TIMESTAMP('2024/12/13 08:44:48', 'YYYY/MM/DD HH24:MI:SS'),'Lose',43,167),
        (1,TO_TIMESTAMP('2024/12/13 22:07:54', 'YYYY/MM/DD HH24:MI:SS'),
         TO_TIMESTAMP('2024/12/13 22:10:57', 'YYYY/MM/DD HH24:MI:SS'),'Win',94,183),
        (1,TO_TIMESTAMP('2024/12/14 13:33:21', 'YYYY/MM/DD HH24:MI:SS'),
         TO_TIMESTAMP('2024/12/14 13:36:23', 'YYYY/MM/DD HH24:MI:SS'),'Lose',3,182),
        (2,TO_TIMESTAMP('2024/12/11 23:23:21', 'YYYY/MM/DD HH24:MI:SS'),
         TO_TIMESTAMP('2024/12/11 23:31:43', 'YYYY/MM/DD HH24:MI:SS'),'Lose',34,497),
        (2,TO_TIMESTAMP('2024/12/11 23:32:01', 'YYYY/MM/DD HH24:MI:SS'),
         TO_TIMESTAMP('2024/12/11 23:34:13', 'YYYY/MM/DD HH24:MI:SS'),'Win',78,132),
        (2,TO_TIMESTAMP('2024/12/11 23:34:21', 'YYYY/MM/DD HH24:MI:SS'),
         TO_TIMESTAMP('2024/12/11 23:35:03', 'YYYY/MM/DD HH24:MI:SS'),'Lose',21,42),
        (2,TO_TIMESTAMP('2024/12/11 23:35:11', 'YYYY/MM/DD HH24:MI:SS'),
         TO_TIMESTAMP('2024/12/11 23:38:13', 'YYYY/MM/DD HH24:MI:SS'),'Win',89,182),
        (2,TO_TIMESTAMP('2024/12/13 13:22:11', 'YYYY/MM/DD HH24:MI:SS'),
         TO_TIMESTAMP('2024/12/13 13:27:33', 'YYYY/MM/DD HH24:MI:SS'),'Win',99,322),
        (2,TO_TIMESTAMP('2024/12/14 07:13:41', 'YYYY/MM/DD HH24:MI:SS'),
         TO_TIMESTAMP('2024/12/14 07:17:58', 'YYYY/MM/DD HH24:MI:SS'),'Lose',46,257),
        (3,TO_TIMESTAMP('2024/12/11 03:13:21', 'YYYY/MM/DD HH24:MI:SS'),
         TO_TIMESTAMP('2024/12/11 03:16:38', 'YYYY/MM/DD HH24:MI:SS'),'Win',87,197),
        (3,TO_TIMESTAMP('2024/12/11 03:16:41', 'YYYY/MM/DD HH24:MI:SS'),
         TO_TIMESTAMP('2024/12/11 03:18:58', 'YYYY/MM/DD HH24:MI:SS'),'Lose',33,137),
        (3,TO_TIMESTAMP('2024/12/12 12:03:01', 'YYYY/MM/DD HH24:MI:SS'),
         TO_TIMESTAMP('2024/12/12 12:06:38', 'YYYY/MM/DD HH24:MI:SS'),'Win',67,217),
        (3,TO_TIMESTAMP('2024/12/13 16:33:01', 'YYYY/MM/DD HH24:MI:SS'),
         TO_TIMESTAMP('2024/12/13 16:34:02', 'YYYY/MM/DD HH24:MI:SS'),'Win',67,61),
        (3,TO_TIMESTAMP('2024/12/14 13:03:42', 'YYYY/MM/DD HH24:MI:SS'),
         TO_TIMESTAMP('2024/12/14 13:05:57', 'YYYY/MM/DD HH24:MI:SS'),'Lose',54,135),
        (4,TO_TIMESTAMP('2024/12/11 12:53:42', 'YYYY/MM/DD HH24:MI:SS'),
         TO_TIMESTAMP('2024/12/11 12:56:53', 'YYYY/MM/DD HH24:MI:SS'),'Win',77,191),
        (4,TO_TIMESTAMP('2024/12/13 02:03:12', 'YYYY/MM/DD HH24:MI:SS'),
         null,null,100,100),
        (4,TO_TIMESTAMP('2024/12/14 11:04:12', 'YYYY/MM/DD HH24:MI:SS'),
         TO_TIMESTAMP('2024/12/14 11:06:13', 'YYYY/MM/DD HH24:MI:SS'),'Lose',32,121),
        (5,TO_TIMESTAMP('2024/12/11 13:03:02', 'YYYY/MM/DD HH24:MI:SS'),
         TO_TIMESTAMP('2024/12/11 13:06:13', 'YYYY/MM/DD HH24:MI:SS'),'Win',90,191),
        (5,TO_TIMESTAMP('2024/12/12 10:05:23', 'YYYY/MM/DD HH24:MI:SS'),
         TO_TIMESTAMP('2024/12/12 10:06:43', 'YYYY/MM/DD HH24:MI:SS'),'Lose',24,80);

INSERT INTO board_state
VALUES (1,20,1,1,'[31m0[0m',1),
       (2,20,1,2,'[34m0[0m',0),
       (3,20,1,3,'[34m1[0m',0),
       (4,20,1,4,'[31m1[0m',1),
       (5,20,2,1,'_',0),
       (6,20,2,2,'_',0),
       (7,20,2,3,'[31m1[0m',1),
       (8,20,2,4,'_',0),
       (9,20,3,1,'_',0),
       (10,20,3,2,'[31m1[0m',1),
       (11,20,3,3,'_',0),
       (12,20,3,4,'_',0),
       (13,20,4,1,'[34m1[0m',0),
       (14,20,4,2,'[34m1[0m',0),
       (15,20,4,3,'[34m0[0m',0),
       (16,20,4,4,'[31m0[0m',1);
