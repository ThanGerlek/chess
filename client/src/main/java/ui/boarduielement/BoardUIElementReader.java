package ui.boarduielement;

import ui.boarduielement.element.BoardElement;

public interface BoardUIElementReader {

    BoardElement get(int row, int col);
}
