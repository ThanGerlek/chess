package client.ui;

import client.ui.element.BoardElement;

public interface BoardUIElementReader {

    BoardElement get(int row, int col);
}
