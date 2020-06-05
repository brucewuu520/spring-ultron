package org.springultron.qrcode.option;

import java.util.Objects;

/**
 * 矩阵
 *
 * @author brucewuu
 * @date 2020/6/4 19:59
 */
public class DotSize {
    public static final DotSize SIZE_1_1 = new DotSize(1, 1);
    public static final DotSize SIZE_1_2 = new DotSize(1, 2);
    public static final DotSize SIZE_2_1 = new DotSize(2, 1);
    public static final DotSize SIZE_2_2 = new DotSize(2, 2);

    public static DotSize create(final int row, final int col) {
        if (row == 1 && col == 1) {
            return SIZE_1_1;
        } else if (row == 1 && col == 2) {
            return SIZE_1_2;
        } else if (row == 2 && col == 1) {
            return SIZE_2_1;
        } else if (row == 2 && col == 2) {
            return SIZE_2_2;
        } else {
            return new DotSize(row, col);
        }
    }

    public int size() {
        return row * col;
    }

    private final int row;
    private final int col;

    public DotSize(final int row, final int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DotSize dotSize = (DotSize) o;
        return row == dotSize.row && col == dotSize.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
