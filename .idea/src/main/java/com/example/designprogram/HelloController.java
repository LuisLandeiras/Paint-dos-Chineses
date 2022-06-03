package com.example.designprogram;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
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
public class HelloController {
    @FXML
    private Canvas canvas;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private ToggleButton eraser;
    @FXML
    private Spinner pincel;
    @FXML
    private Slider tamfiguras, grofiguras;
    @FXML
    private CheckBox triangulo, quadrado, circulo, mandelbrot;
    @FXML
    private TextField text1, text;
    @FXML
    private ImageView image = new ImageView();
    @FXML
    private AnchorPane panes, pane;
    private double reMin = -2, reMax = 1 , imMin = -1.2, imMax = 1.2;

    ArrayList<Cordenadas> MoveFractal = new ArrayList<>();
    ArrayList <Cordenadas> ImageMove = new ArrayList<>(); //Array das coordenadas dos pontos para mover a Imagem

    private double distpontos (Cordenadas a, Cordenadas b){
        double x = Math.pow((a.getX() - b.getX()), 2);
        double y = Math.pow((a.getY() - b.getY()), 2);
        return Math.sqrt(x + y);
    }

    /*private double modulo (double x){
        if (x > 0){
            return x;
        } else {
            return -x;
        }
    }

    private int modulo2(double x, double y) {
        if(x - y > 0){
            return (int) (x - y);
        }   else {
            return (int) (y - x);
        }
    }*/

