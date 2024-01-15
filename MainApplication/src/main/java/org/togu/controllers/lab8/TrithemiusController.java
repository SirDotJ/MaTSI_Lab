package org.togu.controllers.lab8;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.togu.common.*;
import org.togu.lab8.Trithemius;

import java.util.ArrayList;
import java.util.List;

public class TrithemiusController implements EncryptorForm, DecryptorForm {
	@FXML
	TextField coefficientA;
	@FXML
	TextField coefficientB;
	@FXML
	TextField coefficientC;

	@FXML
	TextField functionOutput;

	@FXML
	TextArea inputMessage;
	@FXML
	TextArea outputMessage;

	@FXML
	private RadioButton cyrillicRadioButton;
	@FXML
	private RadioButton latinRadioButton;

	private final Trithemius encryptor = new Trithemius();

	private int getCoefficientA() {
		return Integer.parseInt(this.coefficientA.getText());
	}
	private int getCoefficientB() {
		return Integer.parseInt(this.coefficientB.getText());
	}
	private int getCoefficientC() {
		return Integer.parseInt(this.coefficientC.getText());
	}

	private Alphabet getSelectedAlphabet() {
		if (cyrillicRadioButton.isSelected())
			return USED_CYRILLIC_ALPHABET;
		else
			return USED_LATIN_ALPHABET;
	}

	public static final Alphabet USED_CYRILLIC_ALPHABET = AlphabetConstants.CYRILLIC_WITH_SPACE;
	public static final Alphabet USED_LATIN_ALPHABET = AlphabetConstants.LATIN_WITH_SPACE;

	private boolean initializeCryptosystem() {
		Alphabet previousAlphabet = this.encryptor.getAlphabet();
		Alphabet alphabet = this.getSelectedAlphabet();
		this.encryptor.setNewAlphabet(alphabet);

		int coefficientA = this.getCoefficientA();
		int coefficientB = this.getCoefficientB();
		int coefficientC = this.getCoefficientC();
		try {
			this.encryptor.setNewKey(FunctionMath.cypherFunctionGenerator(
				coefficientA,
				coefficientB,
				coefficientC
		));
		} catch (IllegalArgumentException e) {
			Alerts.showError(
					"Недопустимые коэффициенты",
					"Коэффициенты функции Тритемиуса недопустимы",
					"Пожалуйста выберете коэффициенты, приводящие к однозначной шифровке"
			);
			this.encryptor.setNewAlphabet(previousAlphabet);
			return false;
		}

		this.displayFunction();

		return true;
	}

	public void generateFunction() {
		int coefficientValueRange = 100;
		List<Integer> coefficients;
		do {
			coefficients = FunctionMath.randomCoefficients(3, coefficientValueRange);
		} while (!FunctionMath.checkCoefficientValidityForTrithemius(this.getSelectedAlphabet(), coefficients));
		this.coefficientA.setText(String.valueOf(coefficients.get(0)));
		this.coefficientB.setText(String.valueOf(coefficients.get(1)));
		this.coefficientC.setText(String.valueOf(coefficients.get(2)));

		this.displayFunction();
	}

	public void displayFunction() {
		List<Integer> coefficients = new ArrayList<>();
		coefficients.add(this.getCoefficientA());
		coefficients.add(this.getCoefficientB());
		coefficients.add(this.getCoefficientC());

		if (!FunctionMath.checkCoefficientValidityForTrithemius(this.getSelectedAlphabet(), coefficients))
			this.functionOutput.setText("Шифрация по функции не однозначна!");
		else
			this.functionOutput.setText( // Ниже просто парсинг в функцию вида Ap^2 + Bp + C с учётом знаков и нулевых коэффициентов
					(this.getCoefficientA() != 0 ? (this.getCoefficientA() + "p^2") : "") +
					(this.getCoefficientB() != 0 ? ((this.getCoefficientB() < 0 ? " - " : " + ") + Math.abs(this.getCoefficientB()) + "p") : "") +
					(this.getCoefficientC() != 0 ? ((this.getCoefficientC() < 0 ? " - " : " + ") + Math.abs(this.getCoefficientC())) : "")
			);
	}

	@Override
	public void encrypt() {
		if (!this.initializeCryptosystem())
			return;
		String encryptedText = this.encryptor.encrypt(this.inputMessage.getText());
		this.outputMessage.setText(encryptedText);
	}
	@Override
	public void decrypt() {
		if (!this.initializeCryptosystem())
			return;
		String decryptedText = this.encryptor.decrypt(this.inputMessage.getText());
		this.outputMessage.setText(decryptedText);
	}

	public void transferOutputToInput() {
		this.inputMessage.setText(this.outputMessage.getText());
		this.outputMessage.setText("");
	}

	public void openHelp() {
		HelpController.open("Алгоритм Тритемиуса", "TrithemiusDescription.md");
	}
}
