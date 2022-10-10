CREATE TABLE IF NOT EXISTS users (
  user_id INTEGER NOT NULL AUTO_INCREMENT,
  username varchar(45) NOT NULL,
  password varchar(150) NOT NULL,
  salt varchar(150) NOT NULL,
  f_name varchar(50),
  l_name varchar(50),
  PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS files (
  file_id INTEGER NOT NULL AUTO_INCREMENT,
  file_name varchar(50) NOT NULL,
  file_binary binary NOT NULL,
  PRIMARY KEY (file_id)
);

CREATE TABLE IF NOT EXISTS notes (
  note_id INTEGER NOT NULL AUTO_INCREMENT,
  title varchar(50) NOT NULL,
  description varchar(250) NOT NULL,
  PRIMARY KEY (note_id)
);

CREATE TABLE IF NOT EXISTS credentials (
  credential_id INTEGER NOT NULL AUTO_INCREMENT,
  url varchar(150) NOT NULL,
  username varchar(45) NOT NULL,
  password varchar(150) NOT NULL,
  PRIMARY KEY (credential_id)
);