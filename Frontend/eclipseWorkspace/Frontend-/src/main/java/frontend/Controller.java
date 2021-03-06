package frontend;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
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
    	private String subPTitle, paragraphsTitle;
		private Root r;
		
    	@FXML
        void backButtonAction(MouseEvent event) {
    		if(listViewStatus == 1) {
    			listViewStatus = 0;
    			statuesStackPane.toFront();
    			statuesListView.setDisable(false);
    			
    			paragraphsListView.toBack();
    			paragraphsListView.setDisable(true);
    			
    			titleLabel.setText("Gesetzb�cher");
    			backButton.setDisable(true);
    			
    		} else if(listViewStatus == 2) {
    			listViewStatus = 1;
    			paragraphsListView.toFront();
    			paragraphsListView.setDisable(false);
    			
    			subStackPane.toBack();
    			subListView.setDisable(true);
    			
    			titleLabel.setText(paragraphsTitle);
    			
    		} else if(listViewStatus == 3) {
    			listViewStatus = 2;
    			subListView.toFront();
    			subListView.setDisable(false);
    			
    			paragraphsListView.toBack();
    			paragraphsListView.setDisable(true);
    			
    			statuesStackPane.toBack();
    			statuesListView.setDisable(true);
    			
    			referStackPane.toBack();
    			referListView.setDisable(true);
    			
    			titleLabel.setText(subPTitle);
    			
    		}
        }

	    @FXML
	    void deleteButtonAction(MouseEvent event) {
	    	deleteButton.setDisable(true);
	    	int statIndex = statuesListView.getSelectionModel().getSelectedIndex();
	    	int paraIndex = paragraphsListView.getSelectionModel().getSelectedIndex();
	    	int subIndex = subListView.getSelectionModel().getSelectedIndex();
	    	int referIndex = referListView.getSelectionModel().getSelectedIndex();
	    	
	    	if(listViewStatus == 0) {
	    		if(statuesListView.getSelectionModel().getSelectedItem() != null) {
	    			statuesListView.getItems().remove(statIndex);
	    			
	    			Statues[] newStatues = new Statues[r.getStatues().length-1];
	    			for(int i = 0; i < r.getStatues().length; i++) {
	    					if(i <= statIndex) {
	    						newStatues[i] = r.getStatues()[i];
	    					} else {
	    						newStatues[i-1] = r.getStatues()[i];
	    					}
	    			}
	    			r.setStatues(newStatues);
	    		}
	    	} else if(listViewStatus == 1) {	    		
	    		if(paragraphsListView.getSelectionModel().getSelectedItem() != null) {
	    			paragraphsListView.getItems().remove(paraIndex);
	    		    
	    			Paragraphs[] newParagraphs = new Paragraphs[r.getStatues()[statIndex].getParagraphs().length-1];
					for(int i = 0; i < r.getStatues()[statIndex].getParagraphs().length; i++) {
						if(i <= paraIndex) {
    						newParagraphs[i] = r.getStatues()[statIndex].getParagraphs()[i];
    					} else {
    						newParagraphs[i-1] = r.getStatues()[statIndex].getParagraphs()[i];
    					}
	    			}
	    			
	    			r.getStatues()[statIndex].setParagraphs(newParagraphs);
	    		}
    		} else if(listViewStatus == 2) {    			
	    		if(subListView.getSelectionModel().getSelectedItem() != null) {
	    			subListView.getItems().remove(subIndex);
	    			
	    			Subparagraphs[] newSubs = new Subparagraphs[r.getStatues()[statIndex].getParagraphs()[paraIndex].getSubparagraphs().length - 1];
	    			for(int i = 0; i < newSubs.length; i++) {
	    				if(i <= subIndex) {
    						newSubs[i] = r.getStatues()[statIndex].getParagraphs()[paraIndex].getSubparagraphs()[subIndex];
    					} else {
    						newSubs[i-1] = r.getStatues()[statIndex].getParagraphs()[paraIndex].getSubparagraphs()[subIndex];
    					}
		    		}
	    			r.getStatues()[statIndex].getParagraphs()[paraIndex].setSubparagraphs(newSubs);
	    			
	    		}
	    	} else if(listViewStatus == 3) {    			
	    		if(referListView.getSelectionModel().getSelectedItem() != null) {
	    			referListView.getItems().remove(subIndex);
	    			
	    			RequiredBy[] newRefers = new RequiredBy[r.getStatues()[statIndex].getParagraphs()[paraIndex].getSubparagraphs()[subIndex].getRequiredby().length - 1];
	    			for(int i = 0; i < newRefers.length; i++) {
	    				if(i <= subIndex) {
	    					newRefers[i] = r.getStatues()[statIndex].getParagraphs()[paraIndex].getSubparagraphs()[subIndex].getRequiredby()[referIndex];
    					} else {
    						newRefers[i-1] = r.getStatues()[statIndex].getParagraphs()[paraIndex].getSubparagraphs()[subIndex].getRequiredby()[referIndex];
    					}
		    		}
	    			r.getStatues()[statIndex].getParagraphs()[paraIndex].getSubparagraphs()[subIndex].setRequiredby(newRefers);	    			
	    		}
	    	}
	    	
	    	ObjectMapper mapper = new ObjectMapper();
			try {
				mapper.writeValue(new File("root.json"), r);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
	    
	    @FXML
	    void statuesListViewAction(MouseEvent event) {
	    	if(statuesListView.getSelectionModel().getSelectedItem() != null) {
	    		deleteButton.setDisable(false);
	    		if(event.getButton().equals(MouseButton.PRIMARY)) {
	    			if(event.getClickCount() == 2) {
	    				listViewStatus = 1;
	    				
	    				backButton.setDisable(false);
	    				
	    				paragraphsTitle = statuesListView.getSelectionModel().getSelectedItem().getShorthand() + " - Paragraphen";
	    				titleLabel.setText(paragraphsTitle);
	    				
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
	    				
	    				subPTitle = paragraphsListView.getSelectionModel().getSelectedItem().toString() + " - Subparagraphen";
	    				titleLabel.setText(subPTitle);
	    				
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
	    	if(subListView.getSelectionModel().getSelectedItem() != null) {
	    		deleteButton.setDisable(false);
	    		if(event.getButton().equals(MouseButton.PRIMARY)) {
	    			if(event.getClickCount() == 2) {
	    				referStackPane.toFront();
	    				listViewStatus = 3;
	    				backButton.setDisable(false);
	    				
	    				titleLabel.setText(subListView.getSelectionModel().getSelectedItem().toString() + " - Verweise");
	    				
	    				referListView.toFront();
	    				referListView.setDisable(false);
	    				
	    				ObservableList<RequiredBy> referItems = FXCollections.observableArrayList();
	    				
	    				referItems.addAll(subListView.getSelectionModel().getSelectedItem().getRequiredby());
	    				
	    				if(referItems.isEmpty()) {
	    					RequiredBy error = new RequiredBy();
	    					referItems.add(error);
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
	    		int subSize = subListView.getItems().size();
		    	MultipleSelectionModel<Subparagraphs> model = subListView.getSelectionModel();
		    	
		    	LinkedList<Subparagraphs> getMatches = new LinkedList<Subparagraphs>();

		    	if(!searchTextField.getText().isEmpty() || searchTextField.getText() != null) {

		    		getMatches.clear();
		    		for(int i = 0; i < subSize; i++) {
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

	    				for(int i = 0; i < subSize; i++) {
	    	    			if(subListView.getItems().get(i).toString().toLowerCase().contains(searchTextField.getText().toLowerCase())) {
	    	    				model.select(i);
	    	    				subListView.scrollTo(i);
	    	    				break;
	    	    			}
	    	    		}
	    			}
		    	}
	    	} else if(listViewStatus == 3) {
	    		int referSize = referListView.getItems().size();
		    	MultipleSelectionModel<RequiredBy> model = referListView.getSelectionModel();
		    	
		    	LinkedList<RequiredBy> getMatches = new LinkedList<RequiredBy>();

		    	if(!searchTextField.getText().isEmpty() || searchTextField.getText() != null) {

		    		getMatches.clear();
		    		for(int i = 0; i < referSize; i++) {
		    			if(referListView.getItems().get(i).toString().toLowerCase().contains(searchTextField.getText().toLowerCase())) {
		    				getMatches.add(referListView.getItems().get(i));
		    			}
		    		}
		    		
		    		if(model.getSelectedItem() != null && model.getSelectedIndex() != getMatches.size()-1) {

		    			for(int i = model.getSelectedIndex()+1; i < referListView.getItems().size(); i++) {
			    			if(referListView.getItems().get(i).toString().toLowerCase().contains(searchTextField.getText().toLowerCase())) {
			    				model.select(i);
			    				referListView.scrollTo(i);
			    				break;
			    			}
			    		}	
	    			} else {

	    				for(int i = 0; i < referSize; i++) {
	    	    			if(referListView.getItems().get(i).toString().toLowerCase().contains(searchTextField.getText().toLowerCase())) {
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

		    	 r = mapper.readValue(new File("root.json"), Root.class);
                 statuesItems.addAll(r.getStatues());

		    	 statuesListView.setItems(statuesItems);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	    
	    private ObservableList checkForNull(ObservableList<?> check) {
	    	for(int i = 0; i < check.size(); i++) {
	    		if(check.get(i).toString().contains("null")) {
	    			check.remove(i);
	    		}
	    	}
	    	
	    	return check;
	    }
 }
