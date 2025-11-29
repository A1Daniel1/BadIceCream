package presentation;

import java.awt.CardLayout;

import javax.swing.JPanel;

import domain.IceCreamFlavour;

public class MenuPanel extends JPanel {

    private CardLayout layout;
    private MenuInicioPanel inicioPanel;
    private MenuModoPanel modoPanel;
    private MenuSaborPanel saborPanel;
    private MenuNivelPanel nivelPanel;
    private IceCreamFlavour selectedFlavor;

    public MenuPanel(GameFrame frame) {
        layout = new CardLayout();
        setLayout(layout);
        selectedFlavor = IceCreamFlavour.VANILLA;

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
    
    public void setSelectedFlavor(IceCreamFlavour flavor) {
        this.selectedFlavor = flavor;
    }
    
    public IceCreamFlavour getSelectedFlavor() {
        return selectedFlavor;
    }
}