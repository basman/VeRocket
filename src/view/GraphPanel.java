package view;

import simulation.ZeitUndRaum;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Roman on 11.12.2015.
 */
public class GraphPanel extends JPanel {
    private ZeitUndRaum simulator;
    private ArrayList<Double> heightHistory = new ArrayList<>();

    public GraphPanel() {
        super();
    }

    private void init() {
        heightHistory = new ArrayList<>();
    }

    public void update() {
        heightHistory.add(simulator.getRakete().getHoehe());
        this.repaint();
    }

    public void setSimulator(ZeitUndRaum simulator) {
        this.simulator = simulator;
        init();
    }

    @Override
    public Dimension getPreferredSize() {
        /* enforce same size as the parent LeftPanel */
        return getParent().getSize();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Dimension dim = getSize();
        double width = dim.getWidth();
        double height = dim.getHeight();

        g.drawLine(20,10, 10,10);
    }
}
