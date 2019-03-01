/*
 * Copyright (C) 2015 SpeakYourMind Foundation
 * Visit us at http://www.speakyourmindfoundation.org/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.symfound.main.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.log4j.Logger;
import org.symfound.builder.Buildable;
import org.symfound.builder.Builder;
import org.symfound.builder.user.Usable;
import org.symfound.builder.user.User;
import org.symfound.main.ResourceLister;

/**
 *
 * @author Javed Gangjee
 */
public class App implements Buildable, Usable {

    private static final String NAME = App.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String NONE = "None";
    private final String initValue;
    private StringProperty value;

    /**
     *
     */
    public List<String> buildList;
    private List<StandaloneUI> uiList;
    private final List<String> stackList = new ArrayList<>();
    private StackedUI stackedUI;
    private final User user;

    /**
     *
     * @param user
     * @param value
     */
    public App(User user, String value) {
        this.user = user;
        this.initValue = value;
    }

    /**
     *
     * @return
     */
    @Override
    public User getUser() {
        return user;
    }

    /**
     *
     * @param builder
     */
    @Override
    public void build(Builder builder) {
        String appName = getValue().toLowerCase().replaceAll(" ", "");
        String fxmlFolder = "/fxml/app/" + appName + "/";
        ResourceLister resourceList = new ResourceLister(fxmlFolder);
        List<String> fxmlList = resourceList.getFileNames();
        buildList = createDisplayOrder(fxmlList);

        LOGGER.info("Building App " + getValue() + " with " + buildList.size() + " screens...");

        for (String UI : buildList) {
            String file = fxmlFolder + UI;
            file = file.replace(".fxml", "");
            StandaloneUI ui = new StandaloneUI(file);
            getUIs().add(ui);
            ui.build(builder);
            ui.builtProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    completionCheck();
                }
            });
        }

        /*  for (String UI:buildList){
         String UIFile = fxmlFolder + UI;
         UIFile = UIFile.replace(".fxml", "");
         stackList.add(UIFile);
         }
         stackedUI = new UI(stackList);
         stackedUI.buildStack(builder);*/
    }

    /**
     * Sort through the provided list of file names with the default UI as the
     * first item and the remaining are fxml files that contain the usernames.
     *
     * @param fileNameList list of files in the users folder
     * @return
     */
    public List<String> createDisplayOrder(List<String> fileNameList) {
        List<String> sortedList = new ArrayList<>();
        fileNameList.stream().forEach((fileName) -> {
            Boolean isFile = fileName.endsWith(".fxml");
            if (isFile) {
                sortedList.add(fileName);
            }
        });

        return sortedList;
    }

    private static void order(List<StandaloneUI> uiUsableList) {
        Collections.sort(uiUsableList, (UI t, UI t1) -> {
            Integer x1 = t.getUIPath().getPriority();
            Integer x2 = t1.getUIPath().getPriority();
            int sComp = x1.compareTo(x2);
            if (sComp != 0) {
                return sComp;
            } else {
                Integer x3 = t.getUIPath().getDifficulty();
                Integer x4 = t1.getUIPath().getDifficulty();
                return x4.compareTo(x3);
            }
        });
    }

    /**
     *
     * @return
     */
    public List<StandaloneUI> getUsables() {
        List<StandaloneUI> uiUsableList = new ArrayList<>();
        uiList.stream().forEach((ui) -> {
            if (ui.isUsable(user)) {
                uiUsableList.add(ui);
            }
        });

        order(uiUsableList);
        /*
         Comparator<UI> comparator = (UI t, UI t1) ->
         t1.getUIPath().getDifficulty() - t.getUIPath().getDifficulty();
         Collections.sort(uiUsableList, comparator);*/

        return uiUsableList;
    }

    /**
     *
     * @return
     */
    public List<StandaloneUI> getUIs() {
        if (uiList == null) {
            uiList = new ArrayList<>();
        }
        return uiList;
    }

    /**
     * Close all the stages in the list.
     *
     */
    public void close() {
        getUIs().stream().forEach((UI ui) -> {
            ui.close();
        });
    }

    /**
     *
     */
    public void completionCheck() {
        Boolean isBuilt = true;
        for (UI ui : getUIs()) {
            isBuilt = isBuilt && ui.isBuilt();
        }

        setBuilt(isBuilt);
    }

    /**
     *
     * @param index
     */
    public void launch(Integer index) {
        getUIs().stream().forEach((ui) -> {
            ui.setCurrentProperties();
        });
        getUsables().get(index).open();

    }

    /*
     public void launch(Integer index){
     stackedUI.load(stackList.get(index));
     stackedUI.open();
     }*/
    /**
     *
     * @param value
     */
    public void setValue(String value) {
        valueProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public String getValue() {
        return valueProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty valueProperty() {
        if (value == null) {
            value = new SimpleStringProperty(initValue);
        }
        return value;
    }

    private BooleanProperty built;

    /**
     *
     * @return
     */
    public Boolean isBuilt() {
        return builtProperty().getValue();
    }

    /**
     *
     * @param value
     */
    public void setBuilt(Boolean value) {
        builtProperty().setValue(value);
    }

    /**
     *
     * @return built
     */
    public BooleanProperty builtProperty() {
        if (built == null) {
            built = new SimpleBooleanProperty(Boolean.FALSE);
        }
        return built;
    }

}
