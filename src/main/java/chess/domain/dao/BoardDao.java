package chess.domain.dao;

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
import java.util.HashMap;
import java.util.Map;

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

    public void addChessBoard(Board board) {
        for (File file : File.sorted()) {
            for (Rank rank : Rank.sorted()) {
                Piece piece = board.findPieceBySquare(Square.of(file, rank));
                addBySquare(file, rank, piece);
            }
        }
    }

    private void addBySquare(File file, Rank rank, Piece piece) {
        final var query = "INSERT INTO chessboard VALUES(?, ?, ?, ?, ?)";

        try (final var connection = getConnection();
             final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, file.symbol());
            preparedStatement.setInt(3, rank.value());
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
            preparedStatement.setString(1, file.symbol());
            preparedStatement.setInt(2, rank.value());

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

    public void updateSquarePiece(File file, Rank rank, PieceType newPieceType, ColorType newColorType) {
        final var query = "UPDATE chessboard SET piece_type = ?, piece_color = ? " +
                "WHERE file_value = ? AND rank_value = ?";

        try (final var connection = getConnection();
             final var preparedStatement = connection.prepareStatement(query)) {
             preparedStatement.setString(1, newPieceType.name());
             preparedStatement.setString(2, newColorType.name());
             preparedStatement.setString(3, file.symbol());
             preparedStatement.setInt(4, rank.value());

             preparedStatement.executeUpdate();
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existBoard() {
        final var query = "SELECT COUNT(*) FROM chessboard";

        try (final var connection = getConnection();
             final var preparedStatement = connection.prepareStatement(query)) {

            final var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int exists = resultSet.getInt(1);
                return exists != 0;
            }

        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public Map<Square, Piece> getAllPiecesInfo() {
        final var query = "SELECT * FROM chessboard";
        final Map<Square, Piece> board = new HashMap<>();

        try (final var connection = getConnection();
             final var preparedStatement = connection.prepareStatement(query)) {
            final var resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Square square = Square.from(resultSet.getString("file_value") + resultSet.getString("rank_value"));
                Piece piece = Piece.from(resultSet.getString("piece_color") + resultSet.getString("piece_type"));

                board.put(square, piece);
            }

            preparedStatement.executeQuery();
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }

        return board;
    }

    public void updateBoard(Board board) {
        for (File file : File.sorted()) {
            for (Rank rank : Rank.sorted()) {
                Piece piece = board.findPieceBySquare(Square.of(file, rank));
                updateSquarePiece(file, rank, piece.pieceType(), piece.colorType());
            }
        }
    }

    public void deleteBoard() {
        final var query = "DELETE FROM chessboard";

        try (final var connection = getConnection();
             final var preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.executeUpdate();
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
