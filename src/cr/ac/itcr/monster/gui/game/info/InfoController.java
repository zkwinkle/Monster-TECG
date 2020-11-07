package cr.ac.itcr.monster.gui.game.info;

import cr.ac.itcr.monster.communication.Host;
import cr.ac.itcr.monster.game.cards.Card;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class InfoController {
    @FXML
    private Label nameLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label manaLabel;
    @FXML
    private Label typeLabel;
    @FXML
    private Button cancelButton;

    @FXML
    private void cancelButtonAction(ActionEvent actionEvent){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void setup(Card card) {
        nameLabel.setText(card.getNombre());
        typeLabel.setText(card.getType());
        manaLabel.setText(String.valueOf(card.getCoste()));

        if (card.getType().equals("Esbirro")) {
            descriptionLabel.setText(card.getNombre()+" con " + card.getAtaque() + " de ataque y "+ card.getVida() +" de vida." );
        } else {
            descriptionLabel.setText(card.getDescripcion());
        }

    }
}