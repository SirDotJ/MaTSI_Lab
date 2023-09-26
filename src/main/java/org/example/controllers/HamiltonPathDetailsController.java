package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Pair;


import java.util.*;

public class HamiltonPathDetailsController {
	@FXML
	TextField displayedPathOutput;
	@FXML
	Line line01, line06, line07, line12, line13, line23, line24, line34, line35, line36, line37, line45, line56, line67;

	// Я знаю, что это ужас, но на сейчас хватит чтобы автоматически при паре чисел обратится к соответствующей линии
	private Map<String, Line> edgeToLine;

	ArrayList<Integer> getKey() throws IllegalArgumentException {
		ArrayList<Integer> output = new ArrayList<>();
		String data = displayedPathOutput.getText();
		ArrayList<String> dataList = new ArrayList<>(Arrays.asList(data.split(" ")));

		dataList.forEach((input) -> {
			int vertexNumber = Integer.parseInt(input);
			if (output.contains(vertexNumber))
				throw new IllegalArgumentException("Path must not repeat any vertex");
			output.add(vertexNumber);
		});
		if (output.size() != 8) // 8 - из выбранного графа
			throw new IllegalArgumentException("Must be path through all vertices");

		return output;
	}
	public void initializeConnections() {
		if (this.edgeToLine == null)
			this.edgeToLine = new HashMap<>();
		this.edgeToLine.put("01", line01);
		this.edgeToLine.put("06", line06);
		this.edgeToLine.put("07", line07);
		this.edgeToLine.put("12", line12);
		this.edgeToLine.put("13", line13);
		this.edgeToLine.put("23", line23);
		this.edgeToLine.put("24", line24);
		this.edgeToLine.put("34", line34);
		this.edgeToLine.put("35", line35);
		this.edgeToLine.put("36", line36);
		this.edgeToLine.put("37", line37);
		this.edgeToLine.put("45", line45);
		this.edgeToLine.put("56", line56);
		this.edgeToLine.put("67", line67);
	}
	public void setPath(String hamiltonPath) {
		this.displayedPathOutput.setText(hamiltonPath);
		this.updatePath();
	}
	private void clearHIghlights() {
		edgeToLine.forEach((edge, line) -> {
			line.setFill(Color.BLACK);
			line.setStroke(Color.BLACK);
		});
	}
	public void updatePath() {
		this.clearHIghlights();
		ArrayList<Integer> path = getKey();
		for (int i = 0; i < path.size() - 1; i++) {
			int vertex1 = path.get(i);
			int vertex2 = path.get(i+1);
			String edgeCode = String.valueOf(Math.min(vertex1, vertex2)) + String.valueOf(Math.max(vertex1, vertex2));
			Line lineToColor = edgeToLine.get(edgeCode);
			lineToColor.setFill(Color.RED);
			lineToColor.setStroke(Color.RED);
		}
	}
}
