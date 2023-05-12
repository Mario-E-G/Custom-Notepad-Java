/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fx.notepad;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author Mario_Ehab
 */
public class Init extends Application {

    private BorderPane border_pane;
    private MenuBar menubar;
    private Menu file, edit, about;
    private MenuItem new_file, open, save, exit, undo, cut, copy, paste, delete, select_all, help;
    private TextArea text_area;
    private String copied_text = "";
    private String cutted_text = "";
    private String paste_text = "";
    private String current_text = "";

    private Scene scene;

    //Horizontal separator
    private SeparatorMenuItem fileSeparator;
    private SeparatorMenuItem editSeparator;

    @Override
    public void init() throws Exception {
        /*File Menu*/
        file = new Menu("File");
        new_file = new MenuItem("New");
        new_file.setAccelerator(KeyCombination.keyCombination("Shift+o"));
        open = new MenuItem("Open...");
        open.setAccelerator(KeyCombination.keyCombination("Shift+e"));
        save = new MenuItem("Save");
        save.setAccelerator(KeyCombination.keyCombination("Shift+l"));
        exit = new MenuItem("Exit");
        exit.setAccelerator(KeyCombination.keyCombination("esc"));

        fileSeparator = new SeparatorMenuItem();
        file.getItems().addAll(new_file, open, save, fileSeparator, exit);

        /*Edit Menu*/
        edit = new Menu("Edit");
        undo = new MenuItem("Undo");
        undo.setAccelerator(KeyCombination.keyCombination("Shift+m"));
        cut = new MenuItem("Cut");
        cut.setAccelerator(KeyCombination.keyCombination("Shift+n"));
        copy = new MenuItem("Copy");
        copy.setAccelerator(KeyCombination.keyCombination("Shift+p"));
        paste = new MenuItem("Paste");
        paste.setAccelerator(KeyCombination.keyCombination("Shift+v"));
        delete = new MenuItem("Delete");
        delete.setAccelerator(KeyCombination.keyCombination("Shift+d"));
        select_all = new MenuItem("Select all");
        select_all.setAccelerator(KeyCombination.keyCombination("Shift+a"));
        editSeparator = new SeparatorMenuItem();
        edit.getItems().addAll(undo, cut, copy, paste, delete, editSeparator, select_all);

        /*About Menu*/
        about = new Menu("About");
        help = new MenuItem("Help");
        about.getItems().addAll(help);

        /*Add Menus to MenuBar*/
        menubar = new MenuBar(file, edit, about);

        text_area = new TextArea();
        border_pane = new BorderPane();

        /*Add menubar & text_area to BorderPane*/
        border_pane.setTop(menubar);
        border_pane.setCenter(text_area);

//       help.setOnAction(new EventHandler<ActionEvent>(){
//            
//            @Override
//            public void handle(ActionEvent event) {
//                System.out.println("Help");
//            }
//            
//       });
        help.setOnAction((ActionEvent event) -> aboutInfo());

        exit.setOnAction((ActionEvent event) -> Platform.exit());
    }
    
    public void copyText(){
        cutted_text = "";
        copied_text = text_area.getSelectedText();
    }
    
    private void contentModefied(Stage primaryStage,String current_file){
        if(current_text != text_area.getText()){
            primaryStage.setTitle("*"+current_file);
        }
    }
    
    
    public void cutText(){
        copied_text = "";
        cutted_text = text_area.getSelectedText();
        text_area.replaceSelection("");
    }
    
    public void pasteText(){
        if("".equals(text_area.getSelectedText()) &&
                "".equals(cutted_text)){
            text_area.insertText(text_area.getCaretPosition(), copied_text);
        }
        else{
           text_area.insertText(text_area.getCaretPosition(), cutted_text);
        }
    }
    
    public void selectAll(){
        text_area.selectAll();
    }
    
    public void delete(){
        text_area.replaceSelection("");
    }
    
    public void undo(){
        text_area.undo();
    }
    
    public void newFile(Stage primaryStage) throws IOException{
        primaryStage.setTitle("Untitled - notepad");
        text_area.setText("");
        current_text = text_area.getText();
        contentModefied(primaryStage,"Untitled - notepad");
    }

