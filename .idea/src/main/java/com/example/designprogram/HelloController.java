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
    private CheckBox triangulo, quadrado, circulo, line, mandelbrot, pincel1, pincel2;
    @FXML
    private TextField text1, text;
    @FXML
    private AnchorPane panes, pane;
    @FXML
    private ImageView image = new ImageView();
    private double reMin = -2, reMax = 1 , imMin = -1.2, imMax = 1.2;
    ArrayList<Coordenadas> MoveFractal = new ArrayList<>(); //Array das coordenadas do fractal
    ArrayList <Coordenadas> ImageMove = new ArrayList<>(); //Array das coordenadas dos pontos para mover a Imagem
    int click = 0;

    //Método responsável pelas alterações do canvas
    @FXML
    public void initialize() {
        pincel1.setSelected(true);
        Coordenadas c1 = new Coordenadas();
        Coordenadas c2 = new Coordenadas();
        pincel.setEditable(true);
        pincel.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
        GraphicsContext coords = canvas.getGraphicsContext2D();
        canvas.setOnMouseDragged(e -> {
            if (mandelbrot.isSelected()) {
                Coordenadas coordenadas = new Coordenadas(e.getX(), e.getY());
                MoveFractal.add(coordenadas);
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
                if (eraser.isSelected()) { //Desativa tudo para poder apagar sem bugs
                    coords.clearRect(x, y, size, size);
                    quadrado.setSelected(false);
                    circulo.setSelected(false);
                    triangulo.setSelected(false);
                    line.setSelected(false);
                    pincel1.setSelected(false);
                    pincel2.setSelected(false);
                } else if (!triangulo.isSelected() & !quadrado.isSelected() & !circulo.isSelected()) {
                    if(pincel1.isSelected()) {
                        coords.setFill(colorPicker.getValue());
                        coords.fillRect(x, y, size, size);
                    }
                    if(pincel2.isSelected()) {
                        coords.setFill(colorPicker.getValue());
                        coords.fillOval(x, y, size, size);
                    }
                }
            }
        });

        //Evento usado para limpar a arraylist de coordenadas do fractal
        canvas.setOnMouseMoved(event -> {
            MoveFractal.clear();
        });

        //Evento responsável pelo movimento da imagem
        image.setOnMouseDragged(e -> {
            double size = tamfiguras.getValue();
            double x = e.getX() - size / 2;
            double y = e.getY() - size / 2;
            ImageMove.add(new Coordenadas(x, y));
            image.setTranslateX(image.getTranslateX() - (ImageMove.get(0).getX() - ImageMove.get(ImageMove.size() - 1).getX()) / 2);
            image.setTranslateY(image.getTranslateY() - (ImageMove.get(0).getY() - ImageMove.get(ImageMove.size() - 1).getY()) / 2);
        });

        //Evento responsável por limpar o array de movimento da imagem
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
                    coords.setStroke(colorPicker.getValue());
                    coords.strokeOval(x, y, size, size);
                }
                if (quadrado.isSelected()) {
                    coords.setStroke(colorPicker.getValue());
                    coords.strokeRect(x, y, size, size);
                }
                if (triangulo.isSelected()) {
                    double[] xPoints = {x, x + size, x - size};
                    double[] yPoints = {y, y + size, y + size};
                    coords.setStroke(colorPicker.getValue());
                    coords.strokePolygon(xPoints, yPoints, 3);
                }
            }else if (e.getButton() == MouseButton.SECONDARY) {
                double grossura = Double.parseDouble(String.valueOf(grofiguras.getValue()));
                coords.setLineWidth(grossura);

                if (circulo.isSelected()){
                    click++;
                    if (click == 1) {
                        c1.setX(e.getX());
                        c1.setY(e.getY());
                    } else if(click == 2){
                        double x1 = e.getX();
                        double y1 = e.getY();
                        coords.setStroke(colorPicker.getValue());
                        coords.strokeOval(menor(c1.getX(), x1), menor(c1.getY(), y1), distanciapontos(c1, new Coordenadas(x1, y1)).getX(), distanciapontos(c1, new Coordenadas(x1, y1)).getY());
                        click = 0;
                    }
                }
                if (triangulo.isSelected()){
                    click++;
                    if (click == 1) {
                        c1.setX(e.getX());
                        c1.setY(e.getY());
                    } else if(click == 2){
                        c2.setX(e.getX());
                        c2.setY(e.getY());
                    } else{
                        double x = e.getX();
                        double y = e.getY();
                        coords .setStroke(colorPicker.getValue());
                        coords.strokePolygon(new double[]{c1.getX(), c2.getX(), x}, new double[]{c1.getY(), c2.getY(), y}, 3);
                        click = 0;
                    }
                }
                if (quadrado.isSelected()){
                    click++;
                    if (click == 1) {
                        c1.setX(e.getX());
                        c1.setY(e.getY());
                    } else {
                        double x1 = e.getX();
                        double y1 = e.getY();
                        coords.setStroke(colorPicker.getValue());
                        coords.strokeRect( menor(c1.getX(), x1), menor(c1.getY(), y1), distanciapontos(c1, new Coordenadas(x1, y1)).getX(), distanciapontos(c1, new Coordenadas(x1, y1)).getY());
                        click = 0;
                    }
                }
                if(line.isSelected()){
                    click++;
                    if (click == 1) {
                        c1.setX(e.getX());
                        c1.setY(e.getY());
                    } else {
                        double x1 = e.getX();
                        double y1 = e.getY();
                        coords.setStroke(colorPicker.getValue());
                        coords.strokeLine(c1.getX(), c1.getY(), x1, y1);
                        click = 0;
                    }
                }
            }
        });
    }

    //Métodos usados para desativar funcinalidade de desenho quando uma estiver a ser usada
    @FXML
    private void qtrue() {
        circulo.setSelected(false);
        triangulo.setSelected(false);
        line.setSelected(false);
    }
    @FXML
    private void ctrue() {
        quadrado.setSelected(false);
        triangulo.setSelected(false);
        line.setSelected(false);
    }
    @FXML
    private void ttrue() {
        quadrado.setSelected(false);
        circulo.setSelected(false);
        line.setSelected(false);
    }
    @FXML
    private void ltrue() {
        quadrado.setSelected(false);
        circulo.setSelected(false);
        triangulo.setSelected(false);
    }
    @FXML
    private void pincel1true(){
        pincel2.setSelected(false);
        quadrado.setSelected(false);
        circulo.setSelected(false);
        triangulo.setSelected(false);
    }
    @FXML
    private void pincel2true(){
        pincel1.setSelected(false);
        quadrado.setSelected(false);
        circulo.setSelected(false);
        triangulo.setSelected(false);
    }
    //---------------------------------------------------------------------------------------------------------------------

    //Métodos responsáveis por aumentar o tamanho das figuras e do pincel/borracha
    @FXML
    public void tamanhofig() {
        tamfiguras.setMax(1000);
        tamfiguras.setMin(0);
        double size = Double.parseDouble(String.valueOf(tamfiguras.getValue()));
        int resultado = (int) size;

        text.setText(String.valueOf(resultado));

    }
    @FXML
    public void tamanhotext(){
        tamfiguras.setValue(Integer.parseInt(String.valueOf(text.getText())));
    }
    @FXML
    public void grossurafig(){
        grofiguras.setMax(100);
        grofiguras.setMin(0);
        double size = Double.parseDouble(String.valueOf(grofiguras.getValue()));
        int resultado2 = (int) size;
        text1.setText(String.valueOf(resultado2));
    }
    @FXML
    public void grossuratext(){
        grofiguras.setValue(Integer.parseInt(String.valueOf(text1.getText())));
    }
    //---------------------------------------------------------------------------------------------------------------------

    //Métodos responáveis por importar imagens e salvar o canvas como imagem
    @FXML
    private void ImportImage(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir Ficheiro");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPEG", "*.jpg"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg")
        );
        File file = fileChooser.showOpenDialog(pane.getScene().getWindow());
        image = new ImageView(file.getAbsolutePath());
        image.setLayoutY(canvas.getLayoutY());
        image.setLayoutX(canvas.getLayoutX());
        pane.getChildren().add(image);
        initialize();
    }
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
    //---------------------------------------------------------------------------------------------------------------------

    //Método responsável por limpar o canvas
    @FXML
    public void clear() {
        pane.getChildren().remove(image);
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.setScaleX(1);
        canvas.setScaleY(1);
    }

    //Método responsável por redimensionar o programa
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

    //Método responsável por dar zoom no canvas e zoom no fractal
    @FXML
    public void zoom() {
        if(!mandelbrot.isSelected()){
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

    //Método responsável por desenhar o fractal
    @FXML
    private void mandelbrot(){
        double precision = Math.max((reMax - reMin) / canvas.getWidth(), (imMax - imMin) / canvas.getHeight());
        int convergenceSteps = 100;
        for (double c = reMin, xR = 0; xR < canvas.getWidth(); c = c + precision, xR++) {
            for (double ci = imMin, yR = 0; yR < canvas.getHeight(); ci = ci + precision, yR++) {
                double convergenceValue = checkConvergence(ci, c, convergenceSteps);
                double t1 = convergenceValue / convergenceSteps;
                double c1 = Math.min(255 * 2 * t1, 255);
                double c2 = Math.max(255 * (2 * t1 - 1), 0);

                if (convergenceValue != convergenceSteps){
                    canvas.getGraphicsContext2D().setFill(Color.color(c2 / 255, c1 / 255, c2 / 255));
                } else{
                    canvas.getGraphicsContext2D().setFill(Color.BLACK); // Convergence Color
                }
                canvas.getGraphicsContext2D().fillRect(xR, yR, 1, 1);
            }
        }
    }

    //Método responsável por calcular a formula de mandelbrot
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

    //Métodos de suporte para as coordenadas
    private Coordenadas distanciapontos(Coordenadas a, Coordenadas b){ //Calcula a distância entre dois pontos
        Coordenadas c = new Coordenadas();
        c.setX(modulo(a.getX() - b.getX()));
        c.setY(modulo(a.getY() - b.getY()));
        return c;
    }
    private double modulo(double x){
        if (x > 0){
            return x;
        } else {
            return -x;
        }
    }
    private double menor(double a, double b){
        if (a < b){
            return a;
        } else {
            return b;
        }
    }
}