# Rocket controller simulation

Use one of the controllers provided or write your own and test it visually with this simple rocket simulation.
In this simple simulator the rocket can go only in one direction: up and down. Air resistance is ignored.

You can use this project to train your controller programming skills or to carry out a contest among different
controller implementations whereas the softest landing and the most remaining fuel win.

## How to implement your own controller:

1. Extend the abstract class AbstractSteuerung and put it into the package steuerung.
2. Add the name of your class to the ItemList in view.Anzeige.initControlSelection() so it becomes available
   in the GUI to be used for simulation runs.
3. Fill in the timeTick() method of your controller to return burn rates for the rocket drive. Use the getters
   provided by the inherited private members sim and rakete to read what's happening during the simulation.
   These private members implement the interfaces ZeitUndRaum and Rakete.
   
## How to run a simulation:

1. Run the application by launching Anzeige.main().
2. Choose the controller in the combobox at the bottom left corner of the GUI.
3. Hit the start button.

## Good to know:

- A successful rocket landing requires it to touch the ground with less than 0.5m/s descending rate.
- As to be expected with a rocket, its weight will reduce proportionally as more fuel is used for propulsion.