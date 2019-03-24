package sample;


import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.paint.Color;

public class MainWindowController implements Initializable {

  @FXML
  public Canvas canvas;
  public JFXTextField angleField;
  public JFXTextField branchAngleField;
  public JFXSlider complexity;
  public JFXSlider branchLength;
  public JFXSlider lengthRatio;
  private GraphicsContext gc;
  private Random rand;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    rand = new Random();
    gc = canvas.getGraphicsContext2D();

    UnaryOperator<Change> filter = change -> {
      String text = change.getText();

      if (text.matches("[0-9]*")) {
        return change;
      }

      return null;
    };
    angleField.setTextFormatter(new TextFormatter<>(filter));
    branchAngleField.setTextFormatter(new TextFormatter<>(filter));
    branchLength.valueProperty().addListener((observable, oldValue, newValue) -> generate() );
    lengthRatio.valueProperty().addListener((observable, oldValue, newValue) -> generate() );
    complexity.valueProperty().addListener((observable, oldValue, newValue) -> generate() );
  }

  private void draw(double startX, double startY, double endX, double endY, double depth,
      double lineLength, double angle) {
    if (depth < complexity.getValue()) {

      gc.setStroke(Color.BLACK);
      gc.strokeLine(startX, startY, endX, endY);


      draw(endX, endY, endX + (lineLength * Math
              .sin(angle + Math.toRadians(Double.valueOf(branchAngleField.getText())))),
          endY - (lineLength * Math
              .cos(angle + Math.toRadians(Double.valueOf(branchAngleField.getText())))),
          depth + 1, lineLength * lengthRatio.getValue(),
          angle + Math.toRadians(Double.valueOf(angleField.getText())));

      draw(endX, endY, endX + (lineLength * Math
              .sin(angle - Math.toRadians(Double.valueOf(branchAngleField.getText())))),
          endY - (lineLength * Math
              .cos(angle - Math.toRadians(Double.valueOf(branchAngleField.getText())))),
          depth + 1, lineLength * lengthRatio.getValue(),
          angle - Math.toRadians(Double.valueOf(angleField.getText())));
    }
  }

  public Color getRandomColor() {
    rand = new Random();
    int r = rand.nextInt(255);
    int g = rand.nextInt(255);
    int b = rand.nextInt(255);
    Color c = Color.rgb(r, g, b);
    return c;
  }

  public void setRandomValues(){
    complexity.setValue(3+rand.nextInt(10));
    lengthRatio.setValue(0.1+rand.nextDouble());
    branchLength.setValue(rand.nextInt(200)+50);
    angleField.setText(String.valueOf(rand.nextInt(80)));
    branchAngleField.setText(String.valueOf(rand.nextInt(80)));
    generate();
  }

  @FXML
  public void generate() {
    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    draw(600, 800, 600, 650, 0, branchLength.getValue(), 0);
  }

  public void random(ActionEvent actionEvent) {
    setRandomValues();
  }
}