package frontend;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class Controller {
	
	   @FXML
	    private ListView<String> lawListView;
	    @FXML
	    private TextField searchTextField;

	    @FXML
	    private Button searchButton;
	    
	    @FXML
	    private Pane placeholderPane;
	    
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
	    	
	    	if(!searchTextField.getText().isEmpty() || searchTextField.getText() != null) {
	    		if(model.getSelectedItem() != null && model.getSelectedIndex() != lawSize-1) {
	    			
	    			for(int i = model.getSelectedIndex()+1; i < lawListView.getItems().size(); i++) {
		    			if(lawListView.getItems().get(i).toLowerCase().contains(searchTextField.getText().toLowerCase())) {
		    				
		    				model.select(i);
		    				break;
		    			}
		    		}	
    			} else {
    				for(int i = 0; i < lawSize; i++) {
    	    			if(lawListView.getItems().get(i).toLowerCase().contains(searchTextField.getText().toLowerCase())) {
    	    				
    	    				model.select(i);
    	    				break;
    	    			}
    	    		}
    			}
	    	}
	    }

	    @FXML
	    void initialize() {
	        assert lawListView != null : "fx:id=\"lawListView\" was not injected: check your FXML file 'View.fxml'.";
	        assert searchTextField != null : "fx:id=\"searchTextField\" was not injected: check your FXML file 'View.fxml'.";
	        assert searchButton != null : "fx:id=\"searchButton\" was not injected: check your FXML file 'View.fxml'.";
	        assert deleteButton != null : "fx:id=\"deleteButton\" was not injected: check your FXML file 'View.fxml'.";
	        assert checkButton != null : "fx:id=\"checkButton\" was not injected: check your FXML file 'View.fxml'.";
	        assert placeholderPane != null : "fx:id=\"placeholderPane\" was not injected: check your FXML file 'View.fxml'.";
	        
	        ObservableList<String> items = FXCollections.observableArrayList (
	        	    "Test1", "Test2", "Test3", "Test4");
	        
	        lawListView.setItems(items);
	        
	    }
}
