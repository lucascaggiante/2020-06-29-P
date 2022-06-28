package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;


public class Model {
	SimpleWeightedGraph<Match, DefaultWeightedEdge> grafo;
	PremierLeagueDAO dao;
	List<Match> vertici;
	Map<Integer, Match> verticiMap;
	List<Match> longest;
	Double peso;
	
	public Model() {
		this.dao=new PremierLeagueDAO();
		this.vertici= new LinkedList<>();
		this.verticiMap = new HashMap<>();
		
	}
	
	public void creaGrafo(Integer mese, Integer minuti) {
		this.grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.vertici = this.dao.getMatchesByMonth(mese);
		Graphs.addAllVertices(this.grafo, this.vertici);
		
		for (Match m : this.vertici) {
			this.verticiMap.put(m.getMatchID(), m);
		}
		
		for (Adiacenze a : this.dao.getAdiacenze(mese, minuti, this.verticiMap)) {
			Graphs.addEdge(this.grafo, a.getM1(), a.getM2(), a.getPeso());
		}
		
		
	}
	
	public List<Adiacenze> getMaxConnessione() {
		List<Adiacenze> result = new ArrayList<Adiacenze>();
		Double max = 0.0;
		
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			Double peso =  this.grafo.getEdgeWeight(e);
			if(peso > max) {
				result.clear();
				result.add(new Adiacenze(this.grafo.getEdgeSource(e),
						this.grafo.getEdgeTarget(e), peso));
				max = peso;
			} else if (peso == max) {
				result.add(new Adiacenze(this.grafo.getEdgeSource(e),
						this.grafo.getEdgeTarget(e), peso));
			}
			
		}
		
		return result;
	}

	public Graph getGrafo() {
		
		return this.grafo;
	}
	
	
public List<Match> trovaPercorso (Match partenza, Match arrivo){
		
		this.longest = new ArrayList<>();
		
		List<Match> parziale  = new ArrayList <Match>();
		this.peso=0.0;
		parziale.add(partenza);
		ricorsione(parziale, arrivo, 0);
		return longest;
		
		
	}

	private void ricorsione(List<Match> parziale, Match arrivo, double pesoParziale) {
		
		
		if(parziale.contains(arrivo)) {
			
			if(pesoParziale > peso) {
			peso = pesoParziale;
			longest = new ArrayList<>(parziale);
			}
			return;
		}
		
		
		
		Match ultimoAggiunto= parziale.get(parziale.size()-1);
		Integer t1 = ultimoAggiunto.getTeamHomeID();
		Integer t2 = ultimoAggiunto.getTeamAwayID();
		
		List<Match> vicini = Graphs.neighborListOf(grafo, ultimoAggiunto);
		
		for(Match m : vicini) {
			
			Integer t3 = m.getTeamHomeID();
			Integer t4 = m.getTeamAwayID();
			
			if(!parziale.contains(m) && t1!=t3 && t1!=t4 && t2!=t3  && t2!=t4) {
			 
				
				parziale.add(m);
			double NuovoPesoParziale =  pesoParziale+ grafo.getEdgeWeight(this.grafo.getEdge(m, ultimoAggiunto));
				
				ricorsione(parziale, arrivo, NuovoPesoParziale );
				parziale.remove(m);
				
			}
			
		}
		
		
	}
	
	
}
