package put.ai.games.kassle;

import java.util.ArrayList;
import java.util.List;
import put.ai.games.game.Move;
import put.ai.games.game.Player;
import put.ai.games.game.Player.Color;
import put.ai.games.game.TypicalBoard;

public class KassleBoard extends TypicalBoard {

    /*package*/ boolean lastMoveWasShift = false;
    private Color last = Color.EMPTY;


    public KassleBoard(int boardSize) {
        super(boardSize);
    }


    protected KassleBoard(KassleBoard other) {
        super(other);
        this.lastMoveWasShift = other.lastMoveWasShift;
    }


    @Override
    public KassleBoard clone() {
        return new KassleBoard(this);
    }


    @Override
    public boolean equals(Object other) {
        if (!(other instanceof KassleBoard)) {
            return false;
        }
        if (!super.equals(other)) {
            return false;
        }
        return lastMoveWasShift == ((KassleBoard) other).lastMoveWasShift;
    }


    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 37 * hash + (this.lastMoveWasShift ? 1 : 0);
        return hash;
    }


    private boolean hasFull(Color c) {
        for (int i = 0; i < getSize(); ++i) {
            boolean hasRow = true;
            boolean hasCol = true;
            for (int j = 0; j < getSize(); ++j) {
                hasRow &= (state[i][j] == c);
                hasCol &= (state[j][i] == c);
            }
            if (hasRow || hasCol) {
                return true;
            }
        }
        return false;
    }


    @Override
    public Color getWinner(Color currentPlayer) {
        boolean wins1 = hasFull(Color.PLAYER1);
        boolean wins2 = hasFull(Color.PLAYER2);
        if (!wins1 && !wins2) {
            if (canMove()) {
                return null;
            } else {
                return Color.EMPTY;
            }
        }
        if (wins1 && !wins2) {
            return Color.PLAYER1;
        }
        if (!wins1 && wins2) {
            return Color.PLAYER2;
        }
        assert wins1 && wins2;
        return last;
    }


    private boolean canMove() {
        for (int i = 0; i < getSize(); ++i) {
            for (int j = 0; j < getSize(); ++j) {
                if (state[i][j] == Color.EMPTY) {
                    return true;
                }
            }
        }
        return !lastMoveWasShift;
    }


    @Override
    protected boolean canMove(Color color) {
        return canMove();
    }


    private int magic(int x) {
        if (x >= 0) {
            return x % state.length;
        } else {
            return (state.length + x) % state.length;
        }
    }


    private void shift(int bx, int by, int dx, int dy) {
        Color tmp = state[bx][by];
        for (int i = 0; i < state.length - 1; ++i) {
            int nx = magic(bx - dx);
            int ny = magic(by - dy);
            state[bx][by] = state[nx][ny];
            bx = nx;
            by = ny;
        }
        state[bx][by] = tmp;
    }


    @Override
    public void doMove(Move move) {
        if (move instanceof KassleDropMove) {
            KassleDropMove m = (KassleDropMove) move;
            if (state[m.getX()][m.getY()] != Color.EMPTY) {
                throw new IllegalArgumentException();
            }
            state[m.getX()][m.getY()] = move.getColor();
            m.setPreviousMoveShift(lastMoveWasShift);
            lastMoveWasShift = false;
            last = move.getColor();
        } else if (!lastMoveWasShift && move instanceof KassleShiftMove) {
            KassleShiftMove m = (KassleShiftMove) move;
            switch (m.getDirection()) {
                case DOWN:
                    shift(m.getX(), 0, 0, 1);
                    break;
                case LEFT:
                    shift(0, m.getX(), -1, 0);
                    break;
                case RIGHT:
                    shift(0, m.getX(), 1, 0);
                    break;
                case UP:
                    shift(m.getX(), 0, 0, -1);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            lastMoveWasShift = true;
            last = move.getColor();
        } else {
            throw new IllegalArgumentException();
        }
    }


    @Override
    public List<Move> getMovesFor(Color color) {
        List<Move> result = new ArrayList<>();
        for (int x = 0; x < getSize(); ++x) {
            for (int y = 0; y < getSize(); ++y) {
                if (state[x][y] == Color.EMPTY) {
                    result.add(new KassleDropMove(x, y, color));
                }
            }
        }
        if (!lastMoveWasShift) {
            for (int x = 0; x < getSize(); ++x) {
                result.add(new KassleShiftMove(x, KassleShiftMove.Direction.LEFT, color));
                result.add(new KassleShiftMove(x, KassleShiftMove.Direction.RIGHT, color));
                result.add(new KassleShiftMove(x, KassleShiftMove.Direction.UP, color));
                result.add(new KassleShiftMove(x, KassleShiftMove.Direction.DOWN, color));
            }
        }
        return result;
    }


    @Override
    public void undoMove(Move move) {
        if (move instanceof KassleDropMove) {
            KassleDropMove m = (KassleDropMove) move;
            if (state[m.getX()][m.getY()] != m.getColor()) {
                throw new IllegalArgumentException();
            }
            state[m.getX()][m.getY()] = Color.EMPTY;
            lastMoveWasShift = m.isPreviousMoveShift();
            last = Player.getOpponent(last);
        } else if (lastMoveWasShift && move instanceof KassleShiftMove) {
            KassleShiftMove m = (KassleShiftMove) move;
            switch (m.getDirection()) {
                case DOWN:
                    shift(m.getX(), 0, 0, -1);
                    break;
                case LEFT:
                    shift(0, m.getX(), 1, 0);
                    break;
                case RIGHT:
                    shift(0, m.getX(), -1, 0);
                    break;
                case UP:
                    shift(m.getX(), 0, 0, 1);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            lastMoveWasShift = false;
            last = Player.getOpponent(last);
        } else {
            throw new IllegalArgumentException();
        }
    }
}
