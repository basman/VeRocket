package view;

import simulation.ZeitUndRaum;

import javax.swing.*;

/**
 * Created by Roman on 08.12.2015.
 */
public class Anzeige {

    private ZeitUndRaum simulation;
    private JPanel LeftPanel;
    private JPanel RightPanel;
    private JPanel MasterPanel;
    private JSplitPane SplitPane;
    private JLabel lblAltitude;
    private JLabel lblFuel;
    private JLabel lblSpeed;
    private JLabel lblStatus;
    private GraphPanel gPanel;

    public Anzeige(ZeitUndRaum simulation) {
        this.simulation = simulation;
        this.init();
    }

    public void init() {
        gPanel = new GraphPanel(simulation);
        LeftPanel.add(gPanel);

        JFrame frame = new JFrame("VeRocket");
        frame.setContentPane(this.MasterPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void redraw() {
        // carry out the redraw
        lblFuel.setText(String.format("%.2fl %.1f%%", simulation.getRakete().getBrennstoffVorrat(),
                simulation.getRakete().getBrennstoffVorrat() * 100 / simulation.getRakete().getTankvolumen()));
        lblAltitude.setText(String.format("%.2fm", simulation.getRakete().getHoehe()));
        lblSpeed.setText(String.format("%.2fm/s", simulation.getRakete().getGeschwindigkeit()));

        gPanel.update();
    }

    public void setStatus(String s) {
        lblStatus.setText(s);
    }
}
