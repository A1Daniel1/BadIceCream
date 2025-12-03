package presentation;

import java.awt.CardLayout;

import javax.swing.JPanel;

import domain.IceCreamFlavour;

/**
 * Panel contenedor que gestiona la navegación entre los diferentes menús.
 * Utiliza un CardLayout para alternar entre las pantallas de inicio, modo,
 * sabor y nivel.
 */
public class MenuPanel extends JPanel {

    private CardLayout layout;
    private MenuInicioPanel inicioPanel;
    private MenuModoPanel modoPanel;
    private MenuSaborPanel saborPanel;
    private MenuNivelPanel nivelPanel;
    private IceCreamFlavour selectedFlavor;

    /**
     * Constructor de la clase MenuPanel.
     * Inicializa los sub-paneles y configura el layout.
     * 
     * @param frame La ventana principal del juego.
     */
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

    /**
     * Muestra una pantalla específica del menú.
     * 
     * @param name El nombre de la pantalla a mostrar ("inicio", "modo", "sabor",
     *             "nivel").
     */
    public void showScreen(String name) {
        layout.show(this, name);
    }

    /**
     * Establece el sabor de helado seleccionado.
     * 
     * @param flavor El sabor elegido.
     */
    public void setSelectedFlavor(IceCreamFlavour flavor) {
        this.selectedFlavor = flavor;
    }

    /**
     * Obtiene el sabor de helado seleccionado actualmente.
     * 
     * @return El sabor elegido.
     */
    public IceCreamFlavour getSelectedFlavor() {
        return selectedFlavor;
    }
}