package frontend;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    	private String json;
    	private Paragraphs[] p;
    	
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
    			
    		} else if(listViewStatus == 3) {
    			listViewStatus = 2;
    			subListView.toFront();
    			subListView.setDisable(false);
    			
    			referStackPane.toBack();
    			referListView.setDisable(true);
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
	    				
	    				titleLabel.setText("Paragraphen");
	    				
	    				paragraphsListView.toFront();
	        			paragraphsListView.setDisable(false);
	    				
	    				ObservableList<Paragraphs> paragraphItems = FXCollections.observableArrayList();
                        paragraphItems.addAll(statuesListView.getSelectionModel().getSelectedItem().getParagraphs());
                        
                        if(paragraphItems.isEmpty())
                        	System.out.println(1);

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
	    				listViewStatus = 2;
	    				backButton.setDisable(false);
	    				
	    				titleLabel.setText(paragraphsListView.getSelectionModel().getSelectedItem().toString() + " - Subparagraphen");
	    				
	    				subListView.toFront();
	    				subListView.setDisable(false);
	    				
	    				ObservableList<Subparagraphs> subItems = FXCollections.observableArrayList();
	    				
	    				subItems.addAll(paragraphsListView.getSelectionModel().getSelectedItem().getSubparagraphs());
	    				
	    				if(subItems.isEmpty())
                        	System.out.println(2);
	    				
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
	    				listViewStatus = 3;
	    				backButton.setDisable(false);
	    				
	    				referListView.toFront();
	    				referListView.setDisable(false);
	    				
	    				ObservableList<RequiredBy> referItems = FXCollections.observableArrayList();
	    				referItems.addAll(subListView.getSelectionModel().getSelectedItem().getrequiredby());
	    				
	    				if(referItems.isEmpty())
//                        	referItems.add();
	    				
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
	        setJSON();
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
	    
	    private ObservableList<String> getParagraphs() {
	    	ObservableList<String> paragraphItems = FXCollections.observableArrayList();
	    	String item = "";

			for(int i = 0; i < r.getStatues().length; i++) {
				if(statuesListView.getSelectionModel().getSelectedItem().toString().contains(r.getStatues()[i].getFullname())) {
					
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
	    
	    private ObservableList<Subparagraphs> getRefers() {
	    	ObservableList<Subparagraphs> referItems = FXCollections.observableArrayList();	    	
	    	Subparagraphs sub = null;
	    	
	    	
			
			return referItems;
	    }
 }
