/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import static javafx.scene.layout.AnchorPane.setBottomAnchor;
import static javafx.scene.layout.AnchorPane.setLeftAnchor;
import static javafx.scene.layout.AnchorPane.setRightAnchor;
import static javafx.scene.layout.AnchorPane.setTopAnchor;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Window;
import org.symfound.builder.user.Usable;
import org.symfound.builder.user.User;
import org.symfound.comm.file.ExtensionAnalyzer;
import org.symfound.controls.RunnableControl;
import org.symfound.main.FullSession;
import org.symfound.main.Main;
import org.symfound.main.builder.UI;

/**
 *
 * @author Javed Gangjee
 */
public abstract class CommonGrid extends GridPane implements Usable {

    private final FullSession session;
    private final User user;

    /**
     *
     */
    public static final Double DEFAULT_GRID_GAP = 5.0;
    public static final Double DEFAULT_MARGIN= 0.0;

    /**
     *
     */
    public CommonGrid() {
        session = Main.getSession();
        user = session.getUser();
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
     * @return
     */
    public FullSession getSession() {
        return session;
    }

    /**
     *
     */
    public List<Node> nodes;

    /**
     *
     * @return
     */
    public List<Node> getNodes() {
        if (nodes == null) {
            refreshNodes();
        }
        return nodes;
    }

    /**
     *
     */
    public void refreshNodes() {
        nodes = moveObservableListToList(getChildren());
    }

    /**
     *
     * @return
     */
    public Integer getNumOfNodes() {
        return getNodes().size();
    }

    /**
     * This transfers the elements of an ObservableList of objects to a List. An
     * ObservableList node list reflects changes to the list if the order of
     * buttons changes. This is an issue if the order is changing through the
     * course of operation. This way the list stays the same.
     *
     * @param observableList ObservableList to convert to a List
     * @return permanent list of objects
     */
    public List<Node> moveObservableListToList(List<Node> observableList) {
        // Node List to return
        List<Node> permList = new ArrayList<>();
        if (observableList != null) {
            observableList.stream().forEach((node) -> {
                if (node instanceof Node) {
                    permList.add(node);
                }
            });
        }

        return permList;
    }

    /**
     * Disable or enable all buttons in the given Node list
     *
     * @param value <code>true</code> to disable, <code>false</code> to enable
     */
    public void setDisableAll(Boolean value) {
        getNodes().stream().forEach((node) -> {
            node.setDisable(value);
        });

    }

    /**
     * Show or hide all buttons in the given Node list.
     *
     * @param value <code>true</code> to show, <code>false</code> to hide
     */
    public void setVisibleAll(Boolean value) {
        getNodes().stream().forEach((node) -> {
            node.setVisible(value);
        });
    }

    /**
     * Set a list of strings to a corresponding list of nodes. NOTE: Currently
     * this only supports buttons, but can be expanded.
     *
     * @param btnText the list of strings to set to the buttons
     */
    public void setTextAll(List<String> btnText) {
        // Repeat for every string in the button list
        for (int i = 0; i < btnText.size(); i++) {
            // If the corresponding node is an instance of a button
            if (getNodes().get(i) instanceof Button) {
                //Save the node as a button
                AnimatedButton btns = (AnimatedButton) getNodes().get(i);
                // Set the corresponding text from the string list to the button
                btns.setText(btnText.get(i));

            }
        }
    }

    /**
     * Clear style and apply CSS to all buttons in the list of nodes. NOTE:
     * Currently this only supports buttons, but can be expanded.
     *
     */
    public void setCSSAll() {
        for (Node node : getNodes()) {
            // If the corresponding node is an instance of a button
            if (node instanceof Button) {
                // Save the node as a button
                AnimatedButton btns = (AnimatedButton) node;
                // Clear all applied styles
                btns.setStyle("");
                // Apply original CSS
                btns.applyCss();
            }
        }
    }

    /**
     * Use to get a list of strings to a corresponding list of buttons. NOTE:
     * Currently this only supports buttons, but can be expanded.
     *
     * @return list of button text as strings
     */
    public List<String> getTextAll() {
        List<String> btnTextList = new ArrayList<>();

        for (Node node : getNodes()) {
            // If the corresponding node is an instance of a button
            if (node instanceof Button) {
                // Save the node as a button
                AnimatedButton btns = (AnimatedButton) node;
                // Get button text and add to list
                btnTextList.add(btns.getText());
            }
        }

        return btnTextList;
    }

    /**
     * Set a list of images to the background of a list of nodes. Randomize if
     * needed. NOTE: Currently this only supports buttons, but can be expanded.
     *
     * @param images list of images to set the node background
     * @param randomize <code>true</code> to randmize
     */
    public void setImagesAll(List<String> images, Boolean randomize) {

        // List of indices to be built. Randomize if needed
        List<Integer> indexList = buildIndexList(images.size(), randomize);

        for (int i = 0; i < getNodes().size(); i++) {
            // Get the node
            Node node = getNodes().get(i);
            // If the node is a button & the images are available
            if (node instanceof Button) {
                // Get the image name based on the index order
                String image = images.get(indexList.get(i));
                ExtensionAnalyzer picChecker = new ExtensionAnalyzer(image);
                // Check if the file is a picture
                if (picChecker.isPictureFile()) {
                    // Set the look of the target
                    node.setStyle("-fx-background-image: url(\"file:///"
                            + image + "\");");

                }
            }
        }
    }

    /**
     *
     * @param size size of the list
     * @param randomize <code>true</code> to randomize
     * @return
     */
    public List<Integer> buildIndexList(int size, Boolean randomize) {

        List<Integer> indexList = new ArrayList<>();

        // Build Integer array list the same size as the number of nodes
        for (int i = 0; i < size; i++) {
            indexList.add(i);
        }

        if (randomize) {
            // Shuffle the index list
            Collections.shuffle(indexList);
        }

        return indexList;
    }

    /**
     *
     * @param row
     * @param column
     * @return
     */
    public RunnableControl get(final Integer row, final Integer column) {
        RunnableControl result = null;
        ObservableList<Node> children = getChildren();

        for (Node node : children) {
            if (node instanceof RunnableControl) {
                RunnableControl screenControl = (RunnableControl) node;
                if (GridPane.getRowIndex(node) == row.intValue()
                        && GridPane.getColumnIndex(node) == column.intValue()) {
                    result = screenControl;
                    break;
                }
            }
        }
        return result;
    }

    /**
     *
     * @param node
     * @param dimension1
     * @param index1
     * @param dimension2
     * @param index2
     */
    public void setCell(Node node, String dimension1, Integer index1, String dimension2, Integer index2) {
        setDimensionIndex(node, index1, dimension1);
        setDimensionIndex(node, index2, dimension2);
    }

    /**
     *
     * @param node
     * @param dimension1
     * @return
     */
    public Integer getDimensionSpan(Node node, String dimension1) {
        return (Integer) node.getProperties().get("gridpane-" + dimension1 + "-span");
    }

    /**
     *
     * @param node
     * @param dimension1
     * @return
     */
    public Integer getDimensionIndex(Node node, String dimension1) {
        return (Integer) node.getProperties().get("gridpane-" + dimension1);
    }

    /**
     *
     * @param node
     * @param nextIndex
     * @param dimension
     * @throws IllegalArgumentException
     */
    public void setDimensionIndex(Node node, Integer nextIndex, String dimension) throws IllegalArgumentException {
        if (nextIndex != null && nextIndex < 0) {
            throw new IllegalArgumentException("columnIndex must be greater or equal to 0, but was " + nextIndex);
        }
        node.getProperties().put("gridpane-" + dimension, nextIndex);
        if (node.getParent() != null) {
            node.getParent().requestLayout();
        }
    }

    /**
     *
     * @param node
     * @param span
     * @param dimension
     * @throws IllegalArgumentException
     */
    public void setDimensionSpan(Node node, Integer span, String dimension) throws IllegalArgumentException {
        if (span != null && span < 1) {
            throw new IllegalArgumentException("span must be greater or equal to 1, but was " + span);
        }
        node.getProperties().put("gridpane-" + dimension + "-span", span);
        if (node.getParent() != null) {
            node.getParent().requestLayout();
        }
    }

    /**
     *
     */
    public final void removeFromParent() {
        if (getParent() instanceof Pane) {
            Pane parent = (Pane) getParent();
            parent.getChildren().remove(this);
        }
    }

    /**
     *
     */
    public void clear() {
        getChildren().removeAll(getChildren());
        getRowConstraints().clear();
        getColumnConstraints().clear();
    }

    /**
     * Get the stage that the provided node belongs to.
     *
     * @return
     */
    public UI getParentUI() {
        Scene scene = getScene();
        Window window = scene.getWindow();
        return (UI) window;
    }

    /**
     *
     */
    public IntegerProperty specRows;

    /**
     *
     * @param value
     */
    public void setSpecRows(Integer value) {
        specRowsProperty().set(value);
    }

    /**
     *
     * @return
     */
    public Integer getSpecRows() {
        return specRowsProperty().get();
    }

    /**
     *
     * @return
     */
    public IntegerProperty specRowsProperty() {
        if (specRows == null) {
            specRows = new SimpleIntegerProperty(1);
        }
        return specRows;
    }

    /**
     *
     */
    public IntegerProperty specColumns;

    /**
     *
     * @param value
     */
    public void setSpecColumns(Integer value) {
        specColumnsProperty().set(value);
    }

    /**
     *
     * @return
     */
    public Integer getSpecColumns() {
        return specColumnsProperty().get();
    }

    /**
     *
     * @return
     */
    public IntegerProperty specColumnsProperty() {
        if (specColumns == null) {
            specColumns = new SimpleIntegerProperty(1);
        }
        return specColumns;
    }


    public void setAnchors(Node node, Double top, Double left, Double right, Double bottom) {
        setTopAnchor(node, top);
        setLeftAnchor(node, left);
        setRightAnchor(node, right);
        setBottomAnchor(node, bottom);
    }

}
