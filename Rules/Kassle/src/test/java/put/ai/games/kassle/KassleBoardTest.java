/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.kassle;

import org.junit.Before;
import org.junit.Test;
import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;
import put.ai.games.game.Player.Color;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * 
 * @author smaug
 */
public class KassleBoardTest {

    private KassleBoard b;


    @Before
    public void setup() {
        b = new KassleBoard(5);
    }


    @Test
    public void dropTest() {
        b.doMove(new KassleDropMove(2, 3, Player.Color.PLAYER1));
        assertEquals(Player.Color.PLAYER1, b.getState(2, 3));
        for (int i = 0; i < b.getSize(); ++i) {
            for (int j = 0; j < b.getSize(); ++j) {
                if (i != 2 && j != 3) {
                    assertEquals(Player.Color.EMPTY, b.getState(i, j));
                }
            }
        }
    }


    private Color[] row(int row) {
        Color[] result = new Color[b.getSize()];
        for (int i = 0; i < result.length; ++i) {
            result[i] = b.getState(i, row);
        }
        return result;
    }


    private Color[] col(int col) {
        Color[] result = new Color[b.getSize()];
        for (int i = 0; i < result.length; ++i) {
            result[i] = b.getState(col, i);
        }
        return result;
    }


    private String toString(Color[] row) {
        String result = "";
        for (Color c : row) {
            switch (c) {
                case PLAYER1:
                    result += "1";
                    break;
                case PLAYER2:
                    result += "2";
                    break;
                case EMPTY:
                    result += "_";
                    break;
            }
        }
        return result;
    }


    @Test
    public void leftRightTest() {
        b.doMove(new KassleDropMove(3, 3, Player.Color.PLAYER1));
        b.doMove(new KassleDropMove(4, 3, Player.Color.PLAYER2));
        b.doMove(new KassleShiftMove(3, KassleShiftMove.Direction.RIGHT, Player.Color.PLAYER1));
        assertEquals("2___1", toString(row(3)));
        b.lastMoveWasShift = false;
        b.doMove(new KassleShiftMove(3, KassleShiftMove.Direction.RIGHT, Player.Color.PLAYER1));
        assertEquals("12___", toString(row(3)));
        b.lastMoveWasShift = false;
        b.doMove(new KassleShiftMove(3, KassleShiftMove.Direction.RIGHT, Player.Color.PLAYER1));
        assertEquals("_12__", toString(row(3)));
        b.lastMoveWasShift = false;
        b.doMove(new KassleShiftMove(3, KassleShiftMove.Direction.LEFT, Player.Color.PLAYER1));
        assertEquals("12___", toString(row(3)));
        b.lastMoveWasShift = false;
        b.doMove(new KassleShiftMove(3, KassleShiftMove.Direction.LEFT, Player.Color.PLAYER1));
        assertEquals("2___1", toString(row(3)));
        b.lastMoveWasShift = false;
        b.doMove(new KassleShiftMove(3, KassleShiftMove.Direction.LEFT, Player.Color.PLAYER1));
        assertEquals("___12", toString(row(3)));
    }


    @Test
    public void upDownTest() {
        b.doMove(new KassleDropMove(3, 3, Player.Color.PLAYER1));
        b.doMove(new KassleDropMove(3, 4, Player.Color.PLAYER2));
        b.doMove(new KassleShiftMove(3, KassleShiftMove.Direction.DOWN, Player.Color.PLAYER1));
        assertEquals("2___1", toString(col(3)));
        b.lastMoveWasShift = false;
        b.doMove(new KassleShiftMove(3, KassleShiftMove.Direction.DOWN, Player.Color.PLAYER1));
        assertEquals("12___", toString(col(3)));
        b.lastMoveWasShift = false;
        b.doMove(new KassleShiftMove(3, KassleShiftMove.Direction.DOWN, Player.Color.PLAYER1));
        assertEquals("_12__", toString(col(3)));
        b.lastMoveWasShift = false;
        b.doMove(new KassleShiftMove(3, KassleShiftMove.Direction.UP, Player.Color.PLAYER1));
        assertEquals("12___", toString(col(3)));
        b.lastMoveWasShift = false;
        b.doMove(new KassleShiftMove(3, KassleShiftMove.Direction.UP, Player.Color.PLAYER1));
        assertEquals("2___1", toString(col(3)));
        b.lastMoveWasShift = false;
        b.doMove(new KassleShiftMove(3, KassleShiftMove.Direction.UP, Player.Color.PLAYER1));
        assertEquals("___12", toString(col(3)));
    }


    @Test(expected = IllegalArgumentException.class)
    public void consecutiveShiftTest() {
        b.doMove(new KassleShiftMove(3, KassleShiftMove.Direction.DOWN, Player.Color.PLAYER1));
        b.doMove(new KassleShiftMove(3, KassleShiftMove.Direction.DOWN, Player.Color.PLAYER2));
    }


    @Test
    public void undoTest() {
        Move[] m = new Move[] { new KassleDropMove(3, 3, Player.Color.PLAYER1),
                new KassleShiftMove(3, KassleShiftMove.Direction.DOWN, Player.Color.PLAYER2),
                new KassleDropMove(2, 2, Player.Color.PLAYER1),
                new KassleShiftMove(2, KassleShiftMove.Direction.RIGHT, Player.Color.PLAYER2), };
        Board[] boards = new Board[m.length];
        for (int i = 0; i < m.length; ++i) {
            boards[i] = b.clone();
            b.doMove(m[i]);
            assertFalse(b.equals(boards[i]));
        }
        for (int i = m.length - 1; i >= 0; --i) {
            b.undoMove(m[i]);
            assertEquals(boards[i], b);
        }
    }


    @Test
    public void winCol() {
        for (int i = 0; i < b.getSize(); ++i) {
            b.doMove(new KassleDropMove(2, i, Color.PLAYER2));
            if (i < b.getSize() - 1) {
                b.doMove(new KassleDropMove(3, i, Color.PLAYER1));
            }
        }
        assertEquals(Color.PLAYER2, b.getWinner(Color.EMPTY));
    }


    @Test
    public void winRow() {
        for (int i = 0; i < b.getSize(); ++i) {
            b.doMove(new KassleDropMove(i, 2, Color.PLAYER1));
            if (i < b.getSize() - 1) {
                b.doMove(new KassleDropMove(i, 3, Color.PLAYER2));
            }
        }
        assertEquals(Color.PLAYER1, b.getWinner(Color.EMPTY));
    }
}