    //Método responsável pelas alterações do canvas
    @FXML
    public void initialize() {
        Circle c = new Circle();
        Rectangle r = new Rectangle();
        GraphicsContext coords = canvas.getGraphicsContext2D();
        canvas.setOnMouseDragged(e -> {
            if (mandelbrot.isSelected()) {
                Cordenadas cordenadas = new Cordenadas(e.getX(), e.getY());
                MoveFractal.add(cordenadas);
                if (MoveFractal.size() > 1) {
                    imMin = imMin + ((MoveFractal.get(0).getY() - MoveFractal.get(MoveFractal.size() - 1).getY()) / 20000);
                    imMax = imMax + ((MoveFractal.get(0).getY() - MoveFractal.get(MoveFractal.size() - 1).getY()) / 20000);
                    reMin = reMin + ((MoveFractal.get(0).getX() - MoveFractal.get(MoveFractal.size() - 1).getX()) / 20000);
                    reMax = reMax + ((MoveFractal.get(0).getX() - MoveFractal.get(MoveFractal.size() - 1).getX()) / 20000);
                    mandelbrot();
                }
            } else if (e.getButton() == MouseButton.PRIMARY) {
                double size = Double.parseDouble(String.valueOf(pincel.getValue()));
                double x = e.getX() - size / 2;
                double y = e.getY() - size / 2;
                if (eraser.isSelected()) {
                    coords.clearRect(x, y, size, size);
                    quadrado.setSelected(false);
                    circulo.setSelected(false);
                    triangulo.setSelected(false);
                } else if(triangulo.isSelected() == false & quadrado.isSelected() == false & circulo.isSelected() == false){
                    coords.setFill(colorPicker.getValue());
                    coords.fillRect(x, y, size, size);
                }
            }
        });

        canvas.setOnMouseMoved(event -> {
            MoveFractal.clear();
        });

        //Método responsável pelo movimento da imagem
        image.setOnMouseDragged(e -> {
            double size = tamfiguras.getValue();
            double x = e.getX() - size / 2;
            double y = e.getY() - size / 2;
            ImageMove.add(new Cordenadas(x, y));
            image.setTranslateX(image.getTranslateX() - (ImageMove.get(0).getX() - ImageMove.get(ImageMove.size() - 1).getX()) / 2);
            image.setTranslateY(image.getTranslateY() - (ImageMove.get(0).getY() - ImageMove.get(ImageMove.size() - 1).getY()) / 2);
        });

        //Responsável por limpar o arrray de movimento da imagem
        image.setOnMouseMoved(event -> {
            ImageMove.clear();
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
            }
        });
    }
    //Método responsavel por mostrar o tamanho das figuras
    @FXML
    public void tamanhofig() {
        tamfiguras.setMax(1000);
        tamfiguras.setMin(1);
        double size = Double.parseDouble(String.valueOf(tamfiguras.getValue()));
        int resultado = (int) size;

        text.setText(String.valueOf(resultado));

    }

    @FXML
    public void tamanhotext(){
        tamfiguras.setValue(Integer.parseInt(String.valueOf(text.getText())));
    }

    //Método responsavel por mostrar a grossura das figuras
    @FXML
    public void grossurafig(){
        grofiguras.setMax(100);
        grofiguras.setMin(1);
        double size = Double.parseDouble(String.valueOf(grofiguras.getValue()));
        int resultado2 = (int) size;
        text1.setText(String.valueOf(resultado2));
    }

    @FXML
    public void grossuratext(){
        grofiguras.setValue(Integer.parseInt(String.valueOf(text1.getText())));
    }
    @FXML
    public void tamanho(){
        pincel.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100));
    }


    //Devolve o caminho do ficheiro
    private String FilePath(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir Ficheiro");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPEG", "*.jpg"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg")
        );
        File file = fileChooser.showOpenDialog(pane.getScene().getWindow());
        return file.getAbsolutePath();
    }

    //Importa a imagem
    @FXML
    private void ImportImage(){
        image = new ImageView(FilePath());
        image.setLayoutY(canvas.getLayoutY());
        image.setLayoutX(canvas.getLayoutX());
        pane.getChildren().add(image);
        initialize();
    }

    //Método responsável por salvar o canvas
    @FXML
    public void save() {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar Imagem");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("JPEG", "*.jpeg")
        );
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            WritableImage writableImage = pane.snapshot(null, null);
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
        pane.getChildren().remove(image);
    }

    @FXML
    private void resolution (){
        panes.setOnMouseMoved(e -> {
            if(canvas.getHeight() <= Window.getWindows().get(0).getHeight() && canvas.getWidth() <= Window.getWindows().get(0).getWidth()){
                canvas.setHeight(Window.getWindows().get(0).getHeight());
                canvas.setWidth(Window.getWindows().get(0).getWidth());
                canvas.setLayoutY(0);
            }
        });
    }

    //Método responsável por dar zoom no canvas
    @FXML
    public void zoom() {
        if(mandelbrot.isSelected() == false){
            canvas.setOnScroll(e -> {
                double scaleFactor = 1.05;
                if (e.getDeltaY() < 0) {
                    scaleFactor = 1 / scaleFactor;
                }
                canvas.setScaleX((canvas.getScaleX()) * scaleFactor);
                canvas.setScaleY((canvas.getScaleY()) * scaleFactor);
            });
        }   else {
            canvas.setOnScroll(e -> {
                if(e.getDeltaY() > 0) {
                    imMax = imMax - imMax / 20;
                    reMin = reMin - reMin / 20;
                    reMax = reMax - reMax / 20;
                    imMin = imMin - imMin / 20;
                    mandelbrot();
                } else {
                    imMax = imMax + imMax / 20;
                    reMin = reMin + reMin / 20;
                    reMax = reMax + reMax / 20;
                    imMin = imMin + imMin / 20;
                    mandelbrot();
                }
            });
        }
    }

    @FXML
    private void mandelbrot(){
        double precision = Math.max((reMax - reMin) / canvas.getWidth(), (imMax - imMin) / canvas.getHeight());
        int convergenceSteps = 100;
        for (double c = reMin, xR = 0; xR < canvas.getWidth(); c = c + precision, xR++) {
            for (double ci = imMin, yR = 0; yR < canvas.getHeight(); ci = ci + precision, yR++) {
                double convergenceValue = checkConvergence(ci, c, convergenceSteps);
                double t1 = (double) convergenceValue / convergenceSteps;
                double c1 = Math.min(255 * 2 * t1, 255);
                double c2 = Math.max(255 * (2 * t1 - 1), 0);

                if (convergenceValue != convergenceSteps) {
                    canvas.getGraphicsContext2D().setFill(Color.color(c2 / 255, c1 / 255, c2 / 255));
                } else {
                    canvas.getGraphicsContext2D().setFill(Color.PURPLE); // Convergence Color
                }
                canvas.getGraphicsContext2D().fillRect(xR, yR, 1, 1);
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