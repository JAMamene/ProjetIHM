BEGIN TRANSACTION;
CREATE TABLE "source" (
	`_id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`name`	TEXT
);
INSERT INTO `source` VALUES (1,'TWITTER');
INSERT INTO `source` VALUES (2,'MAGASIN');
INSERT INTO `source` VALUES (3,'EVENEMENT');
CREATE TABLE "news" (
	`_id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`title`	NUMERIC,
	`source`	INTEGER,
	`author`	INTEGER,
	`image`	TEXT,
	`content`	TEXT UNIQUE,
	`date`	NUMERIC NOT NULL,
	`event_date`	INTEGER,
	`event_duree`	INTEGER,
	`registered`	INTEGER,
	FOREIGN KEY(`source`) REFERENCES `source`(`_id`),
	FOREIGN KEY(`author`) REFERENCES `author`(`id`)
);
INSERT INTO `news` VALUES (1,'Message de Santeshop',2,4,'http://www.adzif.biz/site/medias/A0363-SEUL.jpg','N''oubliez pas de manger 5 fruits et légumes par jour c''est très très très important voilà je sais pas quoi dire alors je meuble mais oubliez pas de manger équilibré, N''oubliez pas qu''il y a un macdo au deuxième étage du centre merci',1493566319000,'','',NULL);
INSERT INTO `news` VALUES (2,'Annonce de Mosser',2,5,'https://pbs.twimg.com/media/CWXqNGrWEAEfkBD.jpg','Annonce très importante de mosser industries pour dire que les notes de QGL arriveront avec 4 ans de retard, merci de votre compréhension',1494925271000,NULL,NULL,NULL);
INSERT INTO `news` VALUES (3,'Gros evenement',3,2,'https://cannes-program.com/wp-content/uploads/2015/05/evenement.voiron.jpg','Attention gros évenement à venir, préparez vous tous à vivre une expérience incroyable dans un évenement génial wouhou',1493566419000,1493566819000,120,NULL);
INSERT INTO `news` VALUES (4,'Ventes de NieR Automata',2,2,'https://static.gamespot.com/uploads/screen_medium/1406/14063904/3203875-3166347301-Nier-.jpg','Le brillant NieR Automata vient de nous livrer par la voix de son éditeur Square Enix ses premiers chiffres de distribution. En cumulant le nombre d''exemplaires distribués en magasin et les ventes dématérialisées, ce sont pas moins d''un million d''unités du dernier titre de Platinum Games qui se retrouvent donc dans la nature.

Si le chiffre n''apparaît pas comme ronflant comparé aux millions d''exemplaires écoulés par les AAA, il reste appréciable au regard de la confidentialité de l''épisode original, sorti il y a tout juste 7 ans sur PlayStation 3 et Xbox 360. Le démarrage japonais du titre était d''ailleurs excellent d''après Media Create (via Gematsu), avec près de 200 000 exemplaires écoulés dès la première semaine, un nombre atteignant quasiment le total cumulé des ventes du premier épisode sur le même territoire.

Noté 19/20 dans nos colonnes, NieR Automata est déjà disponible sur PC et PlayStation 4.',1493715671000,NULL,NULL,NULL);
INSERT INTO `news` VALUES (5,'Celio promotions Pull',2,3,'https://medias.oas.io/medias/2016/05/04/20/060495c880f6512fb8aa7567be59465f-300x240.jpg','MAJ : retour de la promotion chez Celio jusqu''au 30 mai en ligne mais aussi en magasin ! Pour 2 polos achetés, le 3e est offert (le moins cher des 3). Offre valable sur une sélection d''articles. Offre non valable sur l''achat de carte cadeaux et non cumulable avec toute autre promotion en cours.

En savoir plus : https://www.offresasaisir.fr/promotion/celio-2-polos-achetes-le-3e-offert/14197',1494583271000,NULL,NULL,NULL);
INSERT INTO `news` VALUES (6,'Le 280 de retour',2,6,'http://www.fastandfood.fr/wp-content/uploads/2017/01/280_burger_Mcdonalds.jpg','McDonald’s fait revenir une nouvelle fois le fameux et célèbre 280 dès aujourd’hui, le sandwich au doux poids de 280GR, honnêtement on l’adore alors très heureux de le revoir, on souligne également l’arrivée de deux variantes, la première qui est celle au Chèvre et la seconde qui est celle au Gruyère de France.



Comme d’habitude, les recettes du 280 seront en alternance une semaine sur deux. Vous avez prévu de revenir les tester bientôt ?',1494928871000,NULL,NULL,NULL);
INSERT INTO `news` VALUES (7,'Concours McDonalds',3,6,'http://cdn.quebecconcoursgratuits.com/wp-content/uploads/2015/06/concours-gagnez-carte-cadeau-mcdonalds-de-50-520x325.jpg','Gagnez une carte cadeau d''une valeur de 50 euros à McDonalds le 18 avril seulement, participation gratuite, évenement disponible ce jour là seulement wahou',1494965600000,1495065600000,1440,NULL);
INSERT INTO `news` VALUES (8,'Participez au jeu santé',3,4,'http://www.sante-en-jeux.fr/images/santeEnJeux_logo_240.png','Grand jeu pour la santé par SantéShop

Rejoignez nous mercredi pour participer à un jeu incroyable sur le thème de la santé avec plein de prix à la clé, évenement à CapSophia, mercredi après midi, venez nombreux et apprenez en vous amusant.

Pour toute la famille !',1494446400000,1495029600000,240,NULL);
CREATE TABLE `category` (
	`_id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`name`	TEXT
);
INSERT INTO `category` VALUES (1,'MODE');
INSERT INTO `category` VALUES (2,'ALIMENTATION');
INSERT INTO `category` VALUES (3,'INFORMATION');
INSERT INTO `category` VALUES (4,'SANTE');
INSERT INTO `category` VALUES (5,'MEME');
INSERT INTO `category` VALUES (6,'TECHNOLOGIE');
INSERT INTO `category` VALUES (7,'MAROQUINERIE');
INSERT INTO `category` VALUES (8,'ACCESSOIRES');
INSERT INTO `category` VALUES (9,'DECO');
INSERT INTO `category` VALUES (10,'JEUXVIDEO');
INSERT INTO `category` VALUES (11,'STAGE');
INSERT INTO `category` VALUES (12,'CONCOURS');
INSERT INTO `category` VALUES (13,'JEU');
CREATE TABLE `categ_link` (
	`idnews`	INTEGER NOT NULL,
	`idcateg`	INTEGER NOT NULL,
	PRIMARY KEY(`idnews`,`idcateg`),
	FOREIGN KEY(`idnews`) REFERENCES news(_id),
	FOREIGN KEY(`idcateg`) REFERENCES category(_id)
);
INSERT INTO `categ_link` VALUES (1,3);
INSERT INTO `categ_link` VALUES (1,4);
INSERT INTO `categ_link` VALUES (2,5);
INSERT INTO `categ_link` VALUES (2,8);
INSERT INTO `categ_link` VALUES (3,3);
INSERT INTO `categ_link` VALUES (3,11);
INSERT INTO `categ_link` VALUES (4,6);
INSERT INTO `categ_link` VALUES (4,10);
INSERT INTO `categ_link` VALUES (5,7);
INSERT INTO `categ_link` VALUES (5,1);
INSERT INTO `categ_link` VALUES (6,2);
INSERT INTO `categ_link` VALUES (7,12);
INSERT INTO `categ_link` VALUES (7,2);
INSERT INTO `categ_link` VALUES (8,13);
INSERT INTO `categ_link` VALUES (8,4);
CREATE TABLE "author" (
	`_id`	INTEGER PRIMARY KEY AUTOINCREMENT,
	`name`	TEXT UNIQUE,
	`following`	INTEGER
);
INSERT INTO `author` VALUES (1,'JA',0);
INSERT INTO `author` VALUES (2,'Micromania',0);
INSERT INTO `author` VALUES (3,'Celio',NULL);
INSERT INTO `author` VALUES (4,'SanteShop',NULL);
INSERT INTO `author` VALUES (5,'Mosser',NULL);
INSERT INTO `author` VALUES (6,'McDonalds',NULL);
CREATE TABLE "android_metadata" ("locale" TEXT DEFAULT 'fr_FR');
INSERT INTO `android_metadata` VALUES ('fr_FR');
COMMIT;
