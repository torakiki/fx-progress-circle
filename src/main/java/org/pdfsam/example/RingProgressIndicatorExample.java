/* 
 * Copyright (c) 2014, Andrea Vacondio
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.pdfsam.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.pdfsam.ui.RingProgressIndicator;

public class RingProgressIndicatorExample extends Application {

	@Override
	public void start(Stage primaryStage) {
		RingProgressIndicator indicator = new RingProgressIndicator();
		Slider slider = new Slider(0, 100, 50);

		slider.valueProperty().addListener((o, oldVal, newVal) -> indicator.setProgress(newVal.intValue()));
		VBox main = new VBox(1, indicator, slider);
		indicator.setProgress(Double.valueOf(slider.getValue()).intValue());
		Scene scene = new Scene(main);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Test ring progress");
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

}