    public void openFile(Stage primaryStage) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text", "*.txt"),
                new FileChooser.ExtensionFilter("Java", "*.java")
        );
        fileChooser.setTitle("Open file");
        // get the file selected
        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        if (selectedFile != null) {
             Alert a = new Alert(AlertType.NONE);

            a.setAlertType(AlertType.CONFIRMATION);
            a.setContentText("Are you want to open this file?");
            
            Optional<ButtonType> result = a.showAndWait();
            if(result.get()==ButtonType.OK){
                 try {
                     
                     BufferedReader b = new BufferedReader(
                             new FileReader( selectedFile.getPath()));
                     String s = "";
                     String line = "";

                     try {
                         while((line=b.readLine()) != null){
                             s += line+"\n";
                         }
                         text_area.setText(s);
                     } catch (IOException ex) {
                         Logger.getLogger(Init.class.getName()).log(Level.SEVERE, null, ex);
                     }
                     if(b != null){
                         b.close();
                     }
                 } catch (FileNotFoundException ex) {
                     Logger.getLogger(Init.class.getName()).log(Level.SEVERE, null, ex);
                 }
                System.out.println("Opened");
            }else{
                System.out.println("Do not open!!");
            }
            primaryStage.setTitle(selectedFile.getName());
            contentModefied(primaryStage,selectedFile.getName());
            System.out.println(selectedFile.getAbsolutePath() + "  selected");
        }
    }

    public void saveFile(Stage primaryStage) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text", "*.txt"),
                new FileChooser.ExtensionFilter("Java", "*.java")
        );
        fileChooser.setTitle("Save as");

        File selectedFile = fileChooser.showSaveDialog(primaryStage);

        if (selectedFile != null) {
            Alert a = new Alert(AlertType.NONE);

            a.setAlertType(AlertType.CONFIRMATION);
            a.setContentText("Are you want to save this file?");
            
            Optional<ButtonType> result = a.showAndWait();
            if(result.get()==ButtonType.OK){
                
                /*To Clear File Before Adding Text*/
//                FileWriter fw = new FileWriter(selectedFile, false);
//
//                PrintWriter pw = new PrintWriter(fw, false);
//
//                pw.flush();
//
//                pw.close();
//
//                fw.close();
//    
                FileWriter fileWriter = new FileWriter(selectedFile, true);

                fileWriter.write(text_area.getText());

                fileWriter.close();
                PrintWriter print = new PrintWriter(fileWriter);
                
                print.close();
                
                contentModefied(primaryStage,selectedFile.getName());
                System.out.println(selectedFile.getAbsolutePath() + "  selected");

            }else{
                System.out.println("Do not save!!");
            }
        }
    }

    private void aboutInfo() {
        Alert alert = new Alert(AlertType.NONE);

        alert.setAlertType(AlertType.INFORMATION);
        alert.setTitle("Information MessageBox");
        alert.setContentText("This is a INFORMATION " + "message for you!");
        alert.show();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        open.setOnAction((ActionEvent event) -> {
            try {
                openFile(primaryStage);
            } catch (IOException ex) {
                Logger.getLogger(Init.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        save.setOnAction((ActionEvent event) -> {
            try {
                saveFile(primaryStage);
            } catch (IOException ex) {
                Logger.getLogger(Init.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        new_file.setOnAction((ActionEvent event)->{
            try {
                newFile(primaryStage);
            } catch (IOException ex) {
                Logger.getLogger(Init.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        copy.setOnAction((ActionEvent event)->{
            copyText();
        });
        
        cut.setOnAction((ActionEvent event)->{
            cutText();
        });
        
        paste.setOnAction((ActionEvent event)->{
            pasteText();
        });
        
        select_all.setOnAction((ActionEvent event)->{
            selectAll();
        });
        
        undo.setOnAction((ActionEvent event)->{
            undo();
        });
        
        delete.setOnAction((ActionEvent event)->{
            delete();
        });
        
        
        scene = new Scene(border_pane, 800, 800);
        primaryStage.setTitle("Notepad-GUI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
