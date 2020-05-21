package com.moyisuiying.view;

import com.moyisuiying.app.FontSettingSage;
import com.moyisuiying.util.Charsetutil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;


/**
 * @author 陌意随影
 * @create 2020-03-23 14:35
 * @desc 主面板
 **/
public class NodePadPane extends VBox {
    //头部菜单面板
    private MenuBar  menuBar = null;
    //文件菜单
    private Menu file_menu = null;
    //字体菜单
    private Menu font_menu = null;
    //顶部面板
    private HBox topPane = null;
    //主面板
    private Pane centerPane = null;
    private TextArea  textArea = null;
    private TabPane tabPnae = null;
    private Tab tab = null;
    private FileChooser fileChooser = null;
    private static  Map<String,TextArea> map  = new HashMap<String,TextArea>();
    private static  Map<String,String> filePathMap  = new HashMap<String,String>();
    private     Stage stage = null;
    public  NodePadPane(){
        this.menuBar = new MenuBar();
        this.file_menu  = new Menu("文件(F)");
        this.font_menu = new Menu("字体(C)");
        this.topPane = new HBox();
        this.centerPane = new HBox();
        this.textArea = new TextArea();
        this.tabPnae = new TabPane();
        initToPane();
        initCenterPane();
        topPane.setPrefHeight(30);
        tabPnae.setPrefHeight(50);
        initTabPane();
        this.getChildren().addAll(this.topPane,tabPnae,this.centerPane);
    }
    private  void initTabPane(){
   tabPnae.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
       @Override
       public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
           if (newValue!= null){
               if (map.containsKey(newValue.getText())){
                   centerPane.getChildren().clear();
                   centerPane.getChildren().add(map.get(newValue.getText()));
               }
           }
       }
   });

    }
    private  void createTabByName(String tabName){
        if (tabName == null || tabName.trim().length() == 0) return;
        ObservableList<Tab> tabs = this.tabPnae.getTabs();
        if (tabs != null && tabs.size() > 0){
            for (int i = 0 ;i< tabs.size();i++){
                String text = tabs.get(i).getText();
                if(text.equals(tabName)){
                    this.tabPnae.getSelectionModel().select(i);
                   return;
                }
            }
        }
        Tab tab = new Tab(tabName);
        tab.setOnCloseRequest(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
               if (filePathMap.containsKey(tab.getText())){
                   filePathMap.remove(tab.getText());
               }
               int index = 0;
               for (int i = 0;i < tabs.size();i++){
                   if (tabs.get(i).getText().equals(tab.getText())){
                       index = i;
                   }
               }
               if (index > 0){
                   tabPnae.getSelectionModel().select(index-1);
               }else{
                   Label label = new Label("请打开一个文件或新建一个文件");
                   label.setStyle("-fx-font-size: 20");
                   centerPane.getChildren().clear();
                   centerPane.getChildren().add(label);
               }
               if (map.containsKey(tab.getText())){
                   map.remove(tab.getText());
               }
            }
        });
        this.tabPnae.getTabs().add(tab);
        tabPnae.getSelectionModel().select(tab);
    }
    private void initCenterPane() {
        textArea.setPrefWidth(800);
        centerPane.setPadding(new Insets(0,1,1,1));
        centerPane.setPrefSize(800,550);
        Label label = new Label("请打开一个文件或新建一个文件");
        label.setStyle("-fx-font-size: 20");
        centerPane.getChildren().add(label);

    }

    private void initToPane() {
         this.topPane.spacingProperty().setValue(15);
        MenuItem item_newFile = new MenuItem("新建一个文件");
        item_newFile.setOnAction(e->{
            ObservableList<Tab> tabs = tabPnae.getTabs();
            String name ="";
             if (tabs == null || tabs.size() == 0){
                 name="new 1";
             }else{
                 int count = 0;
                 for (int i = 0;i < tabs.size();i++){
                     Tab tab1 = tabs.get(i);
                     if (tab1.getText().startsWith("new ")){
                         count ++;
                     }
                 }
                 name="new "+(count+1);
            }
            createTabByName(name);
            createTextArea(name,null);
        });
        MenuItem item_openFile =new MenuItem("打开一个文件");
        item_openFile.setOnAction(e->{
            fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("."));
            File file = fileChooser.showOpenDialog(null);
            if(file == null ){
                return;
            }
            String name = file.getName();
            filePathMap.put(name,file.getAbsolutePath());
            createTabByName(name);
            createTextArea(name,file);
        });
        MenuItem item_saveFile =new MenuItem("保存文件");
        item_saveFile.setOnAction(e->{
            Tab selectedItem = tabPnae.getSelectionModel().getSelectedItem();
            if (selectedItem == null){
                Alert alert  = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("您尚未有要保存的文件！");
                alert.show();
                return;
            }
            String fileName = selectedItem.getText();
            String path = null;
            boolean fla = false;
           if (filePathMap.containsKey(fileName)){
               path = filePathMap.get(fileName);
               path = path.substring(0,path.lastIndexOf("\\"));
               fla = true;
           }else{
               path=".";
           }

            fileChooser = new FileChooser();
           if (!fileName.contains(".")){
               fileName =fileName+".txt";
           }
            fileChooser.setInitialDirectory(new File(path));
            fileChooser.setInitialFileName(fileName);
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("All FILE", "*.*"),
                    new FileChooser.ExtensionFilter("txt", "*.txt")
            );
            File file = fileChooser.showSaveDialog(null);
            if(file == null ){
                return;
            }
            String name = file.getName();
            if (name == null || name.trim().length() == 0){
               return;
            }
            if (!fla){
                fileName = fileName.substring(0,fileName.lastIndexOf("."));
            }
            TextArea textArea = map.get(fileName);
            saveFile(textArea.getText(),file);

        });
        this.file_menu.getItems().addAll(item_newFile,item_openFile,item_saveFile);
        file_menu.setStyle("-fx-font-size: 16");
        font_menu.setStyle("-fx-font-size: 16");
        Menu item_color =new Menu("字体设置");
        item_color.setOnAction(event -> {
           if (stage == null){
               stage = new FontSettingSage(map);
               stage.show();
           }else{
               stage.show();
           }
        });
        this.font_menu.getItems().addAll(item_color);
        this.menuBar.getMenus().addAll(file_menu,this.font_menu);
        this.topPane.getChildren().add(menuBar);
    }

    private void saveFile(String textValue, File file) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file,false)) {
            byte[] bytes = textValue.getBytes();
            fileOutputStream.write(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createTextArea(String name, File file) {
        if (map.containsKey(name)){
            this.centerPane.getChildren().clear();
            this.centerPane.getChildren().add(map.get(name));
            return;
        }

        TextArea textArea = new TextArea();
        if (file != null){
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                byte[] buff = new byte[1024];
                int len = -1;
                StringBuilder sb = new StringBuilder();
                String filecharset = Charsetutil.getFilecharset(file);
                while ((len = fileInputStream.read(buff)) != -1){
                    sb.append(new String(buff,0,len,filecharset));
                }
                textArea.setText(sb.toString());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
         map.put(name,textArea);
        textArea.setPrefWidth(800);
        this.centerPane.getChildren().clear();
        this.centerPane.getChildren().add(textArea);
    }
}
