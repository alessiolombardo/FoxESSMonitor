package view;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import controller.Controller;
import model.FoxEssTimeSpan;
import model.FoxEssVariables;
import model.Settings;

public class Gui {

	private Controller controller;

	private Settings settings;

	public void setController(Controller controller) {
		this.controller = controller;
	}

	@FXML
	private TextField usernameTextField;

	@FXML
	private PasswordField passwordTextField;

	@FXML
	private TextField devideIdTextField;

	@FXML
	private TextField endpointTextField;

	@FXML
	private TextField exportFileTextField;

	@FXML
	private CheckBox appendCheckBox;

	@FXML
	private DatePicker datePicker;

	@FXML
	private ComboBox<Byte> hourComboBox;

	@FXML
	private ComboBox<FoxEssTimeSpan> timeSpanComboBox;

	@FXML
	private TableView<List<String>> monitorTableView;

	@FXML
	private GridPane variablesGridPane;

	@FXML
	private TextField statusTextField;

	@FXML
	void extractData(ActionEvent event) {
		saveSettings(null);
		controller.extractData(false);
	}

	@FXML
	void exportData(ActionEvent event) {
		saveSettings(null);
		controller.extractData(true);
	}

	@FXML
	void selectExportFile(MouseEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select Export File");
		fileChooser.setInitialDirectory(new File("."));
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"));
		File file = fileChooser.showSaveDialog(null);
		if (file != null) {
			exportFileTextField.setText(file.getPath());
			settings.setExportFilename(file.getPath());
		}
	}

	@FXML
	void appendSelection(ActionEvent event) {
		settings.setAppend(appendCheckBox.isSelected());
	}

	@FXML
	void setDateAndHour(ActionEvent event) {
		LocalDate date = datePicker.getValue();
		settings.setDate(LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(),
				hourComboBox.getValue(), 0));
	}

	@FXML
	void setTimeSpan(ActionEvent event) {
		settings.setTimeSpan(timeSpanComboBox.getValue());
	}

	@FXML
	void saveSettings(ActionEvent event) {

		settings.setEndpoint(endpointTextField.getText());
		settings.setUsername(usernameTextField.getText());
		try {
			settings.setPassword(passwordTextField.getText());
		} catch (NoSuchAlgorithmException e) {
			setStatus(e, "Password encryption failed", true);
		}
		settings.setDeviceId(devideIdTextField.getText());

		controller.saveSettings();

	}

	public void controlsInit() {

		settings = controller.loadSettings();

		endpointTextField.setText(settings.getEndpoint());
		usernameTextField.setText(settings.getUsername());
		passwordTextField.setText("");
		devideIdTextField.setText(settings.getDeviceId());
		exportFileTextField.setText(settings.getExportFilename());

		for (FoxEssVariables variables : settings.getVariables().keySet()) {
			CheckBox c = new CheckBox(variables.name());
			c.setSelected(settings.getVariables().get(variables));
			c.selectedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> v, Boolean oldValue, Boolean newValue) {
					settings.getVariables().put(variables, newValue);
					updateMonitorColumns();
				}
			});
			variablesGridPane.add(c, (int) (variables.ordinal() / 14), variables.ordinal() % 14);
		}

		datePicker.setValue(settings.getDate().toLocalDate());

		List<Byte> hoursList = new ArrayList<Byte>();
		for (int i = 0; i < 24; i++) {
			hoursList.add((byte) i);
		}
		hourComboBox.setItems(FXCollections.observableList(hoursList));
		hourComboBox.getSelectionModel().select(settings.getDate().getHour());

		timeSpanComboBox.setItems(FXCollections.observableList(Arrays.asList(FoxEssTimeSpan.values())));
		timeSpanComboBox.getSelectionModel().select(settings.getTimeSpan());

		appendCheckBox.setSelected(settings.getAppend());

		updateMonitorColumns();

	}

	private void updateMonitorColumns() {

		monitorTableView.getColumns().clear();
		monitorTableView.getItems().clear();

		TableColumn<List<String>, String> timestampColumn = new TableColumn<>("Timestamp");
		timestampColumn.setCellValueFactory(col -> new SimpleStringProperty(col.getValue().get(0).toString()));
		monitorTableView.getColumns().add(timestampColumn);

		for (Map.Entry<FoxEssVariables, Boolean> entry : settings.getVariables().entrySet()) {
			if (entry.getValue().equals(true)) {
				TableColumn<List<String>, String> variableColumn = new TableColumn<>(entry.getKey().name());
				int columnIndex = monitorTableView.getColumns().size();
				variableColumn.setCellValueFactory(
						col -> new SimpleStringProperty(col.getValue().get(columnIndex).toString()));
				monitorTableView.getColumns().add(variableColumn);
			}
		}

		monitorTableView.getItems().add(Collections.nCopies(settings.getVariables().entrySet().size() + 1, ""));

	}

	public void updateMonitowRecord(Map<OffsetDateTime, ArrayList<String>> dataMap) {

		monitorTableView.getItems().clear();

		for (Map.Entry<OffsetDateTime, ArrayList<String>> entry : dataMap.entrySet()) {
			ArrayList<String> record = new ArrayList<String>();
			record.add(entry.getKey().toString());
			for (String variables : entry.getValue()) {
				record.add(variables);
			}
			monitorTableView.getItems().add(record);
		}

	}

	public void setStatus(Exception e, String status, boolean isError) {
		if (e != null) {
			e.printStackTrace();
		}
		statusTextField.setText(status);
		if (isError) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText(status);
			alert.show();
		}
	}

}