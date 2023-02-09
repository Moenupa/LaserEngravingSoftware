package engraver;

import engraver.model.BElement;
import engraver.model.Board;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Undo {
    static List<List<BElement>> history = new ArrayList<>();
    static int cur = 0;
    static int steps = 20;

    public static void add() {
        if (cur < steps) {
            if (history.size() > cur) {
                IntStream.range(0, history.size() - cur + 1)
                        .forEach(i -> history.remove(history.size() - 1));
            }
        } else {
            history.remove(0);
        }
        history.add(BElement.copy(Board.bElements));
        cur = history.size();
    }

    public static void undo() {
        if (cur > 1) {
            --cur;
            Board.bElements = BElement.copy(history.get(cur - 1));
        }
    }

    public static void redo() {
        if (cur < steps) {
            ++cur;
            Board.bElements = BElement.copy(history.get(cur - 1));
        }
    }
}
