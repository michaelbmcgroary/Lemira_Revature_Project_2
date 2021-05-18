DROP TABLE IF EXISTS glb_reviews;
DROP TABLE IF EXISTS glb_users;
DROP TABLE IF EXISTS glb_review_status;
DROP TABLE IF EXISTS glb_user_type;
DROP TABLE IF EXISTS glb_user_status;



CREATE TABLE glb_user_status (
	user_status_id INT NOT NULL UNIQUE,
	user_status VARCHAR(10) NOT NULL UNIQUE,
	PRIMARY KEY (user_status_id)
);

CREATE TABLE glb_user_type (
	user_type_id INT NOT NULL UNIQUE,
	user_type VARCHAR(10) NOT NULL UNIQUE,
	PRIMARY KEY (user_type_id)
);

CREATE TABLE glb_review_status (
	review_completion_status_id INT NOT NULL UNIQUE,
	review_completion_status VARCHAR(20) NOT NULL UNIQUE,
	PRIMARY KEY (review_completion_status_id)
);

CREATE TABLE glb_users (
	userID INT NOT NULL UNIQUE AUTO_INCREMENT,
	username VARCHAR(50) NOT NULL UNIQUE,
	password VARCHAR(50) NOT NULL,
	user_firstname VARCHAR(100) NOT NULL,
	user_lastname VARCHAR(100) NOT NULL,
	user_email VARCHAR(150) NOT NULL,
	user_type_id INT NOT NULL,
    user_status_id INT NOT NULL,
	PRIMARY KEY (userID),
	FOREIGN KEY (user_type_id) REFERENCES glb_user_type(user_type_id),
    FOREIGN KEY (user_status_id) REFERENCES glb_user_status(user_status_id)
);

CREATE TABLE glb_reviews (
	reviewID INT NOT NULL UNIQUE AUTO_INCREMENT,
	userID INT NOT NULL,
    review_rating INT NOT NULL,
    review_description VARCHAR(500),
    review_hours_played DOUBLE NOT NULL,
    review_completion_status_id INT NOT NULL,
    game_id INT,
	PRIMARY KEY(reviewID),
	FOREIGN KEY (userID) REFERENCES glb_users(userID),
    FOREIGN KEY (review_completion_status_id) REFERENCES glb_review_status(review_completion_status_id)
);
-- glb_reviews is missing the final foreign key for the game information and will be updated as soon as we get the API working


INSERT INTO glb_user_status (user_status_id, user_status)
	VALUES (1, "Active"), (2, "Banned");

INSERT INTO glb_user_type (user_type_id, user_type)
	VALUES (1, "Basic"), (2, "Moderator");

INSERT INTO glb_review_status (review_completion_status_id, review_completion_status)
	VALUES (1, "Played"), (2, "Finished"), (3, "Completed"), (4, "Watched Playthrough");




INSERT INTO glb_users (
    userID,
    username,
    password,
    user_firstname,
    user_lastname,
    user_email,
    user_type_id,
    user_status_id
) VALUES (
    1,
    "Username",
    "144656446c77a6b15e7d2be7cfc4ae78", -- Password123*
    "George",
    "Lucas",
    "glucas@gmail.com",
    2,
    1
), (
    2,
    "Username2",
    "5c58865e6b26294fa4558c88a55d66c2", -- Password69420
    "Johnny",
    "Depp",
    "jdepp@gmail.com",
    1,
    1
), (
    3,
    "Username3",
    "7c89d143715e59c1967e4e3040a6f9e1", -- YoThisIsMyPassword
    "Tommy",
    "Wiseau",
    "TWiseau@gmail.com",
    1,
    2
);

INSERT INTO glb_reviews (
    reviewID,
    userID,
    review_rating,
    review_description,
    review_hours_played,
    review_completion_status_id,
    game_id
) VALUES (
    1,
    2, -- Johnny Depp
    10, -- 10/10
    "Expansive powers, amazing characters, fun movement, emotional story, and amazing visuals. Fantastic game.",
    58.6, -- Hours
    3, -- Completed
    515 -- inFAMOUS 2
), (
    2,
    3, -- Tommy Wiseau
    8, -- 8/10
    "Frustratingly difficult, but really rewarding if you can end up nailing the combat and movenemt. The story sucks and is way too predictable, but the gameplay is worth checking it out",
    12, -- Hours
    2, -- Finished
    121752 -- Ghostrunner
), (
    3,
    1, -- George Lucas
    5, -- 5/10
    "Was amazing for the time and was a fantastic reference point for games to build themselves off of, but even if it's \"infuential\", it doesn't mean it's the most fun thing in the world to actually play today",
    15, -- Hours
    1, -- Played
    1029 -- Ocarina of Time
), (
	4,
	3, -- Tommy Wiseau
	9, -- 9/10
	"Really weird concept, but executed well, interesting characters, cool mysteries, upsetting when characters die. It's far from perfect and if you don't like reading, this is definitely not the game for you, but for this strange concept, it works really well.",
	63, -- Hours
	3, -- Completed
	7842 -- Danganronpa Trigger Happy Havoc
), (
	5,
	1, -- Pick a user soon
	6, -- 6/10
	"Amazing tension, interesting puzzles, cool artstyle.... REALLY bad ending that ruins the whole adventure leading up to it",
	3.4, -- Hours
	2, -- Finished
	7342 -- INSIDE
), (
	6,
	2, -- Pick a user soon
	10, -- 10/10
	"Stop what you're doing, go play this game, it's the definition of fun, easier to pick up and play than Smash Bros, and an even higher skill ceiling for pros, PLAY. THIS. GAME.",
	12.6, -- Hours
	1, -- Played
	59858 -- Lethal League Blaze
), (
	7,
	3, -- Pick a user soon
	10, -- 10/10
	"I bought a VR headset just to play this game and it was worth every single penny I spent, fantastic game",
	150, -- Hours
	3, -- Completed
	83731 -- Beat Saber
), (
	8,
	1, -- Pick a user soon
	8, -- 8/10
	"Amazing base game, but the expansion this time around leaves a lot to be desired, only one new character that isn't the most interesting, but still absolutely love the world, gameplay and pure HYPE this game has",
	15, -- Hours
	4, -- Watched Playthrough
	121369 -- UNICLR
), (
	9,
	2, -- Pick a user soon
	10, -- 10/10
	"It's god damn Portal 2, need I say more?",
	42.3, -- Hours
	3, -- Completed
	72 -- Portal 2
), (
	10,
	3, -- Pick User soon
	7, -- 7/10
	"Underrated gem, nothing amazing, a pretty standard twin stick shooted, but hey, it's a GOOD twin stick shooter that's a perfect game to wind down with after a long day, the emphasis on stealth and baiting out the enemies is great!",
	30.2, -- Hours
	2, -- Finished
	18954 -- Neon Chrome
), (
	11,
	1, -- Pick a user soon
	6, -- 6/10
	"As of writing this review, this game is still $60, full price, and it's using the PS2 versions of the songs, and is so lacking in content, it's far from worth it, I hope it gets free DLC or a price drop to make it more worth it",
	6, -- Hours
	1, -- Played
	135108 -- Kingdom Hearts: Memory of Melody
);

SELECT * FROM glb_review_status;
SELECT * FROM glb_user_type;
SELECT * FROM glb_user_status;
SELECT * FROM glb_users;
SELECT * FROM glb_reviews;