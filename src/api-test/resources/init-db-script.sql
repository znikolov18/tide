USE tide_it;

CREATE TABLE `announcement` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `subject` varchar(100) NOT NULL,
  `summary` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `vote` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `announcement_id` int(11) NOT NULL,
  `positive` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `vote_user_id_announcement_id_uq` (`user_id`,`announcement_id`),
  KEY `vote_announcement_id_fk` (`announcement_id`),
  CONSTRAINT `vote_announcement_id_fk` FOREIGN KEY (`announcement_id`) REFERENCES `announcement` (`id`),
  CONSTRAINT `vote_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE OR REPLACE VIEW vote_result_vw AS 
SELECT a.id AS announcement_id,
SUM((case when (v.positive = 1) THEN 1 else 0 END)) AS likes,
SUM((case when (v.positive = 0) THEN 1 else 0 END)) AS dislikes 
FROM announcement a LEFT JOIN vote v 
ON v.announcement_id = a.id 
GROUP BY a.id ORDER BY a.id;


INSERT INTO announcement (subject,summary) VALUES ('TestSubject1','TestSummary1');
INSERT INTO announcement (subject,summary) VALUES ('TestSubject2','TestSummary2');
INSERT INTO announcement (subject,summary) VALUES ('TestSubject3','TestSummary3');

INSERT INTO user (name) VALUES ('Adam');
INSERT INTO user (name) VALUES ('John');
INSERT INTO user (name) VALUES ('Fred');

INSERT INTO vote (user_id,announcement_id,positive) VALUES ((SELECT id FROM user WHERE name = 'Adam'), (SELECT id FROM announcement WHERE subject = 'TestSubject3'), 0);
INSERT INTO vote (user_id,announcement_id,positive) VALUES ((SELECT id FROM user WHERE name = 'Fred'), (SELECT id FROM announcement WHERE subject = 'TestSubject2'), 1);
INSERT INTO vote (user_id,announcement_id,positive) VALUES ((SELECT id FROM user WHERE name = 'Fred'), (SELECT id FROM announcement WHERE subject = 'TestSubject3'), 1);
INSERT INTO vote (user_id,announcement_id,positive) VALUES ((SELECT id FROM user WHERE name = 'John'), (SELECT id FROM announcement WHERE subject = 'TestSubject3'), 0);
INSERT INTO vote (user_id,announcement_id,positive) VALUES ((SELECT id FROM user WHERE name = 'Adam'), (SELECT id FROM announcement WHERE subject = 'TestSubject2'), 1);
INSERT INTO vote (user_id,announcement_id,positive) VALUES ((SELECT id FROM user WHERE name = 'Adam'), (SELECT id FROM announcement WHERE subject = 'TestSubject1'), 1);
INSERT INTO vote (user_id,announcement_id,positive) VALUES ((SELECT id FROM user WHERE name = 'Fred'), (SELECT id FROM announcement WHERE subject = 'TestSubject1'), 0);
INSERT INTO vote (user_id,announcement_id,positive) VALUES ((SELECT id FROM user WHERE name = 'John'), (SELECT id FROM announcement WHERE subject = 'TestSubject1'), 1);
INSERT INTO vote (user_id,announcement_id,positive) VALUES ((SELECT id FROM user WHERE name = 'John'), (SELECT id FROM announcement WHERE subject = 'TestSubject2'), 1);
