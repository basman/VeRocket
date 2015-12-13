package view;

import mission.Mission;
import mission.MissionElement;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import simulation.Simulator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Roman on 08.12.2015.
 */
public class Anzeige implements ActionListener {

    private Simulator simulation;
    private Thread simulatorThread = null;
    private Mission selectedMission;
    private HashMap<String, Mission> availableMissions = new HashMap<>();

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
    private JButton btnPause;
    private JComboBox cboxMission;
    private JList lstMissionElementList;
    private GraphPanel gPanel;

    public Anzeige() {
        this.initMissions();
        this.initUI();
    }

    public void initUI() {
        gPanel = new GraphPanel();
        LeftPanel.add(gPanel);

        btnStart.addActionListener(this);
        btnPause.addActionListener(this);
        cboxMission.addActionListener(this);
        initControlSelection();
        updateMissionElements();

        lstMissionElementList.setCellRenderer(new MissionElementRenderer());

        JFrame frame = new JFrame("VeRocket");
        frame.setContentPane(this.MasterPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void updateMissionElements() {
        String missionName = (String)cboxMission.getSelectedItem();
        this.selectedMission = availableMissions.get(missionName);
        lstMissionElementList.removeAll();

        if(this.selectedMission != null) {
            lstMissionElementList.setListData(this.selectedMission.getElements().toArray());
        }

    }

    private void initMissions() {
        // read missions from JSON file
        try {
            FileReader fr = new FileReader("missions.json");

            // JSON parser found here: https://code.google.com/p/json-simple
            JSONParser jsonParser = new JSONParser();
            JSONArray missionArray = (JSONArray) jsonParser.parse(fr);

            for(Object m: missionArray) {
                JSONObject mission = (JSONObject) m;

                JSONArray missionElements = (JSONArray)mission.get("elements");
                String name = (String)mission.get("name");

                Mission missionObj = new Mission(name, missionElements);
                availableMissions.put(name, missionObj);
                cboxMission.addItem(missionObj.getName());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: missions.json", e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    public void simulationCompleted() {
        btnStart.setText("Start");
        this.redraw();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnStart) {
            if (this.simulatorThread != null && this.simulatorThread.isAlive()) {
                this.simulation.stop();

            } else {
                String controllerClass = (String) cboxController.getSelectedItem();

                this.setupSimulation("steuerung." + controllerClass);

                btnStart.setText("Stop");
            }

        } else if(e.getSource() == btnPause) {
            this.simulation.pause();

        } else if(e.getSource() == cboxMission) {
            if(e.getActionCommand().equals("comboBoxChanged"))
                updateMissionElements();
        }
    }

    private void setupSimulation(String simulatorClassName) {
        this.setSimulation(new Simulator(simulatorClassName, this));
        this.selectedMission.setSimulation(this.simulation);

        Thread me = new Thread(this.simulation);
        this.simulatorThread = me;
        me.start();
    }

    /* called back by simulator if a single mission element _has been_ completed or if entire mission _is_ completed */
    public void missionElementCompleted() {
        this.lstMissionElementList.repaint();
        redraw();
    }

    public Mission getMission() {
        return selectedMission;
    }
}
