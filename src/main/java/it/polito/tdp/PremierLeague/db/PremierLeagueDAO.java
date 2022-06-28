package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Adiacenze;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Player;

public class PremierLeagueDAO {
	
	public List<Player> listAllPlayers(){
		String sql = "SELECT * FROM Players";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				result.add(player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Match> listAllMatches(){
		String sql = "SELECT m.MatchID, m.TeamHomeID, m.TeamAwayID, m.teamHomeFormation, m.teamAwayFormation, m.resultOfTeamHome, m.date, t1.Name, t2.Name   "
				+ "FROM Matches m, Teams t1, Teams t2 "
				+ "WHERE m.TeamHomeID = t1.TeamID AND m.TeamAwayID = t2.TeamID";
		List<Match> result = new ArrayList<Match>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				
				Match match = new Match(res.getInt("m.MatchID"), res.getInt("m.TeamHomeID"), res.getInt("m.TeamAwayID"), res.getInt("m.teamHomeFormation"), 
							res.getInt("m.teamAwayFormation"),res.getInt("m.resultOfTeamHome"), res.getTimestamp("m.date").toLocalDateTime(), res.getString("t1.Name"),res.getString("t2.Name"));
				
				
				result.add(match);

			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Match> getMatchesByMonth(Integer mese){
		String sql = "SELECT *\n"
				+ "FROM Matches m, Teams t1, Teams t2\n"
				+ "WHERE MONTH(Date)=?\n"
				+ "AND t1.TeamID = m.TeamHomeID\n"
				+ "AND t2.TeamID = m.TeamAwayID\n";
		List<Match> result = new ArrayList<Match>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, mese);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Match match = new Match(res.getInt("m.MatchID"),
										res.getInt("m.TeamHomeID"), 
										res.getInt("m.TeamAwayID"), 
										res.getInt("m.teamHomeFormation"), 
										res.getInt("m.teamAwayFormation"),
										res.getInt("m.resultOfTeamHome"), 
										res.getTimestamp("m.date").toLocalDateTime(), 
										res.getString("t1.Name"),
										res.getString("t2.Name"));
			
			
				
				result.add(match);

			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Adiacenze> getAdiacenze(Integer mese, Integer minuti, Map<Integer, Match> vertici){
		String sql = "SELECT m1.MatchID, m2.MatchID, COUNT(*) AS peso\n"
				+ "FROM matches AS m1, matches m2,  actions AS a1 , actions AS a2\n"
				+ "WHERE \n"
				+ "	MONTH(m1.Date)=? AND MONTH(m2.Date)=? \n"
				+ "	AND a1.MatchID=m1.MatchID \n"
				+ "	AND a1.TimePlayed >? AND a2.TimePlayed>? \n"
				+ "	AND a2.MatchID=m2.MatchID \n"
				+ "	AND m1.MatchID > m2.MatchID \n"
				+ "	AND a1.PlayerID=a2.PlayerID\n"
				+ "GROUP BY m1.MatchID, m2.MatchID";
		List<Adiacenze> result = new ArrayList<Adiacenze>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, mese);
			st.setInt(2, mese);
			st.setInt(3, minuti);
			st.setInt(4, minuti);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				
				Match match1 = vertici.get(res.getInt("m1.MatchID"));
				Match match2 =vertici.get(res.getInt("m2.MatchID"));
				Adiacenze a= new Adiacenze(match1, match2, res.getDouble("peso"));
				
				result.add(a);

			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
