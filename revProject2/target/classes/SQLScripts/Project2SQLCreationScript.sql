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

INSERT INTO glb_user_status (user_status_id, user_status)
	VALUES (1, "Active"), (2, "Banned");

INSERT INTO glb_user_type (user_type_id, user_type)
	VALUES (1, "Basic"), (2, "Moderator");

INSERT INTO glb_review_status (review_completion_status_id, review_completion_status)
	VALUES (1, "Played"), (2, "Finished"), (3, "Completed"), (4, "Watched Playthrough");




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