package it.polito.tdp.crimes.model;
import java.lang.Thread;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;
import java.time.*;
public class Model {
	EventsDao dao = new EventsDao();
	Graph<String, DefaultWeightedEdge> grafo;
	List<String> crimes;
	List<Adiacenze> adiacenze;
	
	//RICORSIONE
	List<String> best;
	List<String> ottimale=new ArrayList<>();
	
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
	
	public void calcolaPercorso(Adiacenze adiacenza)  {
		//RICAVO I DUE VERTICI DALL'ADIACENZA
		String partenza = adiacenza.getCrime1();
		String arrivo = adiacenza.getCrime2();
		
		this.best = new ArrayList<>();
		
		List<String> parziale = new ArrayList<>();
		parziale.add(partenza);
		
		List<String> daVisitare = Graphs.neighborListOf(this.grafo, partenza);
		ricorsione(parziale, arrivo, daVisitare);
		
		
		/* RELATIVO ALLA TERZA VERSIONE

		ricorsione3(partenza, arrivo, daVisitare, parziale);
		System.out.println("Ho finito");
		
		*/
		
		
	}

	public void ricorsione(List<String> parziale, String arrivo, List<String> daVisitare) {
		
			if(parziale.contains(arrivo)) {
				//ho terminato il cammino
				if(best.size() < parziale.size()) {
					System.out.println(parziale.toString());
					//ho trovato cammino più lunog quindi diventa best
					this.best = new ArrayList<>();
					this.best.addAll(parziale);
				}
				return; //torno indietro perché non devo aggiungere altro
			}
			
			//ricerca del cammino
			for(String vertice : daVisitare) {
				if(!parziale.contains(vertice)) {
					
					parziale.add(vertice);
					List<String> daVisitareNext = Graphs.neighborListOf(this.grafo, vertice);
					
					ricorsione(parziale, arrivo, daVisitareNext);
					parziale.remove(vertice);
				}
			}
		
	}
	
	public void ricorsione2(List<String> parziale, String arrivo, List<String> daVisitare){
		for(String vertice : daVisitare) {
			if(!parziale.contains(vertice)) {
				parziale.add(vertice);
				
				//IN QUESTA SECONDA VERSIONE FACCIO IL CONTROLLO NEL FOR
				if(parziale.contains(arrivo)) {
					if(parziale.size() > best.size()) {
						System.out.println(parziale.toString());
						best = new ArrayList<>();
						best.addAll(parziale);
					}
					return;
				}
				
				List<String> daVisitareNext = Graphs.neighborListOf(this.grafo, vertice);
				ricorsione2(parziale, arrivo, daVisitareNext);
				parziale.remove(vertice);
			}
		}
	}
	
	public void ricorsione3(String current,String arrivo, List<String> daVisitare, List<String> parziale) {
		
		if(current.equals(arrivo)) {
			if(best.size()<parziale.size()) {
				best = new ArrayList<>();
				best.addAll(parziale);
			//	System.out.println(ottimale.toString());
			//	System.out.println("BEST Trovata");
			}
			return;
		}
		for(String vicino : daVisitare) {
			if(!parziale.contains(vicino)) {
				parziale.add(vicino);
				ricorsione3(vicino, arrivo,Graphs.neighborListOf(this.grafo, vicino), parziale);
				parziale.remove(vicino);
			}
		}
		
		
		
	}
	public List<String> getBest() {
		return best;
	}
	
}
