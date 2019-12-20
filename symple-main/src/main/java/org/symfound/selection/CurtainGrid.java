/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.selection;

import java.awt.Point;
import org.symfound.selection.controls.SelectorButton;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import org.symfound.controls.AppableControl;
import org.symfound.controls.user.FillableGrid.FillDirection;
import org.symfound.tools.iteration.ParallelList;
import org.symfound.controls.user.ButtonGrid;
import static org.symfound.controls.user.CommonGrid.DEFAULT_GRID_GAP;
import org.symfound.controls.user.ScreenStatus;
import org.symfound.device.Device;
import org.symfound.device.emulation.EmulationManager;
import org.symfound.device.emulation.EmulationRequest;
import org.symfound.device.hardware.Hardware;
import org.symfound.device.hardware.characteristic.Movability;
import org.symfound.main.HomeController;
import org.symfound.main.Main;
import org.symfound.selection.controls.ScrollControl;
import org.symfound.selection.controls.ScrollControlButton;
import static org.symfound.selection.controls.ScrollControlButton.KEY;
import org.symfound.selection.modes.Scroller;
import org.symfound.tools.timing.DelayedEvent;

/**
 *
 * @author Javed Gangjee <javed@speakyourmindfoundation.org>
 */
public class CurtainGrid extends ButtonGrid {

    Selector selector;

    public CurtainGrid(Selector selector) {
        super();
        this.selector = selector;

    }

    /**
     *
     * @param buildOrder
     * @param method
     * @param direction
     * @param size
     */
    @Override

    public void build(ParallelList<String, String> buildOrder,
            FillMethod method, FillDirection direction, Double size) {
        setStatus(ScreenStatus.ENDING);
        clear();
        setStatus(ScreenStatus.READY);
        int rowSize = getRowSize(size);
        setSpecRows(1);
        int columnSize = getColumnSize(size);
        setSpecColumns(3);
        setStatus(ScreenStatus.REQUESTED);
        fill(buildOrder, rowSize * columnSize);
        setStatus(ScreenStatus.LOADING);
        build();
        configure(getControlsQueue(), method, direction);

        Insets insets = new Insets(DEFAULT_GRID_GAP);
        setPadding(insets);
        /*        if (!AppGrid.inEditMode()) {
        launchAnimation();
        }*/
        toBack();

    }

    @Override
    public void fill(ParallelList<String, String> buildOrder, Integer size) {
        List<AppableControl> requested = buildRequestedList(buildOrder);
        resetControlsQueue();
        if (requested.size() > 0) {
            getControlsQueue().addAll(requested);
            System.out.println(getControlsQueue().get(0).getKey());
        } else {
            LOGGER.warn("No controls available!");
        }
    }

    public List<AppableControl> buildRequestedList(ParallelList<String, String> buildOrder) {
        List<AppableControl> requested = new ArrayList<>();
        for (int i = 0; i < buildOrder.getFirstList().size(); i++) {
            String toBuild = buildOrder.getFirstList().get(i);
            String index = buildOrder.getSecondList().get(i);
            switch (toBuild.trim()) {
                case SelectorButton.KEY:
                    SelectorButton selectorButton = new SelectorButton(selector);
                    requested.add(selectorButton);
                    break;
                case ScrollControlButton.KEY:
                    ScrollControlButton scrollButton = new ScrollControlButton((Scroller) selector,
                            HomeController.getScrollPane(), ScrollControl.valueOf(index.toUpperCase()));
                    requested.add(scrollButton);
                    break;
                case "Scroll Selector":
                    AppableControl scrollSelectorButton = new AppableControl("", "Scroll Selector", "", index) {
                        @Override
                        public void run() {
                            selector.stop();
                            Device current = getSession().getDeviceManager().getCurrent();
                            EmulationManager em = current.getProcessor().getEmulationManager();
                            final EmulationRequest emulationRequest = new EmulationRequest();
                            emulationRequest.setPosition(new Point(0, 0));
                            final Point nav = new Point((int) (getSession().getDisplay().getScreenWidth() / 2), (int) (getSession().getDisplay().getScreenHeight() / 2));
                            em.getMouse().getAutomator().navigate(nav);

                            //LOGGER.info("Key Pressed: " + event.getCode().getName() + "Toggling Mouse Control");
                            Hardware currentHardware = Main.getSession().getDeviceManager().getCurrent().getHardware();
                            Movability movability = currentHardware.getMovability();
                            movability.setEnabled(Boolean.FALSE);
                            LOGGER.info("Mouse Control is now " + movability.isEnabled());

                            /*  try {
                          em.getMouse().getAutomator().runLeftClick(nav);
                          } catch (AWTException ex) {
                          LOGGER.fatal(ex);
                          }
                             */
                            DelayedEvent postSelectionHold = new DelayedEvent();
                            postSelectionHold.setup(getUser().getTiming().getDwellTime() + 1.0, (ActionEvent e) -> {
                                selector.startStop();
                                movability.setEnabled(Boolean.TRUE);
                            });
                            postSelectionHold.play();
                        }

                        @Override
                        public void click() {

                        }

                        @Override
                        public Preferences getPreferences() {
                            if (preferences == null) {
                                String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
                                Class<? extends AppableControl> aClass = this.getClass();
                                preferences = Preferences.userNodeForPackage(aClass).node(name);
                            }
                            return preferences;
                        }

                    };
                    requested.add(scrollSelectorButton);
                    break;

            }
        }

        return requested;
    }

}
