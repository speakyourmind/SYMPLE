/*
 * Copyright (C) 2014 SpeakYourMind Foundation
 * Visit us at http://www.speakyourmindfoundation.org/

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
package org.symfound.device.emulation.input.mouse;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import org.symfound.builder.component.BaseUI;
import org.symfound.builder.session.Display;

/**
 *
 * @author Javed Gangjee
 */
public class ZoomUI extends BaseUI {

    /**
     *
     */
    public static final Color TRANSLUCENT_COLOUR = new Color(0, 0, 0, 0.05);

    /**
     *
     */
    public double zoomScale = 2.5;

    /**
     *
     */
    public int rectWidth = 200;

    /**
     *
     */
    public int rectHeight = 200;

    /**
     *
     */
    public int x = 0;

    /**
     *
     */
    public int y = 0;
    private int posX;
    private int posY;

    /**
     *
     */
    public static final double CORNER_OFFSET = 0.75;
    private final int origX;
    private final int origY;

    /**
     *
     */
    public AnchorPane anchorPane;

    /**
     *
     * @param location
     * @param zoom
     * @param width
     * @param height
     * @throws AWTException
     */
    public ZoomUI(Point location, double zoom, int width, int height) throws AWTException {

        x = (int) location.getX();
        y = (int) location.getY();
        origX = x;
        origY = y;
        zoomScale = zoom;
        rectWidth = width;
        rectHeight = height;
    }

    /**
     *
     * @param location
     * @throws AWTException
     */
    public ZoomUI(Point location) throws AWTException {
        x = (int) location.getX();
        y = (int) location.getY();
        origX = x;
        origY = y;

    }

    /**
     *
     * @throws AWTException
     */
    public void showWindow() throws AWTException {
        // Add the ImageView object to a pane
        anchorPane = createPane();
        Scene scene = new Scene(anchorPane);
        scene.setFill(TRANSLUCENT_COLOUR);
        setScene(scene);
        initStyle(StageStyle.TRANSPARENT);
        setFullScreen(true);
        supressFullScreenHint();
        show();
    }

    /**
     *
     * @return @throws java.awt.AWTException
     */
    public AnchorPane createPane() throws AWTException {
        AnchorPane blocker = new AnchorPane();
        blocker.setStyle("-fx-background-color:#0000;");
        final GlassButton glassButton = new GlassButton();
        glassButton.setOnMouseReleased((MouseEvent e) -> {
            close();
        });
        blocker.getChildren().add(glassButton);
        refreshImage();
        // Set the preferred height and width of the pane
        AnchorPane pane = new AnchorPane();
        pane.getChildren().add(getImageView());
        pane.setMaxHeight(rectWidth);
        pane.setMaxWidth(rectHeight);
        pane.setTranslateX(posX);
        pane.setTranslateY(posY);
        blocker.getChildren().add(pane);
        // Return the created pane
        return blocker;
    }

    /**
     *
     * @param u
     * @param rectDim
     * @return
     */
    public int getRectPos(int u, int rectDim) {
        int rectPos = u - rectDim / 2;
        return rectPos;
    }

    /**
     *
     * @param e
     * @return
     */
    public Point getClickPoint(MouseEvent e) {

        int zoomX = (int) e.getScreenX();
        int zoomY = (int) e.getScreenY();

        int clickX = origX + (int) ((zoomX - x) / zoomScale);
        int clickY = origY + (int) ((zoomY - y) / zoomScale);

        Point point = new Point(clickX, clickY);

        return point;
    }

    /**
     *
     * @return @throws AWTException
     */
    public ImageView createScreenCaptureView() throws AWTException {
        Display display = new Display();
        Integer screenWidth = display.getScreenWidth();
        Integer screenHeight = display.getScreenHeight();
        // Create image rectangle using the x,y,position, width and height
        posX = getRectPos(origX, rectWidth);
        posY = getRectPos(origY, rectHeight);
        Rectangle rect = new Rectangle(posX, posY, rectWidth, rectHeight);
        Image FXImage = getScreenCaptureImage(rect);
        // Add the FXImage to an ImageView
        ImageView view = new ImageView(FXImage);
        // Scale the image to zoom in
        view.setScaleX(zoomScale);
        view.setScaleY(zoomScale);

        if (posX > screenWidth - rectWidth) {
            int originalPosX = posX;
            posX = (int) (screenWidth - CORNER_OFFSET * rectWidth - rectWidth);
            x -= (originalPosX - posX);
        } else if (posX < rectWidth / 2) {
            int originalPosX = posX;
            posX = (int) (CORNER_OFFSET * rectWidth);
            x -= (originalPosX - posX);
        }

        if (posY > screenHeight - rectHeight) {
            int originalPosY = posY;
            posY = (int) (screenHeight - CORNER_OFFSET * rectHeight - rectHeight);
            y -= (originalPosY - posY);
        } else if (posY < rectHeight / 2) {
            int originalPosY = posY;
            posY = (int) (CORNER_OFFSET * rectHeight);
            y -= (originalPosY - posY);
        }

        return view;
    }

    /**
     *
     * @param rect
     * @return
     * @throws AWTException
     */
    public Image getScreenCaptureImage(Rectangle rect) throws AWTException {
        Robot robot = new Robot();
        // Create a screen capture in the rectangle created
        BufferedImage bufferedImage = robot.createScreenCapture(rect);
        // Convert the awt image to an FX image
        Image FXImage = SwingFXUtils.toFXImage(bufferedImage, null);
        return FXImage;
    }

    private ObjectProperty<ImageView> imageView;

    /**
     *
     * @param value
     */
    public void setImageView(ImageView value) {
        imageViewProperty().setValue(value);
    }

    /**
     *
     * @throws AWTException
     */
    public void refreshImage() throws AWTException {
        setImageView(createScreenCaptureView());
    }

    /**
     *
     * @return
     */
    public ImageView getImageView() {
        return imageViewProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<ImageView> imageViewProperty() {
        if (imageView == null) {
            imageView = new SimpleObjectProperty<>();
        }
        return imageView;
    }

    /**
     *
     * @return
     */
    public double getScale() {
        return zoomScale;
    }

    /**
     *
     * @param scale
     */
    public void setScale(double scale) {
        this.zoomScale = scale;
    }

    /**
     *
     * @param width
     * @param height
     */
    public void setDimensions(int width, int height) {
        rectWidth = width;
        rectHeight = height;
    }

    /**
     *
     */
    public BooleanProperty active;

    /**
     *
     * @param value
     */
    public void setActive(Boolean value) {
        activeProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Boolean isActive() {
        return activeProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty activeProperty() {
        if (active == null) {
            active = new SimpleBooleanProperty(false);

        }
        return active;
    }

}
