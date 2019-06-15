package frontend;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class Controller {
	
	   @FXML
	    private ListView<String> lawListView;
	   
	    @FXML
	    private TextField searchTextField;

	    @FXML
	    private BorderPane root;
	    
	    @FXML
	    private ToolBar toolBar;
	   
	    @FXML
	    private HBox placeholderHBox;
	    
	    @FXML
	    private Button searchButton;
	    
	    @FXML
	    private Button deleteButton;

	    @FXML
	    private Button checkButton;

	    @FXML
	    void checkButtonAction(MouseEvent event) {
	    	
	    }

	    @FXML
	    void deleteButtonAction(MouseEvent event) {

	    }
	    
	    @FXML
	    void lawListViewAction(MouseEvent event) {
	    	if(lawListView.getSelectionModel().getSelectedItem() != null) {
	    		checkButton.setDisable(false);
	    	}
	    }

	    @FXML
	    void searchButtonAction(MouseEvent event) {
	    	int lawSize = lawListView.getItems().size();
	    	MultipleSelectionModel<String> model = lawListView.getSelectionModel();
	    	
	    	LinkedList<String> getMatches = new LinkedList<String>();
	    	
	    	if(!searchTextField.getText().isEmpty() || searchTextField.getText() != null) {
	    		getMatches.clear();
	    		for(int i = 0; i < lawSize; i++) {
	    			if(lawListView.getItems().get(i).toLowerCase().contains(searchTextField.getText().toLowerCase())) {
	    				getMatches.add(lawListView.getItems().get(i));
	    			}
	    		}
	    		
	    		if(model.getSelectedItem() != null && model.getSelectedIndex() != getMatches.size()-1) {
	    			
	    			for(int i = model.getSelectedIndex()+1; i < lawListView.getItems().size(); i++) {
		    			if(lawListView.getItems().get(i).toLowerCase().contains(searchTextField.getText().toLowerCase())) {
		    				model.select(i);
		    				lawListView.scrollTo(i);
		    				break;
		    			}
		    		}	
    			} else {
    				for(int i = 0; i < lawSize; i++) {
    	    			if(lawListView.getItems().get(i).toLowerCase().contains(searchTextField.getText().toLowerCase())) {
    	    				model.select(i);
    	    				lawListView.scrollTo(i);
    	    				break;
    	    			}
    	    		}
    			}
	    	}
	    }

	    @FXML
	    void initialize() {
	        assert root != null : "fx:id=\"root\" was not injected: check your FXML file 'View.fxml'.";
	        assert lawListView != null : "fx:id=\"lawListView\" was not injected: check your FXML file 'View.fxml'.";
	        assert toolBar != null : "fx:id=\"toolBar\" was not injected: check your FXML file 'View.fxml'.";
	        assert searchTextField != null : "fx:id=\"searchTextField\" was not injected: check your FXML file 'View.fxml'.";
	        assert searchButton != null : "fx:id=\"searchButton\" was not injected: check your FXML file 'View.fxml'.";
	        assert placeholderHBox != null : "fx:id=\"placeholderHBox\" was not injected: check your FXML file 'View.fxml'.";
	        assert checkButton != null : "fx:id=\"checkButton\" was not injected: check your FXML file 'View.fxml'.";
	        assert deleteButton != null : "fx:id=\"deleteButton\" was not injected: check your FXML file 'View.fxml'.";
	        
	        setItems();
	    }
	    
	    private void setItems() {
	    	 ObservableList<String> items = FXCollections.observableArrayList();
	    	 
	    	 ObjectMapper mapper = new ObjectMapper();

	    	 try {
				Root r = mapper.readValue(new File("/Users/Mitja/Desktop/test.json"), Root.class);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
	    	 
		     lawListView.setItems(items);
	    }
	    
	    private String readJSON() throws IOException {
	    	BufferedReader br = new BufferedReader(new FileReader("/Users/Mitja/Desktop/test.json"));
	    	try {
	    	    String newString = "";
	    	    String line = br.readLine();

	    	    while (line != null) {
	    	        newString += line + "\n";
	    	        line = br.readLine();
	    	    }
	    	    return newString;
	    	} finally {
	    	    br.close();
	    	}
	    }
}
