package view;

import mission.MissionElement;
import util.Util;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Roman on 13.12.2015.
 */
public class MissionElementRenderer implements ListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        MissionElement me = (MissionElement) value;
        JLabel lbl = new JLabel();
        lbl.setText(me.toString());

        if(me.isCompleted())
            lbl.setIcon(Util.getIcon("ok"));
        else
            lbl.setIcon(Util.getIcon("missing"));

        return lbl;
    }
}
