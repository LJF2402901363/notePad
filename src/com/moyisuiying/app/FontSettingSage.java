package com.moyisuiying.app;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.Map;
/**
 * @author 陌意随影
 * @create 2020-03-26 13:14
 * @desc 字体设置面板
 **/
public class FontSettingSage  extends Stage {
    private   Map<String,TextArea> map = null;
    private VBox box = null;
    private ColorPicker colorPicker = null;
    private ComboBox<Integer> comboBox = null;
    private Scene scene = null;
    private Button btn_ok = null;
    private HBox btnBox = null;
    public FontSettingSage( Map<String,TextArea> map){
        this.map = map;
        this.box = new VBox();
        this.colorPicker = new ColorPicker(Color.web("#1A1A1A"));
        this.comboBox = new ComboBox<>();
        this.btn_ok = new Button("确定");
        this.btnBox = new HBox();
        installComponents();
        this.scene = new Scene(box,400,400);
        this.setScene(scene);
    }

    private void installComponents() {
        this.comboBox.setEditable(true);
        this.comboBox.setValue(14);
        for (int i = 1;i < 60;i++){
            this.comboBox.getItems().add(i);
        }
        this.btn_ok.setOnAction(e->{
            String hex = Integer.toHexString(colorPicker.getValue().hashCode());
            String str = "-fx-text-fill:#"+hex;
                for (Map.Entry<String,TextArea> entry:map.entrySet()){
                    entry.getValue().setStyle("-fx-font-size: "+comboBox.getSelectionModel().getSelectedItem()+";"+str);
            }
                FontSettingSage.this.hide();
        });

        this.btnBox.getChildren().addAll(btn_ok);
        Label label_size = new Label("字体大小:");
        label_size.setStyle("-fx-font-size: 16;");
        Label label_color = new Label("字体颜色");
        label_color.setStyle("-fx-font-size: 16;");
        HBox fontSizeBox = new HBox();
        fontSizeBox.getChildren().addAll(label_size,comboBox);
        HBox fontColorBox = new HBox();
        fontColorBox.getChildren().addAll(label_color,colorPicker);
       this.box.getChildren().addAll(fontSizeBox,fontColorBox,btnBox);
    }


}
