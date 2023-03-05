package frc.robot.controllers;

public class PlasmaGuitar {
    
    public PlasmaButton GREEN;
    public PlasmaButton RED;
    public PlasmaButton YELLOW;
    public PlasmaButton BLUE;   
    public PlasmaButton ORANGE;
    public PlasmaButton START;
    public PlasmaButton BACK;

    public PlasmaDPad DPAD;
    public PlasmaDPad STRUMBAR;

    public PlasmaAxis WAMMY;
    public PlasmaAxis TILT;

    public PlasmaGuitar(int port) {

        GREEN = new PlasmaButton(GuitarConstants.GREEN_ID, port);
        RED = new PlasmaButton(GuitarConstants.RED_ID, port);
        YELLOW = new PlasmaButton(GuitarConstants.YELLOW_ID, port);
        BLUE = new PlasmaButton(GuitarConstants.BLUE_ID, port);
        ORANGE = new PlasmaButton(GuitarConstants.ORANGE_ID, port);
        START = new PlasmaButton(GuitarConstants.START_ID, port);
        BACK = new PlasmaButton(GuitarConstants.BACK_ID, port);

        DPAD = new PlasmaDPad(GuitarConstants.DPAD_ID, port);
        STRUMBAR = new PlasmaDPad(GuitarConstants.STUMBAR_ID, port);

        WAMMY = new PlasmaAxis(GuitarConstants.WAMMY_ID, port, true);
        TILT = new PlasmaAxis(GuitarConstants.TILT_ID, port);

    }
}
