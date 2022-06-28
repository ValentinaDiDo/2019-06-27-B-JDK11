/**
 * Sample Skeleton for 'Crimes.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.crimes.model.Adiacenze;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class CrimesController {

	private Model model;
	Graph<String, DefaultWeightedEdge> grafo;
	private boolean grafoCreato = false;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxCategoria"
    private ComboBox<String> boxCategoria; // Value injected by FXMLLoader

    @FXML // fx:id="boxMese"
    private ComboBox<Integer> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="boxArco"
    private ComboBox<Adiacenze> boxArco; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Crea grafo...\n");
    	
    	String categoria = this.boxCategoria.getValue();
    	int mese = this.boxMese.getValue();
    	if(categoria == null || mese == 0) {
    		txtResult.setText("INSERISCI MESE E CATEGORIA ");
    	}else {
    		this.model.creaGrafo(mese, categoria);
    		this.grafo = this.model.getGrafo();
    		this.grafoCreato = true;
    		
    		txtResult.setText("GRAFO CREATO\n");
    		txtResult.appendText("# VERTICI : "+ this.grafo.vertexSet().size());
    		txtResult.appendText("\n# ARCHI : "+this.grafo.edgeSet().size());
    		
    		List<Adiacenze> adiacenze = this.model.getAdiacenze();
    		double sum = 0.0;
    		int archi = 0;
    		for(Adiacenze a : adiacenze) {
    			if(a.getPeso() > 0) {
    				archi ++;
    				sum += a.getPeso();
    			}
    		}
    		List<Adiacenze> adiacenzeMigliori = new ArrayList<>();
    		double media = sum / archi;
    		txtResult.appendText("\n- - MEDIA : "+media+"- -\n");
    		for(Adiacenze a : adiacenze) {
    			if(a.getPeso() >= media) {
    				txtResult.appendText("\n"+a.toString());
    				adiacenzeMigliori.add(a);
    			}
    		}
    		this.boxArco.getItems().clear();
    		this.boxArco.getItems().addAll(adiacenzeMigliori);
    		
    	}
    }
    
    @FXML
    void doCalcolaPercorso(ActionEvent event) throws InterruptedException {
    	txtResult.clear();
    	txtResult.appendText("Calcola percorso...\n");
    	if(this.grafoCreato == false) {
    		txtResult.setText("devi prima creare il grafo");
    	}if(this.boxArco.getValue() == null) {
    		txtResult.appendText("seleziona un arco");
    	}else {
    		//ricerca ricorsiva
    		txtResult.setText("cerco percorso");
    		Adiacenze scelta = this.boxArco.getValue();
    		this.model.calcolaPercorso(scelta);
    		List<String> best = this.model.getBest();
    		for(String v : best) {
    			txtResult.appendText("\n"+v);
    		}
    		
    	}
    }
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Crimes.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	this.boxCategoria.getItems().clear();
    	this.boxCategoria.getItems().addAll(this.model.getCategories());
    	
    	this.boxMese.getItems().clear();
    	this.boxMese.getItems().addAll(this.model.getMonths());
    }
}
