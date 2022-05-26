package cz.cvut.fel.pjv.model.game;

import cz.cvut.fel.pjv.controller.ChessController;
import cz.cvut.fel.pjv.view.ChessGUI;

import javax.swing.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.logging.Logger;

/**
 * Own chess clock / timer.
 * Runs until end is changed to false.
 */
public class MyTimer extends Thread {
    private ChessController controller;
    private ChessGUI view;

    private Double whiteSideTime;
    private Double blackSideTime;
    private double lastChange = 0;
    private boolean isBlackSide = false;
    private boolean firstMoveDone = false;
    public boolean end = false;

    private static final Logger LOGGER = Logger.getLogger(ChessController.class.getName());

    /**
     * Body of thread that is constantly running until end is set to false.
     * Ensures that chess clock is running and that they are displayed in view.
     */
    public void run()
    {
        lastChange = System.currentTimeMillis();
        LOGGER.info("Thread started running");
        while(!end)
        {
            try{
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            double currTime = System.currentTimeMillis();
            if(firstMoveDone)
            {
                if(isBlackSide)
                {
                    blackSideTime -= currTime - lastChange;
                    if(blackSideTime <= 0)
                    {
                        LOGGER.info("Black player's time is up");
                        controller.timeIsOut(false);
                        end = true;
                    }
                }
                else
                {
                    whiteSideTime -= currTime - lastChange;
                    if(whiteSideTime <= 0)
                    {
                        LOGGER.info("White player's time is up");
                        controller.timeIsOut(true);
                        end = true;
                    }
                }
            }
            SwingUtilities.invokeLater(() -> {
                view.setWhitePlayerTime(convertToString(whiteSideTime));
                view.setBlackPlayerTime(convertToString(blackSideTime));
            });

            lastChange = System.currentTimeMillis();
        }

    }

    public MyTimer(Double initTimeWhite, Double initTimeBlack, ChessController controller, ChessGUI view)
    {
        this.whiteSideTime = initTimeWhite * 1000;
        this.blackSideTime = initTimeBlack * 1000;
        this.controller = controller;
        this.view = view;
    }

    public void changeSide()
    {
        changeSide(true);
    }

    /**
     * Starts counting and provides switching between players
     * @param started
     */
    public void changeSide(boolean started)
    {
        if(!firstMoveDone && started)
        {
            firstMoveDone = true;
        }

        isBlackSide =! isBlackSide;
    }

    public Double getWhiteSideTime()
    {
        return this.whiteSideTime;
    }

    public void setWhiteSideTime(Double whiteSideTime)
    {
        this.whiteSideTime = whiteSideTime;
    }

    public Double getBlackSideTime()
    {
        return this.blackSideTime;
    }

    public void setBlackSideTime(Double blackSideTime)
    {
        this.blackSideTime = blackSideTime;
    }

    /**
     * Converts time to string
     * @param time
     * @return
     */
    private String convertToString(Double time) {
        if (time <= 0) {
            return "0:00.0";
        }

        time /= 1000;
        Double secPart = time%60;
        NumberFormat secondsFormatter = new DecimalFormat("00.0");
        return (int) (time / 60) + ":" + secondsFormatter.format(secPart);
    }
}
