CREATE TABLE IF NOT EXISTS lecturer
(
    id             INT AUTO_INCREMENT PRIMARY KEY,
    name           VARCHAR(250) NOT NULL,
    designation    VARCHAR(600) NOT NULL,
    qualifications VARCHAR(600) NOT NULL,
    picture        VARCHAR(200)  DEFAULT NULL,
    linkedin       VARCHAR(2000) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS full_time_rank (
    lecturer_id INT NOT NULL ,
    `rank` INT NOT NULL ,
    CONSTRAINT pk_full PRIMARY KEY (lecturer_id, `rank`),
    CONSTRAINT fk_full FOREIGN KEY (lecturer_id) REFERENCES lecturer (id)
);

CREATE TABLE IF NOT EXISTS part_time_rank (
    lecturer_id INT NOT NULL ,
    `rank` INT NOT NULL ,
    CONSTRAINT pk_part PRIMARY KEY (lecturer_id, `rank`),
    CONSTRAINT fk_part FOREIGN KEY (lecturer_id) REFERENCES lecturer (id)
);