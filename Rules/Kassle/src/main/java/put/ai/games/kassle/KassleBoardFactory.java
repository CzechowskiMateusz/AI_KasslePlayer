/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.kassle;

import put.ai.games.engine.UniversalBoardFactory;
import put.ai.games.engine.parameters.IntegerParameter;

public class KassleBoardFactory extends UniversalBoardFactory {

    public KassleBoardFactory()
            throws NoSuchMethodException {
        super(KassleBoard.class, "Kassle", "https://web.archive.org/web/20181019204225/http://homepages.di.fc.ul.pt/~jpn/gv/kassle.htm");
        params.removeIf((p) -> BOARD_SIZE.equals(p.getName()));
        params.add(new IntegerParameter(BOARD_SIZE, 5, 3, 30));
    }
}
