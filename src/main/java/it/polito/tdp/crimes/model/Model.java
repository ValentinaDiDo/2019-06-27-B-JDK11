package it.polito.tdp.crimes.model;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	EventsDao dao = new EventsDao();
	Graph<String, DefaultWeightedEdge> grafo;
	List<String> crimes;
	List<Adiacenze> adiacenze;
	
	
	public List<String> getCategories(){
		return this.dao.getCategories();
	}
	public List<Integer> getMonths(){
		return this.dao.getMonths();
	}
	public void creaGrafo(int mese, String categoria) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		this.crimes = this.dao.getCrimini(categoria, mese);
		Graphs.addAllVertices(this.grafo, crimes);
		
		this.adiacenze = this.dao.getAdiacenze(categoria, mese);
		for(Adiacenze a : adiacenze) {
			if(a.getPeso() > 0) {
				Graphs.addEdge(this.grafo, a.getCrime1(), a.getCrime2(), a.getPeso());
			}
		}
		
	}
	public Graph<String, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}
	public List<Adiacenze> getAdiacenze() {
		return adiacenze;
	}
	
}
