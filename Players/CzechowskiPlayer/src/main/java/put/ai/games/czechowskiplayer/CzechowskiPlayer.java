/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *
 */
package put.ai.games.czechowskiplayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;

public class CzechowskiPlayer extends Player {

    @Override
    public String getName() {
        return "Mateusz Czechowski 155921";
    }

    @Override
    public Move nextMove(Board b) {
        long endTime = System.currentTimeMillis() + getTime();

        List<Move> moves = b.getMovesFor(getColor());
        if(moves.isEmpty())
            return null;

        Move bestOption = null;
        int bestScore = Integer.MIN_VALUE;

        for(Move move : moves){
            Board newBoard = b.clone();
            newBoard.doMove(move);

            int check = minmax(newBoard, 4, Integer.MIN_VALUE, Integer.MAX_VALUE, "MIN", endTime);
            if(check > bestScore){
                bestScore = check;
                bestOption = move;
            }

            if (System.currentTimeMillis() > endTime){
                break;
            }
        }

        if(bestOption == null){
            bestOption = moves.get(0);
        }
        return bestOption;
    }

    private int minmax(Board b, int depth, int pMax, int pMin, String Player, Long endTime){
        if(System.currentTimeMillis() > endTime){
            return 0;
        }

        if(isTerminalNode(b)){
            return wins(b);
        }

        if (depth == 0 ) {
            return evaluate(b, getColor());
        }

        if(Player.equals("MAX")){
            List<Move> moves = b.getMovesFor(getColor());
            int bestMax = Integer.MIN_VALUE;
            for(Move move: moves){
                Board newBoard = b.clone();
                newBoard.doMove(move);
                int val = minmax(newBoard, depth-1, pMax, pMin, "MIN", endTime);
                bestMax = Math.max(bestMax, val);
                pMax = Math.max(pMax, val);
                if( pMax >= pMin){ break; }
            }

            return bestMax;
        }else{
            List<Move> moves = b.getMovesFor(getOpponent(getColor()));
            int bestMin = Integer.MAX_VALUE;
            for(Move move: moves){
                Board newBoard = b.clone();
                newBoard.doMove(move);
                int val = minmax(newBoard, depth-1, pMax, pMin, "MAX", endTime);
                bestMin = Math.min(bestMin, val);
                pMin = Math.min(pMin, val);
                if( pMax >= pMin){ break; }
            }

            return bestMin;
        }
    }

    private boolean isTerminalNode(Board b){
        if(b.getWinner(getColor()) != null){
            return true;
        }
        return false;
    }

    private int wins(Board b){
        if(b.getWinner(getColor())  == getColor()){
            return 100000;
        }else if(b.getWinner(getOpponent(getColor())) == getColor()){
            return -100000;
        }else{
            return 0;
        }
    }

    private int evaluate(Board b, Color color) {
        int size = b.getSize();
        int value = 0;

        for (int x = 0; x < size; x++) {
            int rowOpponent = 0, colOpponent = 0;
            int rowSelf = 0, colSelf = 0;

            for (int y = 0; y < size; y++) {
                // wiersz
                if (b.getState(x, y) == color) {
                    rowSelf++;
                } else if (b.getState(x, y) == getOpponent(color)) {
                    rowOpponent++;
                }

                //  kolumna
                if (b.getState(y, x) == color) {
                    colSelf++;
                } else if (b.getState(y, x) == getOpponent(color)) {
                    colOpponent++;
                }
            }

            value -= rowOpponent * rowOpponent;
            value -= colOpponent * colOpponent;

            value += rowSelf * rowSelf;
            value += colSelf * colSelf;
        }

        return value;
    }
}


