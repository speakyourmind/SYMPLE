/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import java.util.prefs.Preferences;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.symfound.builder.loader.RuntimeExecutor;
import org.symfound.controls.AppableControl;
import static org.symfound.controls.user.IftttButton.KEY;

/**
 *
 * @author Javed Gangjee
 */
public class ExecButton extends AppableControl {

    /**
     *
     */
    public static final String RESTART_KEY = "Restart";

    /**
     *
     */
    public static final String RESTART_COMMAND = "shutdown /r";

    /**
     *
     */
    public static final String TSKMGR_KEY = "Task Manager";

    /**
     *
     */
    public static final String TSKMGR_COMMAND = "cmd /c taskmgr.exe,SetSuspendState";

    /**
     *
     */
    public static final String OSK_KEY = "On Screen Keyboard";

    /**
     *
     */
    public static final String OSK_COMMAND = "cmd /c osk.exe,SetSuspendState";

    /**
     * Constructor to allow use on screens.
     */
    public ExecButton() {
        this("button", DEFAULT_COMMAND);
    }

    /**
     *
     * @param CSSClass
     * @param command
     */
    public ExecButton(String CSSClass, String command) {
        super(CSSClass, "Execute", command, "default");
        primary = new AnimatedButton("");
        primary.setWrapText(true);
        load(primary);
        setCSS(cssClass, primary);
        setSelection(primary);
        setConfirmable(false);
        this.initCommand = command;
    }

    @Override
    public void run() {
        String s;
        RuntimeExecutor runtimeExecutor = new RuntimeExecutor();
        runtimeExecutor.execute(getCommand());

    }
    private static final String DEFAULT_COMMAND = "";
    private final String initCommand;
    private StringProperty command;

    /**
     *
     * @param value
     */
    public void setCommand(String value) {
        commandProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public String getCommand() {
        return commandProperty().getValue();
    }

    /**
     *
     * @return
     */
    public final StringProperty commandProperty() {
        if (command == null) {
            command = new SimpleStringProperty(initCommand);
        }
        return command;
    }

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
            Class<? extends ExecButton> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }
}
