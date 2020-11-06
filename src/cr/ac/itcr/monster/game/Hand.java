package cr.ac.itcr.monster.game;

public class Hand { //hand is indexed starting at 0
    private Card head;
    private int size;

    public Hand() {
        this.head = null;
        this.size = 0;
    }

    public int getSize() {
        return size;
    }

    public void addCard(Object card) {
        Card newCard = new Card(card);

        if (this.size == 10) { //max 10 cards in hand
            return;
        } else if (this.size > 10) {
            System.out.println("Error: Maximum hand capacity surpassed"); //esto es solo para que nos demos cuenta si hicimos algo mal
            return;
        }

        if (this.head == null) {
            this.head = newCard;
            return;
        }

        Card current = this.head;
        while (current.getNext() != null) {
            current = current.getNext();
        }

        newCard.setPrevious(current);
        current.setNext(newCard);
        this.size++;
    }

    public Object removeCard(int position) {
        Card current = this.head;

        if (current == null) {
            System.out.println("Error: removeCard called on empty hand"); //for possible bugfixing purposes
        } else if (position==1) { //in case it's the fist one
            this.head = current.getNext();
            this.head.setPrevious(null);
            return current.getInfo();
        }

        for (int i = 1; i < position; i++) { //hand is indexed starting at 1
            current = current.getNext();
        }

        if (position == this.size) { //in case it's the last one
            current.getPrevious().setNext(null);
            return current.getInfo();
        }

        //if it's a card in the middle
        current.getPrevious().setNext(current.getNext());
        current.getNext().setPrevious(current.getPrevious());
        return current.getInfo();
    }


}