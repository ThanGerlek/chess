package client.ui.BoardUIElement;

import client.ui.BoardUIElement.element.BoardElement;

public interface BoardUIElementReader {

    BoardElement get(int row, int col);
}
