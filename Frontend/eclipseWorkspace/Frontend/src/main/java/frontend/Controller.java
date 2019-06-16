package frontend;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class Controller {
	
		@FXML
		private TextField searchTextField;

		@FXML
		private Label titleLabel;
	
		@FXML
    	private Button searchButton;
		
		@FXML
    	private Button backButton;

		@FXML
		private Pane placeholderPane;

		@FXML
		private Button deleteButton;
		
	    @FXML
	    private StackPane listViewStackPane;

	    @FXML
	    private StackPane referStackPane;
	    
	    @FXML
	    private StackPane statuesStackPane;

    	@FXML
    	private ListView<String> statuesListView;

    	@FXML
    	private ListView<String> paragraphsListView;
    
    	@FXML
    	private ListView<String> referListView; // Vorübergehend

    	private int listViewStatus = 0;
    	private Root r;
    	private String json;
    	private Paragraphs p;
    	
    	@FXML
        void backButtonAction(MouseEvent event) {
    		if(listViewStatus == 1) {
    			listViewStatus = 0;
    			statuesStackPane.toFront();
    			statuesListView.setDisable(false);
    			
    			paragraphsListView.toBack();
    			paragraphsListView.setDisable(true);
    			
    			titleLabel.setText("Gesetzbücher");
    			backButton.setDisable(true);
    			
    		} else if(listViewStatus == 2) {
    			listViewStatus = 1;
    			paragraphsListView.toFront();
    			paragraphsListView.setDisable(false);
    			
    			statuesStackPane.toBack();
    			statuesListView.setDisable(true);
    		}
        }

	    @FXML
	    void deleteButtonAction(MouseEvent event) {
	    	deleteButton.setDisable(true);
	    }
	    
	    @FXML
	    void statuesListViewAction(MouseEvent event) {
	    	if(statuesListView.getSelectionModel().getSelectedItem() != null) {
	    		deleteButton.setDisable(false);
	    		if(event.getButton().equals(MouseButton.PRIMARY)) {
	    			if(event.getClickCount() == 2) {
	    				listViewStatus = 1;
	    				backButton.setDisable(false);
	    				
	    				paragraphsListView.toFront();
	        			paragraphsListView.setDisable(false);
	    				
	    				ObservableList<String> paragraphItems = FXCollections.observableArrayList();
	    				
	    				paragraphItems = getParagraphs();
	    				
	    				paragraphsListView.setItems(paragraphItems);
	    				paragraphsListView.setVisible(true);
	    				
	    				statuesListView.setDisable(true);
	    				statuesListView.toBack();
	    				statuesStackPane.toBack();
	    			}
	    		}
	    	}
	    }
	    
	    @FXML
	    void paragraphsListViewsubparagraphsListView(MouseEvent event) {
	    	if(paragraphsListView.getSelectionModel().getSelectedItem() != null) {
	    		deleteButton.setDisable(false);
	    		if(event.getButton().equals(MouseButton.PRIMARY)) {
	    			if(event.getClickCount() == 2) {
	    				listViewStatus = 2;
	    				backButton.setDisable(false);
	    				
	    				referListView.toFront();
	    				referListView.setDisable(false);
	    				
	    				ObservableList<String> referItems = FXCollections.observableArrayList();
	    				
	    				referItems = getRefers();
	    				
	    				referListView.setItems(referItems);
	    				referListView.setVisible(true);
	    				
	    				paragraphsListView.setDisable(true);
	    				paragraphsListView.toBack();
	    				listViewStackPane.toBack();
	    			}
	    		}
	    	}
	    }
	    
	    @FXML
	    void referListViewAction(MouseEvent event) {

	    }
	    
	    @FXML
	    void searchButtonAction(MouseEvent event) {
	    	if(listViewStatus == 0) {
	    		int lawSize = statuesListView.getItems().size();
		    	MultipleSelectionModel<String> model = statuesListView.getSelectionModel();
		    	
		    	LinkedList<String> getMatches = new LinkedList<String>();

		    	if(!searchTextField.getText().isEmpty() || searchTextField.getText() != null) {

		    		getMatches.clear();
		    		for(int i = 0; i < lawSize; i++) {
		    			if(statuesListView.getItems().get(i).toLowerCase().contains(searchTextField.getText().toLowerCase())) {
		    				getMatches.add(statuesListView.getItems().get(i));
		    			}
		    		}
		    		
		    		if(model.getSelectedItem() != null && model.getSelectedIndex() != getMatches.size()-1) {

		    			for(int i = model.getSelectedIndex()+1; i < statuesListView.getItems().size(); i++) {
			    			if(statuesListView.getItems().get(i).toLowerCase().contains(searchTextField.getText().toLowerCase())) {
			    				model.select(i);
			    				statuesListView.scrollTo(i);
			    				break;
			    			}
			    		}	
	    			} else {

	    				for(int i = 0; i < lawSize; i++) {
	    	    			if(statuesListView.getItems().get(i).toLowerCase().contains(searchTextField.getText().toLowerCase())) {
	    	    				model.select(i);
	    	    				statuesListView.scrollTo(i);
	    	    				break;
	    	    			}
	    	    		}
	    			}
		    	}
	    	} else if(listViewStatus == 1) {
	    		int pargraphsSize = paragraphsListView.getItems().size();
		    	MultipleSelectionModel<String> model = paragraphsListView.getSelectionModel();
		    	
		    	LinkedList<String> getMatches = new LinkedList<String>();

		    	if(!searchTextField.getText().isEmpty() || searchTextField.getText() != null) {

		    		getMatches.clear();
		    		for(int i = 0; i < pargraphsSize; i++) {
		    			if(paragraphsListView.getItems().get(i).toLowerCase().contains(searchTextField.getText().toLowerCase())) {
		    				getMatches.add(paragraphsListView.getItems().get(i));
		    			}
		    		}
		    		
		    		if(model.getSelectedItem() != null && model.getSelectedIndex() != getMatches.size()-1) {

		    			for(int i = model.getSelectedIndex()+1; i < paragraphsListView.getItems().size(); i++) {
			    			if(paragraphsListView.getItems().get(i).toLowerCase().contains(searchTextField.getText().toLowerCase())) {
			    				model.select(i);
			    				paragraphsListView.scrollTo(i);
			    				break;
			    			}
			    		}	
	    			} else {

	    				for(int i = 0; i < pargraphsSize; i++) {
	    	    			if(paragraphsListView.getItems().get(i).toLowerCase().contains(searchTextField.getText().toLowerCase())) {
	    	    				model.select(i);
	    	    				paragraphsListView.scrollTo(i);
	    	    				break;
	    	    			}
	    	    		}
	    			}
		    	}
	    	} else if(listViewStatus == 2) {
	    		int referSize = referListView.getItems().size();
		    	MultipleSelectionModel<String> model = referListView.getSelectionModel();
		    	
		    	LinkedList<String> getMatches = new LinkedList<String>();

		    	if(!searchTextField.getText().isEmpty() || searchTextField.getText() != null) {

		    		getMatches.clear();
		    		for(int i = 0; i < referSize; i++) {
		    			if(referListView.getItems().get(i).toLowerCase().contains(searchTextField.getText().toLowerCase())) {
		    				getMatches.add(referListView.getItems().get(i));
		    			}
		    		}
		    		
		    		if(model.getSelectedItem() != null && model.getSelectedIndex() != getMatches.size()-1) {

		    			for(int i = model.getSelectedIndex()+1; i < referListView.getItems().size(); i++) {
			    			if(referListView.getItems().get(i).toLowerCase().contains(searchTextField.getText().toLowerCase())) {
			    				model.select(i);
			    				referListView.scrollTo(i);
			    				break;
			    			}
			    		}	
	    			} else {

	    				for(int i = 0; i < referSize; i++) {
	    	    			if(referListView.getItems().get(i).toLowerCase().contains(searchTextField.getText().toLowerCase())) {
	    	    				model.select(i);
	    	    				referListView.scrollTo(i);
	    	    				break;
	    	    			}
	    	    		}
	    			}
		    	}
	    	}
	    }


	    @FXML
	    void initialize() {
	        assert backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'OldView.fxml'.";
	        assert searchTextField != null : "fx:id=\"searchTextField\" was not injected: check your FXML file 'OldView.fxml'.";
	        assert searchButton != null : "fx:id=\"searchButton\" was not injected: check your FXML file 'OldView.fxml'.";
	        assert placeholderPane != null : "fx:id=\"placeholderPane\" was not injected: check your FXML file 'OldView.fxml'.";
	        assert deleteButton != null : "fx:id=\"deleteButton\" was not injected: check your FXML file 'OldView.fxml'.";
	        assert titleLabel != null : "fx:id=\"titleLabel\" was not injected: check your FXML file 'OldView.fxml'.";
	        assert listViewStackPane != null : "fx:id=\"listViewStackPane\" was not injected: check your FXML file 'OldView.fxml'.";
	        assert referStackPane != null : "fx:id=\"referStackPane\" was not injected: check your FXML file 'OldView.fxml'.";
	        assert referListView != null : "fx:id=\"referListView\" was not injected: check your FXML file 'OldView.fxml'.";
	        assert paragraphsListView != null : "fx:id=\"paragraphsListView\" was not injected: check your FXML file 'OldView.fxml'.";
	        assert statuesStackPane != null : "fx:id=\"listViewInnerStackPane\" was not injected: check your FXML file 'OldView.fxml'.";
	        assert statuesListView != null : "fx:id=\"statuesListView\" was not injected: check your FXML file 'OldView.fxml'.";
	        	        
	        setItems();
	        setJSON();
	    }
	    
	    private void setItems() {
	    	 try {
	    		 ObservableList<String> statuesItems = FXCollections.observableArrayList();
		    	 
		    	 ObjectMapper mapper = new ObjectMapper();
		    	 mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		    	 r = mapper.readValue(new File("/Users/Mitja/Desktop/root.json"), Root.class);
		    	 
		    	 String item;
		    	 
		    	 for(int i = 0; i < r.getStatues().length; i++) {
		    		 item = "";
		    		 item += r.getStatues()[i].getFullname() + " (" + r.getStatues()[i].getShorthand() + ")";
		    		 statuesItems.add(item);
		    	 }
		    	 statuesListView.setItems(statuesItems);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	    
	    private ObservableList<String> getParagraphs() {
	    	ObservableList<String> paragraphItems = FXCollections.observableArrayList();
	    	String item = "";
			
			for(int i = 0; i < r.getStatues().length; i++) {
				if(statuesListView.getSelectionModel().getSelectedItem().contains(r.getStatues()[i].getFullname())) {
					
					titleLabel.setText(r.getStatues()[i].getShorthand() + " - Paragraphen");	
					p = r.getStatues()[i].getParagraphs();
					for(int k = 0; k < r.getStatues()[i].getParagraphs().length; k++) {
    					item = "";
    					item += r.getStatues()[i].getParagraphs()[k].getNumber();
    					paragraphItems.add(item);
    				}
				}
			}
			
			if(item.isEmpty()) {
				paragraphItems.add("Keine Einträge vorhanden!");
			} 
			
			return paragraphItems;
	    }
	    
	    private void setJSON() {
	    	BufferedReader br = null;
	    	
	    	try {
		    	br = new BufferedReader(new FileReader("/Users/Mitja/Desktop/root.json"));
		    	String line = br.readLine();

	    	    while (line != null) {
	    	    	json += line;
	    	        line = br.readLine();
	    	    }
	    	} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
	    	    try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	    	}
	    }
	    
	    private ObservableList<String> getRefers() {
	    	ObservableList<String> referItems = FXCollections.observableArrayList();	    	
	    	Subparagraphs sub = null;
	    	
	    	
			
			return referItems;
	    }
 }
