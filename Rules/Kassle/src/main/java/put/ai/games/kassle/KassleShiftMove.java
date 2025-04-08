/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.kassle;

import put.ai.games.game.Player.Color;
import put.ai.games.game.moves.impl.ShiftMoveImpl;

public class KassleShiftMove extends ShiftMoveImpl {

    public KassleShiftMove(int x, Direction direction, Color color) {
        super(direction, x, color);
    }


    @Override
    public String toString() {
        return String.format("%s(%d)", getDirection(), getX());
    }

}
