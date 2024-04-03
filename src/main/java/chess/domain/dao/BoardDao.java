package chess.domain.dao;

import chess.domain.board.Board;
import chess.domain.piece.ColorType;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceType;
import chess.domain.position.File;
import chess.domain.position.Rank;
import chess.domain.position.Square;
import chess.domain.state.TurnState;
import chess.util.DatabaseConnector;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class BoardDao {

    private final DatabaseConnector connector;

    public BoardDao(DatabaseConnector connector) {
        this.connector = connector;
    }

    public void addBoard(Board board) {
        for (File file : File.sorted()) {
            for (Rank rank : Rank.sorted()) {
                Piece piece = board.findPieceBySquare(Square.of(file, rank));
                addSquareInfo(file, rank, piece, board.turnState());
            }
        }
    }

    private void addSquareInfo(File file, Rank rank, Piece piece, TurnState turn) {
        final var query = "INSERT INTO chessboard (file_value, rank_value, piece_type, piece_color, turn) VALUES(?, ?, ?, ?, ?)";

        try (final var connection = connector.getConnection();
             final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, file.symbol());
            preparedStatement.setInt(2, rank.value());
            preparedStatement.setString(3, piece.pieceType().name());
            preparedStatement.setString(4, piece.colorType().name());
            preparedStatement.setString(5, turn.name());

            preparedStatement.executeUpdate();
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Piece findPieceBySquare(File file, Rank rank) {
        final var query = "SELECT * FROM chessboard WHERE file_value = ? AND rank_value = ?";
        try (final var connection = connector.getConnection();
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

    public boolean existBoard() {
        final var query = "SELECT COUNT(*) FROM chessboard";

        try (final var connection = connector.getConnection();
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

    public Board selectTotalBoard() {
        final var query = "SELECT * FROM chessboard";
        final Map<Square, Piece> board = new HashMap<>();

        try (final var connection = connector.getConnection();
             final var preparedStatement = connection.prepareStatement(query)) {

            final var resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Square square = Square.from(resultSet.getString("file_value") + resultSet.getString("rank_value"));
                Piece piece = Piece.from(resultSet.getString("piece_color") + resultSet.getString("piece_type"));
                board.put(square, piece);
            }

            return new Board(board, selectTurn().decideTurn());
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public TurnState selectTurn() {
        final var query = "SELECT turn FROM chessboard";
        try (final var connection = connector.getConnection();
             final var preparedStatement = connection.prepareStatement(query)) {

            final var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return TurnState.findByName(
                        resultSet.getString("turn")
                );
            }
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public void updateBoard(Board board) {
        for (File file : File.sorted()) {
            for (Rank rank : Rank.sorted()) {
                Piece piece = board.findPieceBySquare(Square.of(file, rank));
                updateSquareInfo(file, rank, piece.pieceType(), piece.colorType(), board.turnState());
            }
        }
    }

    public void updateSquareInfo(File file, Rank rank, PieceType newPieceType, ColorType newColorType, TurnState turn) {
        final var query = "UPDATE chessboard SET piece_type = ?, piece_color = ? , turn = ?" +
                "WHERE file_value = ? AND rank_value = ?";

        try (final var connection = connector.getConnection();
             final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, newPieceType.name());
            preparedStatement.setString(2, newColorType.name());
            preparedStatement.setString(3, turn.name());
            preparedStatement.setString(4, file.symbol());
            preparedStatement.setInt(5, rank.value());

            preparedStatement.executeUpdate();
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteBoard() {
        final var query = "DELETE FROM chessboard";

        try (final var connection = connector.getConnection();
             final var preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.executeUpdate();
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
