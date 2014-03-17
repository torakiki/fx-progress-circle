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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.scene.control.Control;

import com.sun.javafx.css.converters.SizeConverter;

/**
 * Base class for the progress indicator controls represented by circualr shapes
 * 
 * @author Andrea Vacondio
 *
 */
abstract class ProgressCircleIndicator extends Control {
    private static final int INDETERMINATE_PROGRESS = -1;

    private ReadOnlyIntegerWrapper progress = new ReadOnlyIntegerWrapper(0);
    private ReadOnlyBooleanWrapper indeterminate = new ReadOnlyBooleanWrapper(false);

    public ProgressCircleIndicator() {
        this.getStylesheets().add(ProgressCircleIndicator.class.getResource("circleprogress.css").toExternalForm());
    }

    public int getProgress() {
        return progress.get();
    }

    /**
     * Set the value for the progress, it cannot be more then 100 (meaning 100%). A negative value means indeterminate progress.
     * 
     * @param progressValue
     * @see ProgressCircleIndicator#makeIndeterminate()
     */
    public void setProgress(int progressValue) {
        progress.set(defaultToHundred(progressValue));
        indeterminate.set(progressValue < 0);
    }

    public ReadOnlyIntegerProperty progressProperty() {
        return progress.getReadOnlyProperty();
    }

    public boolean isIndeterminate() {
        return indeterminate.get();
    }

    public void makeIndeterminate() {
        setProgress(INDETERMINATE_PROGRESS);
    }

    public ReadOnlyBooleanProperty indeterminateProperty() {
        return indeterminate.getReadOnlyProperty();
    }

    private int defaultToHundred(int value) {
        if (value > 100) {
            return 100;
        }
        return value;
    }

    public final void setInnerCircleRadius(int value) {
        innerCircleRadiusProperty().set(value);
    }

    public final DoubleProperty innerCircleRadiusProperty() {
        return innerCircleRadius;
    }

    public final double getInnerCircleRadius() {
        return innerCircleRadiusProperty().get();
    }

    /**
     * radius of the inner circle
     */
    private DoubleProperty innerCircleRadius = new StyleableDoubleProperty(60) {
        @Override
        public Object getBean() {
            return ProgressCircleIndicator.this;
        }

        @Override
        public String getName() {
            return "innerCircleRadius";
        }

        @Override
        public CssMetaData<ProgressCircleIndicator, Number> getCssMetaData() {
            return StyleableProperties.INNER_CIRCLE_RADIUS;
        }
    };

    private static class StyleableProperties {
        private static final CssMetaData<ProgressCircleIndicator, Number> INNER_CIRCLE_RADIUS = new CssMetaData<ProgressCircleIndicator, Number>(
                "-fx-inner-radius", SizeConverter.getInstance(), 60) {

            @Override
            public boolean isSettable(ProgressCircleIndicator n) {
                return n.innerCircleRadiusProperty() == null || !n.innerCircleRadiusProperty().isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(ProgressCircleIndicator n) {
                return (StyleableProperty<Number>) n.innerCircleRadiusProperty();
            }
        };

        public static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Control.getClassCssMetaData());
            styleables.add(INNER_CIRCLE_RADIUS);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }
}
