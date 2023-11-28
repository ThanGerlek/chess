package client.ui.element;

public record BoardElement(BackgroundElement bgElem, ForegroundElement fgElem) {

    public String toString() {
        return fgElem.toString();
    }

}