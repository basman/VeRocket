package view;

import simulation.ZeitUndRaum;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Roman on 11.12.2015.
 */
public class GraphPanel extends JPanel {
    private ZeitUndRaum simulator;
    private ArrayList<Double> heightHistory;
    private float time;
    private double maxHeight;

    private final int hMargin = 30;
    private final int tMargin = 20;
    private final int bMargin = 40;
    private final float minXtime = 5.0f;
    private final float minYheight = 100f;

    public GraphPanel() {
        super();
        this.setDoubleBuffered(true);
        init();
    }

    private void init() {
        this.time = 0;
        this.maxHeight = 0;
        this.heightHistory = new ArrayList<>();
    }

    public void update() {
        heightHistory.add(simulator.getRakete().getHoehe());
        this.time = simulator.getTime();
        this.maxHeight = maxVal(heightHistory);
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
        int width = (int)dim.getWidth();
        int height = (int)dim.getHeight();

        float xTime = (int)this.time + 1.0f; // maximum x value
        if (xTime < minXtime)
            xTime = minXtime;

        /* draw x-axis */
        g.drawLine(hMargin, height - bMargin, width - hMargin, height - bMargin);

        /* draw x scale */
        int xNumberStep = (int)((xTime+10) / 10);
        for(int i=1; i<=xTime; i++) {
            int x = hMargin + (int)(i * (width - 2*hMargin) / xTime);
            int length = 2;
            if (i % xNumberStep == 0) {
                length = 5;
                g.drawString(String.format("%d", i), x - 4, height - bMargin / 2);
            }

            g.drawLine(x, height - bMargin, x, height - bMargin - length);
        }

        /* draw x unit */
        g.drawString("[s]", width-hMargin+10, height - bMargin + 4);

        double yHeight = Math.floor((maxHeight + 100) / 100) * 100;
        if (yHeight < minYheight)
            yHeight = minYheight;

        /* draw y-axis */
        g.drawLine(hMargin, tMargin, hMargin, height - bMargin);

        /* draw y scale */
        int yStep = (int)yHeight / 10;
        for(int i=yStep; i<=yHeight; i+=yStep) {
            int y = (int)(height - bMargin - i / yHeight * (height - tMargin - bMargin));
            g.drawLine(hMargin, y, hMargin+5, y);
            if (i/1000f >= 100)
                g.drawString(String.format(Locale.US, "%.1f", i/1000f), hMargin/7, y+4);
            else
                g.drawString(String.format(Locale.US, "%.2f", i/1000f), hMargin/7, y+4);
        }

        /* draw y unit */
        int y = tMargin - 10;
        g.drawString("[km]", hMargin/5, y);


        /* draw graph */
        if (heightHistory.size() > 0) {
            int skipInterval = 0;
            float xStep;
            if (width - 2*hMargin >= heightHistory.size()) {
                xStep = time / xTime * (width - 2*hMargin) / heightHistory.size();
            } else {
                // more samples than pixels: drop some samples
                xStep = 1f;
                int overflow = (int)(heightHistory.size() - time / xTime * (width - 2*hMargin));
                skipInterval = heightHistory.size() / overflow;
            }
            int iSample = 0;
            float xc = hMargin;
            for (Double h : heightHistory) {
                if (skipInterval > 0 && iSample++ % skipInterval == 0) // drop every overrun'th sample
                    continue;
                y = (int) (height - bMargin - h / yHeight * (height - tMargin - bMargin));
                g.drawOval((int)xc, y, 2, 2);
                xc += xStep;
            }
        }
    }

    private double maxVal(ArrayList<Double> list) {
        double max = 0;
        for(double v: list) {
            if (max < v)
                max = v;
        }
        return max;
    }
}
