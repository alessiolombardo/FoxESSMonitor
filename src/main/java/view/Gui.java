package view;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import controller.Controller;
import model.FoxEssVariables;
import model.Settings;

public class Gui {

	private Controller controller;

	private Settings settings;

	public void setController(Controller controller) {
		this.controller = controller;
	}

	@FXML
	private TextField statusTextField;

	@FXML
	private TextField apiKeyTextField;

	@FXML
	private TextField inverterSNTextField;

	@FXML
	private TextField exportFileTextField;

	@FXML
	private CheckBox appendCheckBox;

	@FXML
	private DatePicker beginDatePicker;

	@FXML
	private ComboBox<Byte> beginHourComboBox;

	@FXML
	private ComboBox<Byte> spanHourComboBox;

	@FXML
	private ComboBox<Byte> spanDayComboBox;

	@FXML
	private TableView<List<String>> monitorTableView;

	@FXML
	private ListView<FoxEssVariables> allVariablesListView;

	@FXML
	private ListView<FoxEssVariables> enabledVariablesListView;

	@FXML
	void extractData(ActionEvent event) {
		saveSettings(null);
		controller.extractData(spanDayComboBox.getValue() > 0, false);
	}

	@FXML
	void exportData(ActionEvent event) {
		saveSettings(null);
		controller.extractData(spanDayComboBox.getValue() > 0, true);
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
	void setBeginDateAndHour(ActionEvent event) {
		LocalDate date = beginDatePicker.getValue();
		if (spanDayComboBox.getValue() > 0) {
			beginHourComboBox.setValue((byte) 0);
		}
		settings.setBeginDate(LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(),
				beginHourComboBox.getValue(), 0));
		settings.setSpanDay(spanDayComboBox.getValue());
		settings.setEndDate(spanDayComboBox.getValue() > 0);
	}

	@FXML
	void setSpanDay(ActionEvent event) {
		beginHourComboBox.setDisable(spanDayComboBox.getValue() > 0);
		beginHourComboBox.setValue((byte) 0);
		spanHourComboBox.setDisable(spanDayComboBox.getValue() > 0);
		settings.setSpanDay(spanDayComboBox.getValue());
		settings.setSpanHour(spanHourComboBox.getValue());
		settings.setEndDate(spanDayComboBox.getValue() > 0);
	}

	@FXML
	void setSpanHour(ActionEvent event) {
		settings.setSpanHour(spanHourComboBox.getValue());
		settings.setEndDate(false);
	}

	@FXML
	void saveSettings(ActionEvent event) {

		settings.setApiKey(apiKeyTextField.getText());
		settings.setInverterSerialNumber(inverterSNTextField.getText());

		controller.saveSettings();

	}

	public void controlsInit() {

		settings = controller.loadSettings();

		apiKeyTextField.setText(settings.getApiKey());
		inverterSNTextField.setText(settings.getInverterSerialNumber());
		exportFileTextField.setText(settings.getExportFilename());

		allVariablesListView.getItems().addAll(FoxEssVariables.values());

		enabledVariablesListView.getItems().addAll(settings.getVariablesList());

		beginDatePicker.setValue(settings.getBeginDate().toLocalDate());

		List<Byte> dayList = new ArrayList<Byte>();
		for (int i = 0; i < 32; i++) {
			dayList.add((byte) i);
		}
		spanDayComboBox.setItems(FXCollections.observableList(dayList));
		spanDayComboBox.getSelectionModel().select(0);

		List<Byte> hoursList = new ArrayList<Byte>();
		for (int i = 0; i < 24; i++) {
			hoursList.add((byte) i);
		}
		beginHourComboBox.setItems(FXCollections.observableList(hoursList));
		beginHourComboBox.getSelectionModel().select(settings.getBeginDate().getHour());

		List<Byte> hoursList2 = new ArrayList<Byte>();
		for (int i = 1; i <= 24; i++) {
			hoursList2.add((byte) i);
		}
		spanHourComboBox.setItems(FXCollections.observableList(hoursList2));
		spanHourComboBox.getSelectionModel().select((Byte) settings.getSpanHour());

		appendCheckBox.setSelected(settings.getAppend());

		updateMonitorColumns();

	}

	private void updateMonitorColumns() {

		monitorTableView.getColumns().clear();
		monitorTableView.getItems().clear();

		TableColumn<List<String>, String> timestampColumn = new TableColumn<>("Timestamp");
		timestampColumn.setCellValueFactory(col -> new SimpleStringProperty(col.getValue().get(0).toString()));
		monitorTableView.getColumns().add(timestampColumn);

		for (FoxEssVariables variable : settings.getVariablesList()) {
			TableColumn<List<String>, String> variableColumn = new TableColumn<>(variable.name());
			int columnIndex = monitorTableView.getColumns().size();
			variableColumn
					.setCellValueFactory(col -> new SimpleStringProperty(col.getValue().get(columnIndex).toString()));
			monitorTableView.getColumns().add(variableColumn);
		}

		monitorTableView.getItems().add(Collections.nCopies(settings.getVariablesList().size() + 1, ""));

	}

	public void updateMonitorRecord(Map<OffsetDateTime, ArrayList<String>> dataMap) {

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

	@FXML
	void addVariableAction(ActionEvent event) {
		controller.enabledVariablesListControl(allVariablesListView.getSelectionModel().getSelectedItem(), 'a');
		enabledVariablesListView.getItems().clear();
		enabledVariablesListView.getItems().addAll(settings.getVariablesList());
		updateMonitorColumns();
	}

	@FXML
	void removeVariableAction(ActionEvent event) {
		controller.enabledVariablesListControl(enabledVariablesListView.getSelectionModel().getSelectedItem(), 'r');
		enabledVariablesListView.getItems().clear();
		enabledVariablesListView.getItems().addAll(settings.getVariablesList());
		updateMonitorColumns();
	}

	@FXML
	void moveUpVariableAction(ActionEvent event) {
		FoxEssVariables selectedVariable = enabledVariablesListView.getSelectionModel().getSelectedItem();
		controller.enabledVariablesListControl(selectedVariable, 'u');
		enabledVariablesListView.getItems().clear();
		enabledVariablesListView.getItems().addAll(settings.getVariablesList());
		enabledVariablesListView.getSelectionModel().select(selectedVariable);
		enabledVariablesListView.requestFocus();
		updateMonitorColumns();
	}

	@FXML
	void moveDownVariableAction(ActionEvent event) {
		FoxEssVariables selectedVariable = enabledVariablesListView.getSelectionModel().getSelectedItem();
		controller.enabledVariablesListControl(selectedVariable, 'd');
		enabledVariablesListView.getItems().clear();
		enabledVariablesListView.getItems().addAll(settings.getVariablesList());
		enabledVariablesListView.getSelectionModel().select(selectedVariable);
		enabledVariablesListView.requestFocus();
		updateMonitorColumns();
	}

}