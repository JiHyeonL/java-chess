CREATE DATABASE chess DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE chess;

CREATE TABLE chessboard (
    board_id BIGINT NOT NULL AUTO_INCREMENT,
    file_value VARCHAR(10) NOT NULL,
    rank_value VARCHAR(10) NOT NULL,
    piece_type VARCHAR(10) NOT NULL,
    piece_color VARCHAR(10) NOT NULL,

    PRIMARY KEY (board_id)
);

DROP TABLE IF EXISTS chessboard;
