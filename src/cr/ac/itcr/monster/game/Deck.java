package cr.ac.itcr.monster.game;

import cr.ac.itcr.monster.game.cards.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * cñase que crea el deck como un Array de Cartas
 */
public class Deck {
    private int top = 0;
    private Card[] deck = new Card[20];
    private ArrayList<Card> discardPile = new ArrayList<Card>();

    /**
     * Builder del Deck
     */
    public Deck() {
        refill();
    }

    /**
     * Añadir una carta al tope de la pila
     * @param carta
     */
    //Añadir al tope de la pila
    public void push(Card carta){
        this.deck[top]=carta;
        top++;
    }

    /**
     * Sacar la carta que está en el tope de la pila
     * @return Carta del tope
     */
    //Sacar la carta que esté arriba
    public Card draw(){
        if (top > 0) {
            return this.deck[--top];
        }
        return null;
    }

    /**
     * Ver que carta está en el tope de pila
     * @return Carta en el tope
     */
    //Ver la carta que esté arriba
    public Card peek(){
        return this.deck[top-1];
    }

    /**
     * llena el deck a su máximo de cartas aleatorias bien distribuidas entre Hechizos, Esbirros y Secretos
     */
    //Refillear con cartas aleatorias (Sería la llamada que hace en el app)
    private void refill(){
        Random random = new Random(); //instance of random class
            for (int i = 0; i < 7; i++) {
                Card hechizo = (Card.getHechizos()[random.nextInt(10)]);
                if (!(hechizo.getNombre().equals("Poder Supremo") || hechizo.getNombre().equals("Conversión") || hechizo.getNombre().equals("Destrucción") || hechizo.getNombre().equals("Bola de Fuego")|| hechizo.getNombre().equals("Robar Carta")|| hechizo.getNombre().equals("Recuperar cartas")|| hechizo.getNombre().equals("Asesinar"))) {
                    push(hechizo);
                } else {
                    i--;}

            }
            for (int i = 0; i < 13; i++) {
                push(Card.getEsbirros()[random.nextInt(20)]);
            }
            for (int i = 0; i < 0; i++) {
                push(Card.getSecretos()[random.nextInt(10)]);
            }

            for (int i = 0; i < 20; i++) { //shuffling of the deck
                int randomIndexToSwap = random.nextInt(20);
                Card temp = deck[randomIndexToSwap];
                deck[randomIndexToSwap] = deck[i];
                deck[i] = temp;
        }
    }

    /**
     * Settea el deck a 0
     */
    //Resetear el deck a 0
    public void Reset(){
        Arrays.fill(this.deck,null);
    }

    /**
     * El array que contiene las cartas ya jugadas o las cartas muertas
     * @return Array de cartas jugadas
     */
    public ArrayList<Card> getDiscardPile() {
        return discardPile;
    }
}
