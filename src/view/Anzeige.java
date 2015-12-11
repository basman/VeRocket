package view;

import simulation.Simulator;
import simulation.ZeitUndRaum;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Roman on 08.12.2015.
 */
public class Anzeige implements ActionListener {

    private Simulator simulation;
    private Thread simulatorThread = null;

    private JPanel LeftPanel;
    private JPanel RightPanel;
    private JPanel MasterPanel;
    private JSplitPane SplitPane;
    private JLabel lblAltitude;
    private JLabel lblFuel;
    private JLabel lblSpeed;
    private JLabel lblStatus;
    private JButton btnStart;
    private JComboBox cboxController;
    private GraphPanel gPanel;

    public Anzeige() {
        this.initUI();
    }

    public void initUI() {
        gPanel = new GraphPanel();
        LeftPanel.add(gPanel);

        btnStart.addActionListener(this);
        initControlSelection();

        JFrame frame = new JFrame("VeRocket");
        frame.setContentPane(this.MasterPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void initControlSelection() {
        cboxController.addItem("Langlauf");
        cboxController.addItem("Crashtest");
        cboxController.addItem("Icarus");
    }

   /* Programm Startpunkt */
    public static void main(String args[]) {
        Anzeige v = new Anzeige();
    }

    public void redraw() {
        if (this.simulation == null)
            return;

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

    public void setSimulation(Simulator simulation) {
        this.simulation = simulation;
        this.gPanel.setSimulator(simulation);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnStart) {
            if (this.simulatorThread != null && this.simulatorThread.isAlive()) {
                this.simulation.stop();

            } else {
                String controllerClass = (String) cboxController.getSelectedItem();

                this.setSimulation(new Simulator("steuerung." + controllerClass, this));

                Thread me = new Thread(this.simulation);
                this.simulatorThread = me;
                me.start();
            }
        }
    }
}
