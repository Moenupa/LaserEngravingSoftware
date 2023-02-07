package examples;

import java.util.ArrayList;
import java.util.List;

public class Undo {
    static List<List<BElement>> ty_shuzu_cx = new ArrayList();
    static int dang_qian = 0;
    static int bu_shu = 20;

    public static void tian_jia() {
        if (dang_qian < bu_shu) {
            if (ty_shuzu_cx.size() > dang_qian) {
                for (int i = 0; i < ty_shuzu_cx.size() - dang_qian + 1; ++i) {
                    ty_shuzu_cx.remove(ty_shuzu_cx.size() - 1);
                }
            }

            ty_shuzu_cx.add(BElement.copyResult(Board.bElements));
            dang_qian = ty_shuzu_cx.size();
        } else {
            ty_shuzu_cx.remove(0);
            ty_shuzu_cx.add(BElement.copyResult(Board.bElements));
            dang_qian = ty_shuzu_cx.size();
        }

    }

    public static void che_xiao() {
        if (dang_qian > 1) {
            Board.bElements = BElement.copyResult((List) ty_shuzu_cx.get(dang_qian - 1 - 1));
            --dang_qian;
        }

    }

    public static void chong_zuo() {
        if (dang_qian < bu_shu) {
            Board.bElements = BElement.copyResult((List) ty_shuzu_cx.get(dang_qian + 1 - 1));
            ++dang_qian;
        }

    }
}
