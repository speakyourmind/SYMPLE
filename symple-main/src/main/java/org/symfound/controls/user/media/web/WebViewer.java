/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.media.web;

import javafx.scene.web.WebView;
import org.symfound.controls.user.AnimatedButton;
import static org.symfound.controls.user.RootPane.DEFAULT_PREF_HEIGHT;
import static org.symfound.controls.user.RootPane.DEFAULT_PREF_WIDTH;
import org.symfound.controls.user.media.MediaViewer;

/**
 *
 * @author Administrator
 */
public abstract class WebViewer extends MediaViewer {

    /**
     *
     * @param key
     * @param index
     */
    public WebViewer(String key, String index) {
        super("transparent", key, "", index);
        initialize();
    }

    private void initialize() {

        configure();
    }

    /**
     *
     */
    public abstract void configure();

    @Override
    public void loadPrimaryControl() {
        primary = new AnimatedButton("");
        addToPane(getWebView());
    }

    /**
     *
     */
    @Override
    public void addConfigButtons() {
       // addToPane(getEditAppButton(), null, null, getHeight() / 2, getWidth() / 2);
       // getEditAppButton().toFront();
        getPrimaryControl().setDisable(true);
    }

    /**
     *
     */
    public WebView webView;

    /**
     *
     * @return
     */
    public WebView getWebView() {
        if (webView == null) {
            webView = new WebView();
            webView.setPrefSize(DEFAULT_PREF_WIDTH, DEFAULT_PREF_HEIGHT);
            webView.setMaxSize(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

        }
        return webView;
    }
}
