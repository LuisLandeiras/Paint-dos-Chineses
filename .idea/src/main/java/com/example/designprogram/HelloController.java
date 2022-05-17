package com.example.paint;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/*
Objetivos:
-Salvar imagem
-Desenhar fractais
-Permitir zoom
-Criar a opção de balde de tinta
 */
public class HelloController {
    @FXML
    private Canvas canvas;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private ToggleButton eraser, pencil;
    @FXML
    private Spinner pincel;
    @FXML
    private Slider tamfiguras, grofiguras;
    @FXML
    private CheckBox triangulo, quadrado, circulo, balde;
    @FXML
    private Label label, label2;
    @FXML
    private TextField zoom;

    //Método responsável pelas alterações do canvas
    @FXML
    public void initialize() {
        Circle c = new Circle();
        Rectangle r = new Rectangle();
        GraphicsContext coords = canvas.getGraphicsContext2D();
        canvas.setOnMouseDragged(e -> {
            double size = Double.parseDouble(String.valueOf(pincel.getValue()));
            double x = e.getX() - size / 2;
            double y = e.getY() - size / 2;
            if (e.getButton() == MouseButton.PRIMARY) {
                if (eraser.isSelected()) {
                    coords.clearRect(x, y, size, size);
                    quadrado.setSelected(false);
                    circulo.setSelected(false);
                    triangulo.setSelected(false);
                    balde.setSelected(false);
                    pencil.setSelected(false);
                } else if (pencil.isSelected()) {
                    coords.setFill(colorPicker.getValue());
                    coords.fillRect(x, y, size, size);
                }
            }
        });

        canvas.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                double size = Double.parseDouble(String.valueOf(tamfiguras.getValue()));
                double grossura = Double.parseDouble(String.valueOf(grofiguras.getValue()));
                double x = e.getX() - size / 2;
                double y = e.getY() - size / 2;
                if (circulo.isSelected()) {
                    c.setCenterX(x + size / 2);
                    c.setCenterY(y + size / 2);
                    c.setRadius(size / 2);
                    coords.setStroke(colorPicker.getValue());
                    coords.strokeOval(x, y, size, size);
                    coords.setLineWidth(grossura);
                }
                if (quadrado.isSelected()) {
                    r.setX(x);
                    r.setY(y);
                    r.setHeight(size);
                    r.setHeight(size);
                    coords.setStroke(colorPicker.getValue());
                    coords.strokeRect(x, y, size, size);
                    coords.setLineWidth(grossura);
                }
                if (triangulo.isSelected()) {
                    //triangulo retangulo
                    double[] xPoints = {x, x + size, x - size};
                    double[] yPoints = {y, y + size, y + size};
                    coords.setStroke(colorPicker.getValue());
                    coords.strokePolygon(xPoints, yPoints, 3);
                    coords.setLineWidth(grossura);
                }
                if (balde.isSelected()) {
                    coords.setFill(colorPicker.getValue());
                    coords.fillRect(x, y, coords.getCanvas().getHeight(), coords.getCanvas().getWidth());
                }
            }
        });
    }

    //Método responsavel por mostrar o tamanho das figuras
    @FXML
    public void tamanhofig() {
        tamfiguras.setMax(1000);
        tamfiguras.setMin(0);
        double size = Double.parseDouble(String.valueOf(tamfiguras.getValue()));
        int resultado = (int) size;
        label.setText(String.valueOf(resultado));
    }

    //Método responsavel por mostrar a grossura das figuras
    @FXML
    public void grossurafig(){
        grofiguras.setMax(100);
        grofiguras.setMin(0);
        double size = Double.parseDouble(String.valueOf(grofiguras.getValue()));
        int resultado2 = (int) size;
        label2.setText(String.valueOf(resultado2));
    }
    @FXML
    public void tamanho(){
        pincel.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100));
    }

    //Método responsável por salvar o canvas
    @FXML
    public void save() {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg")
        );
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            WritableImage writableImage = canvas.snapshot(null, null);
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Método responsável por importar uma imagem para o canvas
    @FXML
    public void open() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            canvas.getGraphicsContext2D().drawImage(image, 0, 0);
        }
    }

    //Método responsável por limpar o canvas
    @FXML
    public void clear() {
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    //Método responsável por dar zoom no canvas
    @FXML
    public void zoom(){
        if (Integer.parseInt(zoom.getText()) <= 5 ){
            int i = Integer.parseInt(zoom.getText());
            canvas.setScaleX(i);
            canvas.setScaleY(i);
        }else{
            zoom.setText("5");
        }
    }
}