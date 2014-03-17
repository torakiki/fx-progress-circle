/* 
 * Copyright (c) 2014, Andrea Vacondio
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 * this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation 
 * and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.pdfsam.ui;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * Skin of the ring progress indicator where an arc grows and by the progress value up to 100% where the arc becomes a ring.
 * 
 * @author Andrea Vacondio
 *
 */
public class RingProgressIndicatorSkin implements Skin<RingProgressIndicator> {

    private final RingProgressIndicator indicator;
    private final Label percentLabel = new Label();
    private final Circle innerCircle = new Circle();
    private final Circle outerCircle = new Circle();
    private final StackPane container = new StackPane();
    private final Arc fillerArc = new Arc();
    private final RotateTransition transition = new RotateTransition(Duration.millis(2000), fillerArc);

    public RingProgressIndicatorSkin(final RingProgressIndicator indicator) {
        this.indicator = indicator;
        initContainer(indicator);
        initFillerArc();
        container.widthProperty().addListener((o, oldVal, newVal) -> {
            fillerArc.setCenterX(newVal.intValue() / 2);
        });
        container.heightProperty().addListener((o, oldVal, newVal) -> {
            fillerArc.setCenterY(newVal.intValue() / 2);
        });
        innerCircle.getStyleClass().add("ringindicator-inner-circle");
        outerCircle.getStyleClass().add("ringindicator-outer-circle-secondary");
        updateRadii();

        this.indicator.indeterminateProperty().addListener((o, oldVal, newVal) -> {
            initIndeterminate(newVal);
        });
        this.indicator.progressProperty().addListener((o, oldVal, newVal) -> {
            if (newVal.intValue() >= 0) {
                setProgressLabel(newVal.intValue());
                fillerArc.setLength(newVal.intValue() * -3.6);
            }
        });
        this.indicator.ringWidthProperty().addListener((o, oldVal, newVal) -> {
            updateRadii();
        });
        innerCircle.strokeWidthProperty().addListener((e) -> {
            updateRadii();
        });
        innerCircle.radiusProperty().addListener((e) -> {
            updateRadii();
        });
        initTransition();
        initIndeterminate(indicator.isIndeterminate());
        initLabel(indicator.getProgress());
        indicator.visibleProperty().addListener((o, oldVal, newVal) -> {
            if (newVal && this.indicator.isIndeterminate()) {
                transition.play();
            } else {
                transition.pause();
            }
        });
        container.getChildren().addAll(fillerArc, outerCircle, innerCircle, percentLabel);
    }

    private void setProgressLabel(int value) {
        if (value >= 0) {
            percentLabel.setText(String.format("%d%%", value));
        }
    }

    private void initTransition() {
        transition.setAutoReverse(false);
        transition.setCycleCount(Animation.INDEFINITE);
        transition.setDelay(Duration.ZERO);
        transition.setInterpolator(Interpolator.LINEAR);
        transition.setByAngle(360);
    }

    private void initFillerArc() {
        fillerArc.setManaged(false);
        fillerArc.getStyleClass().add("ringindicator-filler");
        fillerArc.setStartAngle(90);
    }

    private void initContainer(final RingProgressIndicator indicator) {
        container.getStylesheets().addAll(indicator.getStylesheets());
        container.getStyleClass().addAll("circleindicator-container");
        container.setMaxHeight(Region.USE_PREF_SIZE);
        container.setMaxWidth(Region.USE_PREF_SIZE);
    }

    private void updateRadii() {
        double ringWidth = indicator.getRingWidth();
        double innerCircleHalfStrokeWidth = innerCircle.getStrokeWidth() / 2;
        double innerCircleRadius = indicator.getInnerCircleRadius();
        outerCircle.setRadius(innerCircleRadius + innerCircleHalfStrokeWidth + ringWidth);
        fillerArc.setRadiusY(innerCircleRadius + innerCircleHalfStrokeWidth - 1 + (ringWidth / 2));
        fillerArc.setRadiusX(innerCircleRadius + innerCircleHalfStrokeWidth - 1 + (ringWidth / 2));
        fillerArc.setStrokeWidth(ringWidth);
        innerCircle.setRadius(innerCircleRadius);
    }

    private void initLabel(int value) {
        setProgressLabel(value);
        percentLabel.getStyleClass().add("circleindicator-label");
    }

    private void initIndeterminate(boolean newVal) {
        percentLabel.setVisible(!newVal);
        if (newVal) {
            fillerArc.setLength(360);
            fillerArc.getStyleClass().add("indeterminate");
            if (indicator.isVisible()) {
                transition.play();
            }
        } else {
            fillerArc.getStyleClass().remove("indeterminate");
            fillerArc.setRotate(0);
            transition.stop();
        }
    }

    @Override
    public RingProgressIndicator getSkinnable() {
        return indicator;
    }

    @Override
    public Node getNode() {
        return container;
    }

    @Override
    public void dispose() {
        transition.stop();
    }

}
