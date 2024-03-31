package chess.domain.database;

import chess.domain.board.Board;
import chess.domain.piece.ColorType;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceType;
import chess.domain.position.File;
import chess.domain.position.Rank;
import chess.domain.position.Square;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BoardDao {
    private static final String SERVER = "localhost:13306"; // MySQL 서버 주소
    private static final String DATABASE = "chess"; // MySQL DATABASE 이름
    private static final String OPTION = "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USERNAME = "root"; //  MySQL 서버 아이디
    private static final String PASSWORD = "root"; // MySQL 서버 비밀번호

    public Connection getConnection() {
        // 드라이버 연결
        try {
            return DriverManager.getConnection("jdbc:mysql://" + SERVER + "/" + DATABASE + OPTION, USERNAME, PASSWORD);
        } catch (final SQLException e) {
            System.err.println("DB 연결 오류:" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void addChessBoard(int boardId, Board board) {
        for (File file : File.sorted()) {
            for (Rank rank : Rank.sorted()) {
                Piece piece = board.findPieceBySquare(Square.of(file, rank));
                addBySquare(boardId, file, rank, piece);
            }
        }
    }

    private void addBySquare(int boardId, File file, Rank rank, Piece piece) {
        final var query = "INSERT INTO chessboard VALUES(?, ?, ?, ?, ?)";

        try (final var connection = getConnection();
             final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, boardId);
            preparedStatement.setString(2, file.name());
            preparedStatement.setString(3, rank.name());
            preparedStatement.setString(4, piece.pieceType().name());
            preparedStatement.setString(5, piece.colorType().name());

            preparedStatement.executeUpdate();
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Piece findPieceBySquare(File file, Rank rank) {
        final var query = "SELECT * FROM chessboard WHERE file_value = ? AND rank_value = ?";
        try (final var connection = getConnection();
             final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, file.name());
            preparedStatement.setString(2, rank.name());

            final var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Piece.from(
                        resultSet.getString("piece_color") +
                        resultSet.getString("piece_type")
                );
            }
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public void updateBoard(File file, Rank rank, PieceType newPieceType, ColorType newColorType) {
        final var query = "UPDATE chessboard SET piece_type = ?, piece_color = ? " +
                "WHERE file_value = ? AND rank_value = ?";

        try (final var connection = getConnection();
             final var preparedStatement = connection.prepareStatement(query)) {
             preparedStatement.setString(1, newPieceType.name());
             preparedStatement.setString(2, newColorType.name());
             preparedStatement.setString(3, file.name());
             preparedStatement.setString(4, rank.name());

             preparedStatement.executeUpdate();
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void getAllBoardInfo() {

    }
}
