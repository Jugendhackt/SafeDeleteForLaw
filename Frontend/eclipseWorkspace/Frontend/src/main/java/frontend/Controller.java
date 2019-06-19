package frontend;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
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
	    private StackPane subStackPane;
	    
	    @FXML
	    private StackPane referStackPane;
	    
	    @FXML
	    private StackPane statuesStackPane;

    	@FXML
    	private ListView<Statues> statuesListView;

    	@FXML
    	private ListView<Paragraphs> paragraphsListView;
    
    	@FXML
    	private ListView<Subparagraphs> subListView; // Vorübergehend

    	@FXML
    	private ListView<RequiredBy> referListView;
    	
    	private int listViewStatus = 0;
    	private Root r;
    	
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
    			
    			subStackPane.toBack();
    			subListView.setDisable(true);
    			
    		} else if(listViewStatus == 3) {
    			listViewStatus = 2;
    			subListView.toFront();
    			subListView.setDisable(false);
    			
    			referStackPane.toBack();
    			referListView.setDisable(true);
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
	    				
	    				titleLabel.setText("Paragraphen");
	    				
	    				paragraphsListView.toFront();
	        			paragraphsListView.setDisable(false);
	    				
	    				ObservableList<Paragraphs> paragraphItems = FXCollections.observableArrayList();
                        paragraphItems.addAll(statuesListView.getSelectionModel().getSelectedItem().getParagraphs());
                        

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
	    void paragraphsListViewAction(MouseEvent event) {
	    	if(paragraphsListView.getSelectionModel().getSelectedItem() != null) {
	    		deleteButton.setDisable(false);
	    		if(event.getButton().equals(MouseButton.PRIMARY)) {
	    			if(event.getClickCount() == 2) {
	    				subStackPane.toFront();
	    				listViewStatus = 2;
	    				backButton.setDisable(false);
	    				
	    				titleLabel.setText("Subparagraphen");
	    				
	    				titleLabel.setText(paragraphsListView.getSelectionModel().getSelectedItem().toString() + " - Subparagraphen");
	    				
	    				subListView.toFront();
	    				subListView.setDisable(false);
	    				
	    				ObservableList<Subparagraphs> subItems = FXCollections.observableArrayList();
	    				
	    				subItems.addAll(paragraphsListView.getSelectionModel().getSelectedItem().getSubparagraphs());
	    				
	    				subListView.setItems(subItems);
	    				subListView.setVisible(true);
	    				
	    				paragraphsListView.setDisable(true);
	    				paragraphsListView.toBack();
	    				listViewStackPane.toBack();
	    			}
	    		}
	    	}
	    }
	    
	    @FXML
	    void subListViewAction(MouseEvent event) {
	    	if(paragraphsListView.getSelectionModel().getSelectedItem() != null) {
	    		deleteButton.setDisable(false);
	    		if(event.getButton().equals(MouseButton.PRIMARY)) {
	    			if(event.getClickCount() == 2) {
	    				referStackPane.toFront();
	    				listViewStatus = 3;
	    				backButton.setDisable(false);
	    				
	    				titleLabel.setText("Verweise");
	    				
	    				referListView.toFront();
	    				referListView.setDisable(false);
	    				
	    				ObservableList<RequiredBy> referItems = FXCollections.observableArrayList();
	    				referItems.addAll(subListView.getSelectionModel().getSelectedItem().getrequiredby());	    				
	    				
	    				if(referItems.isEmpty()) {
	    					RequiredBy err = new RequiredBy();
	    					err.setErrorMsg("Keine Einträge vorhanden!");
	    					referItems.add(err);
	    					
                        	referListView.setItems(referItems);
	    				}
	    					
	    				referListView.setItems(referItems);
	    				referListView.setVisible(true);
	    					
	    				subListView.setDisable(true);
	    				subListView.toBack();
	    				subStackPane.toBack();
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
		    	MultipleSelectionModel<Statues> model = statuesListView.getSelectionModel();
		    	
		    	LinkedList<Statues> getMatches = new LinkedList<Statues>();

		    	if(!searchTextField.getText().isEmpty() || searchTextField.getText() != null) {

		    		getMatches.clear();
		    		for(int i = 0; i < lawSize; i++) {
		    			if(statuesListView.getItems().get(i).toString().toLowerCase().contains(searchTextField.getText().toLowerCase())) {
		    				getMatches.add(statuesListView.getItems().get(i));
		    			}
		    		}
		    		
		    		if(model.getSelectedItem() != null && model.getSelectedIndex() != getMatches.size()-1) {

		    			for(int i = model.getSelectedIndex()+1; i < statuesListView.getItems().size(); i++) {
			    			if(statuesListView.getItems().get(i).toString().toLowerCase().contains(searchTextField.getText().toLowerCase())) {
			    				model.select(i);
			    				statuesListView.scrollTo(i);
			    				break;
			    			}
			    		}	
	    			} else {

	    				for(int i = 0; i < lawSize; i++) {
	    	    			if(statuesListView.getItems().get(i).toString().toLowerCase().contains(searchTextField.getText().toLowerCase())) {
	    	    				model.select(i);
	    	    				statuesListView.scrollTo(i);
	    	    				break;
	    	    			}
	    	    		}
	    			}
		    	}
	    	} else if(listViewStatus == 1) {
	    		int pargraphsSize = paragraphsListView.getItems().size();
		    	MultipleSelectionModel<Paragraphs> model = paragraphsListView.getSelectionModel();
		    	
		    	LinkedList<Paragraphs> getMatches = new LinkedList<Paragraphs>();

		    	if(!searchTextField.getText().isEmpty() || searchTextField.getText() != null) {

		    		getMatches.clear();
		    		for(int i = 0; i < pargraphsSize; i++) {
		    			if(paragraphsListView.getItems().get(i).toString().toLowerCase().contains(searchTextField.getText().toLowerCase())) {
		    				getMatches.add(paragraphsListView.getItems().get(i));
		    			}
		    		}
		    		
		    		if(model.getSelectedItem() != null && model.getSelectedIndex() != getMatches.size()-1) {

		    			for(int i = model.getSelectedIndex()+1; i < paragraphsListView.getItems().size(); i++) {
			    			if(paragraphsListView.getItems().get(i).toString().toLowerCase().contains(searchTextField.getText().toLowerCase())) {
			    				model.select(i);
			    				paragraphsListView.scrollTo(i);
			    				break;
			    			}
			    		}	
	    			} else {

	    				for(int i = 0; i < pargraphsSize; i++) {
	    	    			if(paragraphsListView.getItems().get(i).toString().toLowerCase().contains(searchTextField.getText().toLowerCase())) {
	    	    				model.select(i);
	    	    				paragraphsListView.scrollTo(i);
	    	    				break;
	    	    			}
	    	    		}
	    			}
		    	}
	    	} else if(listViewStatus == 2) {
	    		int referSize = subListView.getItems().size();
		    	MultipleSelectionModel<Subparagraphs> model = subListView.getSelectionModel();
		    	
		    	LinkedList<Subparagraphs> getMatches = new LinkedList<Subparagraphs>();

		    	if(!searchTextField.getText().isEmpty() || searchTextField.getText() != null) {

		    		getMatches.clear();
		    		for(int i = 0; i < referSize; i++) {
		    			if(subListView.getItems().get(i).toString().toLowerCase().contains(searchTextField.getText().toLowerCase())) {
		    				getMatches.add(subListView.getItems().get(i));
		    			}
		    		}
		    		
		    		if(model.getSelectedItem() != null && model.getSelectedIndex() != getMatches.size()-1) {

		    			for(int i = model.getSelectedIndex()+1; i < subListView.getItems().size(); i++) {
			    			if(subListView.getItems().get(i).toString().toLowerCase().contains(searchTextField.getText().toLowerCase())) {
			    				model.select(i);
			    				subListView.scrollTo(i);
			    				break;
			    			}
			    		}	
	    			} else {

	    				for(int i = 0; i < referSize; i++) {
	    	    			if(subListView.getItems().get(i).toString().toLowerCase().contains(searchTextField.getText().toLowerCase())) {
	    	    				model.select(i);
	    	    				subListView.scrollTo(i);
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
	        assert subStackPane != null : "fx:id=\"subStackPane\" was not injected: check your FXML file 'OldView.fxml'.";
	        assert subListView != null : "fx:id=\"subListView\" was not injected: check your FXML file 'OldView.fxml'.";
	        assert paragraphsListView != null : "fx:id=\"paragraphsListView\" was not injected: check your FXML file 'OldView.fxml'.";
	        assert statuesStackPane != null : "fx:id=\"statuesStackPane\" was not injected: check your FXML file 'OldView.fxml'.";
	        assert statuesListView != null : "fx:id=\"statuesListView\" was not injected: check your FXML file 'OldView.fxml'.";

	        setItems();
	    }
	    
	    private void setItems() {
	    	 try {
	    		 ObservableList<Statues> statuesItems = FXCollections.observableArrayList();
		    	 
		    	 ObjectMapper mapper = new ObjectMapper();
		    	 mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		    	 r = mapper.readValue(new File("/Users/Mitja/Desktop/root.json"), Root.class);
                 statuesItems.addAll(r.getStatues());

		    	 statuesListView.setItems(statuesItems);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
 }
