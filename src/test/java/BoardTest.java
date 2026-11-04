
import model.Board;
import model.Square;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    private final Board board = new Board();


    @Test
    void shouldCreateBoardWith16RowsAnd16ColumnsEach(){
        var res = board.getBoard();
        assertEquals(16, res.length);
        assertEquals(16, res[0].length);
    }

    @Test
    void shouldCreateBoardWith16RowsAnd16ColumnsEach_whenUsingCreateBoard(){
        board.createBoard();
        var res = board.getBoard();

        Square firstSquare = res[0][0];
        Square lastSquare = res[15][15];

        // Testar raden
        assertEquals(0, firstSquare.getRow());
        assertEquals(15, lastSquare.getRow());

        // Testar kolumnen
        assertEquals(0, firstSquare.getCol());
        assertEquals(15, lastSquare.getCol());

    }






}
