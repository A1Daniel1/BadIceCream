package presentation;

import java.awt.CardLayout;
import javax.swing.JPanel;
import domain.*;

/**
 * Panel contenedor que gestiona la navegación entre los diferentes menús.
 * Ahora incluye soporte para modalidades de juego y perfiles de IA.
 */
public class MenuPanel extends JPanel {

    private CardLayout layout;
    private MenuInicioPanel inicioPanel;
    private MenuModoPanel modoPanel;
    private MenuSaborPanel saborPanel;
    private MenuNivelPanel nivelPanel;
    
    private IceCreamFlavour selectedFlavor;
    private GameMode gameMode;
    private AIProfile aiProfile1;
    private AIProfile aiProfile2;

    public MenuPanel(GameFrame frame) {
        layout = new CardLayout();
        setLayout(layout);
        
        // Valores por defecto
        selectedFlavor = IceCreamFlavour.VANILLA;
        gameMode = GameMode.PLAYER;
        aiProfile1 = AIProfile.EXPERT;
        aiProfile2 = AIProfile.HUNGRY;

        inicioPanel = new MenuInicioPanel(frame, this);
        modoPanel = new MenuModoPanel(frame, this);
        saborPanel = new MenuSaborPanel(frame, this);
        nivelPanel = new MenuNivelPanel(frame, this);

        add(inicioPanel, "inicio");
        add(modoPanel, "modo");
        add(saborPanel, "sabor");
        add(nivelPanel, "nivel");

        layout.show(this, "inicio");
    }

    public void showScreen(String name) {
        layout.show(this, name);
    }

    // Getters y Setters
    public IceCreamFlavour getSelectedFlavor() {
        return selectedFlavor;
    }

    public void setSelectedFlavor(IceCreamFlavour flavor) {
        this.selectedFlavor = flavor;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode mode) {
        this.gameMode = mode;
    }

    public AIProfile getAIProfile1() {
        return aiProfile1;
    }

    public void setAIProfile1(AIProfile profile) {
        this.aiProfile1 = profile;
    }

    public AIProfile getAIProfile2() {
        return aiProfile2;
    }

    public void setAIProfile2(AIProfile profile) {
        this.aiProfile2 = profile;
    }
}