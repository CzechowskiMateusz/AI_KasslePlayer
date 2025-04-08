/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.kassle;

import put.ai.games.game.Player.Color;
import put.ai.games.game.moves.impl.PlaceMoveImpl;

public class KassleDropMove extends PlaceMoveImpl {

    private boolean shift = false;


    public KassleDropMove(int x, int y, Color c) {
        super(x, y, c);
    }


    @Override
    public String toString() {
        return String.format("%s@(%d,%d)", getColor(), getX(), getY());
    }


    public boolean isPreviousMoveShift() {
        return shift;
    }


    public void setPreviousMoveShift(boolean b) {
        this.shift = b;
    }
}
