package com.example.designprogram;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/*
Objetivos:
-Desenhar fractais(Beta)
-Permitir zoom(Beta)
-Criar a opção de balde de tinta
 */
public class HelloController {
    @FXML
    private Canvas canvas;
    @FXML
    private AnchorPane Pane;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private ToggleButton eraser, pencil;
    @FXML
    private Spinner pincel;
    @FXML
    private Slider tamfiguras, grofiguras;
    @FXML
    private CheckBox triangulo, quadrado, circulo, bucket;
    @FXML
    private Label label, label2;
    @FXML
    private ImageView image = new ImageView();
    @FXML
    private AnchorPane panes;

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
                    bucket.setSelected(false);
                    pencil.setSelected(false);
                } else if (pencil.isSelected()) {
                    coords.setFill(colorPicker.getValue());
                    coords.fillRect(x, y, size, size);
                }
            }
        });

        ArrayList <Cordenadas> ImageMove = new ArrayList<>(); //Array das coordenadas dos pontos para mover a Imagem

        //Método responsável pelo movimento da imagem
        image.setOnMouseDragged(event -> {
            double size = tamfiguras.getValue();
            double x = event.getX() - size / 2;
            double y = event.getY() - size / 2;
            ImageMove.add(new Cordenadas(x, y));
            image.setTranslateX(image.getTranslateX() - (ImageMove.get(0).getX() - ImageMove.get(ImageMove.size() - 1).getX())/2);
            image.setTranslateY(image.getTranslateY() - (ImageMove.get(0).getY() - ImageMove.get(ImageMove.size() - 1).getY())/2);
        });

        //Responsável por limpar o arrray de movimento da imagem
        image.setOnMouseMoved(event -> {
            ImageMove.clear();  //Create by Traço Sublime
        });

        canvas.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                double size = Double.parseDouble(String.valueOf(tamfiguras.getValue()));
                double grossura = Double.parseDouble(String.valueOf(grofiguras.getValue()));
                double x = e.getX() - size / 2;
                double y = e.getY() - size / 2;
                coords.setLineWidth(grossura);
                if (circulo.isSelected()) {
                    c.setCenterX(x + size / 2);
                    c.setCenterY(y + size / 2);
                    c.setRadius(size / 2);
                    coords.setStroke(colorPicker.getValue());
                    coords.strokeOval(x, y, size, size);
                }
                if (quadrado.isSelected()) {
                    r.setX(x);
                    r.setY(y);
                    r.setHeight(size);
                    r.setHeight(size);
                    coords.setStroke(colorPicker.getValue());
                    coords.strokeRect(x, y, size, size);
                }
                if (triangulo.isSelected()) {
                    //triangulo retangulo
                    double[] xPoints = {x, x + size, x - size};
                    double[] yPoints = {y, y + size, y + size};
                    coords.setStroke(colorPicker.getValue());
                    coords.strokePolygon(xPoints, yPoints, 3);
                }
                if (bucket.isSelected()) {
                    coords.setFill(colorPicker.getValue());
                    coords.fillRect(x, y, size, size);
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
        grofiguras.setMin(1);
        double size = Double.parseDouble(String.valueOf(grofiguras.getValue()));
        int resultado2 = (int) size;
        label2.setText(String.valueOf(resultado2));

    }
    @FXML
    public void tamanho(){
        pincel.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100));
    }


    //Devolve o caminho do ficheiro
    private String FilePath(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Background Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPEG", "*.jpg"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg")
        );
        File file = fileChooser.showOpenDialog(Pane.getScene().getWindow());
        return file.getAbsolutePath();
    }

    //Importa a imagem
    @FXML
    private void ImportImage(){
        image = new ImageView(FilePath());
        image.setLayoutY(canvas.getLayoutY());
        image.setLayoutX(canvas.getLayoutX());
        Pane.getChildren().add(image);
        initialize();
    }

    //Método responsável por salvar o canvas
    @FXML
    public void save() {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("JPEG", "*.jpeg")
        );
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            WritableImage writableImage = canvas.snapshot(null, null);
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
                ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "jpg", file);
                ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "jpeg", file);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    //Método responsável por limpar o canvas
    @FXML
    public void clear() {
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    @FXML
    private void resolution (){
        panes.setOnMouseMoved(e -> {
           if(canvas.getHeight() <= Window.getWindows().get(0).getHeight()-145 && canvas.getWidth() <= Window.getWindows().get(0).getWidth()){
               canvas.setHeight(Window.getWindows().get(0).getHeight()-145);
               canvas.setWidth(Window.getWindows().get(0).getWidth());
               canvas.setLayoutY(50);
           }
        });
    }

    //Método responsável por dar zoom no canvas(Beta test)
    @FXML
    public void zoom() {
        canvas.setOnScroll(e -> {
            double scaleFactor = 1.05;
            if (e.getDeltaY() < 0) {
                scaleFactor = 1 / scaleFactor;
            }
            canvas.setScaleX((canvas.getScaleX()) * scaleFactor);
            canvas.setScaleY((canvas.getScaleY()) * scaleFactor);
        });
    }

    @FXML
    private void mandelbrot() {
        GraphicsContext coords = canvas.getGraphicsContext2D();
        double precision = Math.max((1 - (-2)) / 600, (1.2 - (-1.2)) / 400);
        int convergenceSteps = 50;
        for (double c = -2, xR = 0; xR < 600; c = c + precision, xR++) {
            for (double ci = -1.2, yR = 0; yR < 400; ci = ci + precision, yR++) {
                double convergenceValue = checkConvergence(ci, c, convergenceSteps);
                double t1 = convergenceValue / convergenceSteps;
                double c1 = Math.min(255 * 2 * t1, 255);
                double c2 = Math.max(255 * (2 * t1 - 1), 0);

                if (convergenceValue != convergenceSteps) {
                    coords.setFill(Color.color(c2 / 255.0, c1 / 255.0, c2 / 255.0));
                } else {
                    coords.setFill(Color.PURPLE);
                }
                coords.fillRect(xR, yR, 1, 1);
            }
        }
    }
    private int checkConvergence(double ci, double c, int convergenceSteps) {
        double z = 0;
        double zi = 0;
        for (int i = 0; i < convergenceSteps; i++) {
            double ziT = 2 * (z * zi);
            double zT = z * z - (zi * zi);
            z = zT + c;
            zi = ziT + ci;

            if (z * z + zi * zi >= 4.0) {
                return i;
            }
        }
        return convergenceSteps;
    }
}