package com.example.designprogram;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class HelloController {
    @FXML
    private Canvas canvas;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private CheckBox eraser;
    @FXML
    private CheckBox balde;
    @FXML
    private Spinner pincel;
    @FXML
    private Slider tamfiguras;
    @FXML
    private ToggleButton triangulo;
    @FXML
    private ToggleButton quadrado;
    @FXML
    private ToggleButton circulo;
    @FXML
    private Label tamanho;
    @FXML
    public void initialize(){
        GraphicsContext coords= canvas.getGraphicsContext2D();
        pincel.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,100));
        canvas.setOnMouseDragged(e -> {
            double size = Double.parseDouble(String.valueOf(pincel.getValue()));
            double x = e.getX() - size/2;
            double y = e.getY() - size/2;

            if (eraser.isSelected()){
                coords.clearRect(x,y,size,size);
                quadrado.setSelected(false);
                circulo.setSelected(false);
                balde.setSelected(false);
            } else{
                coords.setFill(colorPicker.getValue());
                coords.fillRect(x,y,size,size);
            }
        });
    }
    @FXML
    public void figures(){
        Circle c = new Circle();
        Rectangle r = new Rectangle();
        GraphicsContext coords = canvas.getGraphicsContext2D();
        canvas.setOnMouseClicked(e -> {
            double size = Double.parseDouble(String.valueOf(tamfiguras.getValue()));
            double x = e.getX() - size/2;
            double y = e.getY() - size/2;
            if(circulo.isSelected()){
                c.setCenterX(x);
                c.setCenterY(y);
                c.setRadius(size/2);
                coords.setStroke(colorPicker.getValue());
                coords.strokeOval(x,y,size,size);
            }
            if (quadrado.isSelected()){
                r.setX(x);
                r.setY(y);
                r.setHeight(size);
                r.setHeight(size);
                coords.setStroke(colorPicker.getValue());
                coords.strokeRect(x,y,size,size);
            }
            if(balde.isSelected()){
                coords.setFill(colorPicker.getValue());
                coords.fillRect(x,y,canvas.getWidth(),canvas.getHeight());
            }
        });
    }
    @FXML
    public void tamanho(){
        tamfiguras.setMax(1000);
        tamfiguras.setMin(0);
        tamanho.setText(String.valueOf(tamfiguras.getValue()));
    }
}