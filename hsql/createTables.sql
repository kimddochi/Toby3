DROP TABLE USERS IF EXISTS CASCADE;

CREATE TABLE USERS 
(	
	ID VARCHAR(10) primary key,
	NAME VARCHAR(20) NOT NULL,
	PASSWORD VARCHAR(20) NOT NULL
);
