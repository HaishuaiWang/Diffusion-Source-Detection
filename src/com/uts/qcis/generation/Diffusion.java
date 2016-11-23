package com.uts.qcis.generation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import com.mathworks.toolbox.javabuilder.*;

import matlabJar.*;
import l1.*;
import l1.whs;
//import test_whs.*;

public class Diffusion {
	Graph g = new Graph();
	Map<Integer, List<Edge>> m_edges = new HashMap<Integer, List<Edge>>();
	Map<Integer, List<Edge>> mf_edges = new HashMap<Integer, List<Edge>>();
	public void setD(double mu, double sigma){
		g.setM_delay_mu(1);
		g.setM_delay_sigma(0.001);
	}
	
	public void setL1_mu(int mu){
		g.setMu(mu);
	}
	
	public void setL1_eta(double eta){
		g.setEta(eta);
	}
	
	/* Generate graph from data */
	public int getGraph(String... filenames){
		
		String line;
		int N; //Number of nodes
		int E; //Number of edges
		//Map<Integer, List<Integer>> link_record = new HashMap<Integer, List<Integer>>();		
		List<Edge> le = null;
		List<Edge> lee = null;
		
		if (filenames.length > 0) {
            for (String filename : filenames) {
                try {
                    FileReader fr = new FileReader(new File(filename));
                    BufferedReader br = new BufferedReader(fr);
                    while ((line = br.readLine()) != null) {
                        if (line.trim() != "") {
                            if(line.startsWith("#") & line.contains("Nodes")){
                            	String[] items = line.split(" ");
                            	N = Integer.parseInt(items[2]);
                            	E = Integer.parseInt(items[4]);
                            	g.setM_N(N);
                        		for(int j = 0; j < N; j++){
                        			Node n = new Node();
                        			n.setIfSource(false);
                        			n.setDepth(0);
                        			n.setIfObserved(false);
                        			n.setIfUnobserved(false);
                        			n.setIfDetected(false);
                        			n.setId(j);
                        			n.setState(0);
                        			n.setTime(0);
                        			g.setM_nodes(n);
                        			//List<Integer> s = new ArrayList<Integer>();
                        			//link_record.put(j, s);
                        		}
                            }
                            if(!line.startsWith("#")){
                            	
                            	//String[] items = line.split("\t");
                            	String[] items = line.split(" ");
                            	int fid = Integer.parseInt(items[0]);
                            	int tid = Integer.parseInt(items[1]);
                            	
                            	if(m_edges.get(fid) == null){
                            		le = new ArrayList<Edge>();
                            		Edge e1 = new Edge();
                            		e1.setDelay(0);
                                	e1.setFrom_id(fid);
                                	e1.setTo_id(tid);
                            		//le.clear();
                            		le.add(e1);
                            		m_edges.put(fid, le);
                            	} else {
                            		Edge e1 = new Edge();
                            		e1.setDelay(0);
                                	e1.setFrom_id(fid);
                                	e1.setTo_id(tid);
                            		le.add(e1);
                            		m_edges.put(fid, le);
                            	}
                            	
                            	if(mf_edges.get(tid) == null){
                            		lee = new ArrayList<Edge>();
                            		Edge e2 = new Edge();
                            		e2.setDelay(0);
                                	e2.setFrom_id(tid);
                                	e2.setTo_id(fid);
                            		//le.clear();
                            		lee.add(e2);
                            		mf_edges.put(tid, lee);
                            	} else {
                            		Edge e2 = new Edge();
                            		e2.setDelay(0);
                                	e2.setFrom_id(tid);
                                	e2.setTo_id(fid);
                            		//lee.add(e2);
                            		//mf_edges.put(tid, lee);
                                	mf_edges.get(tid).add(e2);
                            	}
                            	//link_record.get(fid).add(tid);
                            	
                            }
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Failed to read the Graph data!");
                    System.exit(-2);
                }  
            }
        } 
		
		for(int t : mf_edges.keySet()){
			if(m_edges.get(t) == null){
				m_edges.put(t, mf_edges.get(t));
			} else {
				for(int i = 0; i < mf_edges.get(t).size(); i++){
					m_edges.get(t).add(mf_edges.get(t).get(i));
				}
			}
		}	
		return 0;		
	}
	
	/* Random select source */
	int generateRandomSource(int k){
		int count = 0;
		Vector<Integer> v = new Vector<Integer>();
		Random r = new Random();
		g.m_sources.clear();
		if(k > g.getM_N()){
			return -1;
		} 
		g.setM_K(k);
		int rs;
		while(count < k){			
			rs = r.nextInt(g.getM_N()) % (g.getM_N() - 0 + 1);
			if(g.getM_nodes().get(rs).ifSource == true)
				rs = r.nextInt(g.getM_N()) % (g.getM_N() - 0 + 1);
				//v.add(rs);
			else{
				g.getM_nodes().get(rs).setIfSource(true);
				v.add(rs);
				count ++;
			}	
			//count ++;
		}
		g.setM_sources(v);
		System.err.println("source: "+g.getM_sources());
		return 0;
	}
	
	/* Simulate information diffusion in breadth-first manner; and generate delay */
	public void simulateDiffusion(){
//		int m_begin_time = 0;
		g.setM_begin_time(0);
		Queue q=new LinkedList();
		
		for(int i = 0; i < g.getM_sources().size(); i++){
			g.getM_nodes().get(g.getM_sources().get(i)).setState(1);
			g.getM_nodes().get(g.getM_sources().get(i)).setDepth(0);
			g.getM_nodes().get(g.getM_sources().get(i)).setTime(g.getM_begin_time());
			q.add(g.getM_sources().get(i));
		}
		System.out.println("node"+"  "+"depth");
		while(q.isEmpty() == false){
			int id =  (Integer) q.element();
			q.remove();
			int pre_depth = g.getM_nodes().get(id).getDepth();
			//System.out.println(id+"-------"+pre_depth);
			for(int j = 0; j < m_edges.get(id).size(); j++){
				if(g.getM_nodes().get(m_edges.get(id).get(j).getTo_id()).getState() == 0){
					g.getM_nodes().get(m_edges.get(id).get(j).getTo_id()).setState(1);
					g.getM_nodes().get(m_edges.get(id).get(j).getTo_id()).setDepth(pre_depth+1);
					g.getM_nodes().get(m_edges.get(id).get(j).getTo_id()).setTime(genDelay(pre_depth+1));
					q.add(m_edges.get(id).get(j).getTo_id());
					//System.out.println(m_edges.get(id).get(j).getTo_id()+"***"+g.getM_nodes().get(m_edges.get(id).get(j).getTo_id()).getTime());
				}
			}
		}
	}

	/* Generate delay for path with length depth. */
	private double genDelay(int depth) {
		// TODO Auto-generated method stub
		Random r = new Random();
		double mu = g.getM_delay_mu();
		double sigma = g.getM_delay_sigma();
		double gaussrand = r.nextGaussian();
		return gaussrand*(Math.sqrt(depth*sigma))+depth*mu;
	}
	
	/* Sample observations, store them into m_observations. */
	public int sampleObservation(double alpha){
		int d = (int) (alpha * g.getM_N());
		if(d > g.getM_N()){
			System.out.println("Too many observation nodes!... Program exit!");
			return -1;
		}
		Vector<Integer> vd = new Vector<Integer>();
		g.setM_D(d);
		g.m_detector.clear();
		int count = 0;
		Random r = new Random();
		List<Pair> pairList = new ArrayList<Pair>();
		List<Integer> li = new ArrayList<Integer>();
		while(count < d){
			int rs = r.nextInt(g.getM_N()) % (g.getM_N() - 0 + 1);
			if(!li.contains(rs) && g.getM_nodes().get(rs).ifSource == false){
			//if(g.getM_nodes().get(rs).ifSource == false){
				Pair a = new Pair();
				a.n = rs;
				a.v = g.getM_nodes().get(rs).getTime();
				pairList.add(a);
				li.add(rs);
				count ++;
			}	
		}
		Comparator comp = new AscComparator();
		Collections.sort(pairList, comp);
			
		for(int i = 0; i < pairList.size(); i++){
			g.getM_nodes().get(pairList.get(i).n).setIfDetected(true);
			//System.out.println("detectors: "+pairList.get(i).n+"  timeDelay: "+g.getM_nodes().get(pairList.get(i).n).getTime());
			vd.add(pairList.get(i).n);
		}
		g.setM_detector(vd);
		return 0;
		
	}
	
	/*Set time window. */
	public void setTimeWindow(double t){
		g.setM_window(t);
	}
	
	public void timeWindow(){
		int obt = 0;
		int unobt = 0;
		Vector<Integer> vo = new Vector<Integer>();
		Vector<Integer> uvo = new Vector<Integer>();
		for(int i = 0; i < g.m_detector.size(); i++){
			if(g.getM_nodes().get(g.getM_detector().get(i)).getTime() < g.getM_window()){
				g.getM_nodes().get(g.getM_detector().get(i)).setIfObserved(true);
				vo.add(g.getM_detector().get(i));
				//System.out.println("observations: "+g.getM_detector().get(i)+" timeDelay: "+g.getM_nodes().get(g.getM_detector().get(i)).getTime());
				obt++;
			} else {
				g.getM_nodes().get(g.getM_detector().get(i)).setIfUnobserved(true);
				uvo.add(g.getM_detector().get(i));
				//System.out.println("unobservations: "+g.getM_detector().get(i)+" timeDelay: "+g.getM_nodes().get(g.getM_detector().get(i)).getTime());
				unobt++;
			}
		}
		g.setM_observation(vo);
		g.setM_unobservation(uvo);
		g.setM_O(obt);
		g.setM_U(unobt);
		System.out.println("# observations: "+obt+"  # unobservations: "+unobt);
	}
	
	public int observationNumber(){
		return g.getM_O();
	}
	
	/* Generate the path distance from the observation. */
	public void genPropagationMatirx(){
		double [][] ma;
		ma = new double[g.getM_O()][];
		for(int i = 0; i < g.getM_O(); i++){
			ma[i] = new double[g.getM_N()];
			calDistance(i, ma, g.m_observation);
		}		
		g.setM_A(ma);
	}
	
	/*Generate unobservation propagation matrix. */
	public void genUnPropagationMatix(){
		double [][] mb;
		mb = new double[g.getM_U()][];
		for(int j = 0; j < g.getM_U(); j++){
			mb[j] = new double[g.getM_N()];
			calDistance(j, mb, g.m_unobservation);
		}
		g.setM_B(mb);
	}

	/* Calculate the distance from one observation to the other nodes. */
	private void calDistance(int k, double[][] A, Vector<Integer> nodes_v) {
		// TODO Auto-generated method stub
		for(int i = 0; i < g.getM_nodes().size(); i++){
			g.getM_nodes().get(i).setState(0);
		}
		int node_id = nodes_v.get(k);
		Queue qu = new LinkedList<Integer>();
		qu.add(node_id);
		g.getM_nodes().get(node_id).setDepth(0);
		g.getM_nodes().get(node_id).setState(1);
		A[k][node_id] = 0;
		while(qu.isEmpty() == false){
			int id = (Integer) qu.element();
			qu.remove();
			int pre_depth = g.getM_nodes().get(id).getDepth();
			for(int j = 0; j < m_edges.get(id).size(); j++){
				if(g.getM_nodes().get(m_edges.get(id).get(j).getTo_id()).getState() == 0){
					g.getM_nodes().get(m_edges.get(id).get(j).getTo_id()).setState(1);
					g.getM_nodes().get(m_edges.get(id).get(j).getTo_id()).setDepth(pre_depth+1);
					A[k][m_edges.get(id).get(j).getTo_id()] = (pre_depth+1)*(g.getM_delay_mu());
					//System.out.println(m_edges.get(id).get(j).getTo_id());
					//System.out.println(A[k][m_edges.get(id).get(j).getTo_id()]+"^^^"+pre_depth+"  "+m_edges.get(id).get(j).getTo_id()+" "+g.getM_delay_mu());
					qu.add(m_edges.get(id).get(j).getTo_id());
				}
			}
		}
	}
	
	/* Calculate theta with method which implemented by matlab. */
	public void offlineTheta(){
		double[] m_theta = new double[g.getM_N()];
		int s_A = g.getM_O()*g.getM_N();
		int[] sA = {g.getM_O(), g.getM_N()};
		int s_B = g.getM_U()*g.getM_N();
		int[] sB = {g.getM_U(), g.getM_N()};
		double A[] = new double[s_A];
		double B[] = new double[s_B];
		double y[] = new double[g.getM_O()];
		int[] sy = {1, g.getM_O()};
		//int[] sl = {1, 1};
		//int[] se = {1, g.getM_N()};
		Object[] result = null;
		int cnt = 0;
		for(int i = 0; i < g.getM_N(); i++){
			for(int j = 0; j < g.getM_O(); j++){
				A[cnt] = g.getM_A()[j][i];
				cnt++;
			}
		}
		cnt = 0;
		for(int i = 0; i < g.getM_N(); i++){
			for(int j = 0; j < g.getM_U(); j++){
				B[cnt] = g.getM_B()[j][i];
				cnt++;
			}
		}
		for(int i = 0; i < g.getM_O(); i++){
			y[i] = g.getM_nodes().get(g.getM_observation().get(i)).getTime() - g.getM_begin_time();
		}
		
		g.setM_epsilon(0.00000001);
		g.setM_lambda(0);
		//MWNumericArray mwA = new MWNumericArray(g.getM_O(), g.getM_N(), MWClassID.DOUBLE);
		//MWNumericArray mwB = new MWNumericArray(g.getM_U(), g.getM_N(), MWClassID.DOUBLE);

		MWNumericArray mwlambda = new MWNumericArray(g.getMu(), MWClassID.DOUBLE);
		MWNumericArray mwepsilon = new MWNumericArray(0.5, MWClassID.DOUBLE);
		MWNumericArray mwtheta  = new MWNumericArray(1, g.getM_N(), MWClassID.DOUBLE);
		MWNumericArray mwA = MWNumericArray.newInstance(sA, A, MWClassID.DOUBLE);
		MWNumericArray mwB = MWNumericArray.newInstance(sB, B, MWClassID.DOUBLE);
		MWNumericArray mwy = MWNumericArray.newInstance(sy, y, MWClassID.DOUBLE);

		l1.whs ct = null;
		try {
			ct = new l1.whs();
			//result = ct.calTheta_whs(1, mwA, mwB, mwy, mwlambda, mwepsilon);
//			System.out.println(mwA);
//			System.out.println(mwy);
			result = ct.calTheta_whs(1,mwA, mwB, mwy, mwlambda);
			//System.out.println(result[0]);
			List<Integer> l = new ArrayList<Integer>();
			List<Integer> ll = new ArrayList<Integer>();
			List<String> thetaList = new ArrayList<String>();
			StringBuffer sb = new StringBuffer();
			MWNumericArray temp = (MWNumericArray)result[0];
			for(int i = 0; i < temp.toString().length(); i++){
				sb.append(temp.toString().charAt(i));
				if(temp.toString().charAt(i) != ' ')
					l.add(i);
			}
			ll.add(l.get(0));
			for(int j = 2; j < l.size(); j++){
				if(l.get(j) - l.get(j-1) != 1){
					ll.add(l.get(j-1));
					ll.add(l.get(j));
				}
			}
			if(ll.get(ll.size()-1)!=l.get(l.size()-1)){
				ll.add(l.get(l.size()-1));
			}

			if(ll.size()%2 == 0){
				for(int i = 0; i < ll.size()-1; i=i+2)
					thetaList.add(temp.toString().substring(ll.get(i), ll.get(i+1)));
			} else {
				for(int i = 0; i < ll.size()-2; i=i+2)
					thetaList.add(temp.toString().substring(ll.get(i), ll.get(i+1)));
				thetaList.add(temp.toString().substring(ll.get(ll.size()-1)));
			}

			for(int i = 0; i < thetaList.size(); i++){
				if(thetaList.get(i).contains("*")){
					i = i + 2;
					m_theta[i] = Double.parseDouble(thetaList.get(i));
				}
			}
				
			g.setM_theta(m_theta);
		} catch (MWException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			MWArray.disposeArray(mwA);
			MWArray.disposeArray(mwB);
			MWArray.disposeArray(mwy);
			MWArray.disposeArray(mwepsilon);
			MWArray.disposeArray(mwlambda);
			MWArray.disposeArray(result);
			if(ct != null)
				ct.dispose();
		}
	}
	
	public void onlineTheta(){
		long startTime;
		long endTime;
		int s_A = g.getM_O()*g.getM_N();
		double A[] = new double[s_A];
		double y[] = new double[g.getM_O()];
		int[] sA = {g.getM_O(), g.getM_N()};
		int[] sy = {1, g.getM_O()};
		Object[] onlineResult = null;
		String fileName = "onlineTheta.txt";
		double[][] onlineTheta;
		double[] m_theta = new double[g.getM_N()];
		int cnt = 0;
		
		for(int i = 0; i < g.getM_N(); i++){
			for(int j = 0; j < g.getM_O(); j++){
				A[cnt] = g.getM_A()[j][i];
				cnt++;
			}
		}
		for(int i = 0; i < g.getM_O(); i++){
			y[i] = g.getM_nodes().get(g.getM_observation().get(i)).getTime() - g.getM_begin_time();
		}

		MWNumericArray mwA = MWNumericArray.newInstance(sA, A, MWClassID.DOUBLE);
		MWNumericArray mwy = MWNumericArray.newInstance(sy, y, MWClassID.DOUBLE);
		StringBuffer s = new StringBuffer();
		StringBuffer stime = new StringBuffer();
		startTime = System.currentTimeMillis();
		matlabJar.whs ol = null;
		try {
			ol = new matlabJar.whs();
			Object[] o = null;
			
			for(int i = 1; i < g.m_O; i++){
				o = ol.testVal(1, i, mwA, mwy, g.getEta());
				List<Integer> l = new ArrayList<Integer>();
				List<Integer> ll = new ArrayList<Integer>();
				List<String> thetaList = new ArrayList<String>();
				StringBuffer sb = new StringBuffer();
				MWNumericArray temp = (MWNumericArray)o[0];
				for(int j = 0; j < temp.toString().length(); j++){
					sb.append(temp.toString().charAt(j));
					if(temp.toString().charAt(j) != ' ')
						l.add(j);
				}
				ll.add(l.get(0));
				for(int j = 2; j < l.size(); j++){
					if(l.get(j) - l.get(j-1) != 1){
						ll.add(l.get(j-1));
						ll.add(l.get(j));
					}
				}
				if(ll.get(ll.size()-1)!=l.get(l.size()-1)){
					ll.add(l.get(l.size()-1));
				}

				if(ll.size()%2 == 0){
					for(int ii = 0; ii < ll.size()-1; ii=ii+2)
						thetaList.add(temp.toString().substring(ll.get(ii), ll.get(ii+1)));
				} else {
					for(int ii = 0; ii < ll.size()-2; ii=ii+2)
						thetaList.add(temp.toString().substring(ll.get(ii), ll.get(ii+1)));
					thetaList.add(temp.toString().substring(ll.get(ll.size()-1)));
				}

				for(int ii = 0; ii < thetaList.size(); ii++)
					m_theta[ii] = Double.parseDouble(thetaList.get(ii));
				onlineTopUser(g.m_K, m_theta);
				endTime = System.currentTimeMillis();
				double time = endTime - startTime;
				stime.append("running time: "+time);
				s.append("Online Average Distance: "+onlineAveDistance()+"  active time: "+g.getM_nodes().get(g.getM_detector().get(i)).getTime());
				s.append("\r\n");
				stime.append("\r\n");
				System.out.println("Online Average Distance: "+onlineAveDistance());
				g.m_onlineTheta.clear();
			}
			startTime = System.currentTimeMillis();
			offlineTheta();
			offlineTopUser(g.m_K);
			
			s.append("offline Average Distance: "+offlineAveDistance());
			g.setOnlineStringBuffer(s);
			endTime = System.currentTimeMillis();
			double time = endTime - startTime;
			stime.append("offline running time: "+time);
			g.setRunningTimeBuffer(stime);
			System.out.println("offline Average Distance: "+offlineAveDistance());
			FileWriter writer = new FileWriter(fileName);
			writer.write(s.toString());
			writer.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		 finally {
			MWArray.disposeArray(mwA);
			MWArray.disposeArray(mwy);
			MWArray.disposeArray(onlineResult);
			if(ol != null)
				ol.dispose();
		}
		
	}
	
	public StringBuffer returnOnlineStringBuffer(){
		return g.getOnlineStringBuffer();
	}
	
	public StringBuffer returnRunningTimeBuffer(){
		return g.getRunningTimeBuffer();
	}
	
	/*Print theta.*/
	public void printTheta(){
		System.out.println("-----------theta-------------");
		for(int i = 0; i < g.getM_N(); i++){
			if(g.getM_theta()[i] != 0)
				System.out.println(i);
		}
	}
	
	/* Get the most probobly sources. */
	public int offlineTopUser(int k){
		if(k > g.getM_N())
			return -1;
		List<Pair> resultList = new ArrayList<Pair>();
		int tk = 0;
		List<Integer> result = new ArrayList<Integer>();
		for(int n = 0; n < g.getM_N(); n++){
			Pair a = new Pair();
			a.n = n;
			a.v = Math.abs(g.getM_theta()[n]);
			resultList.add(a);
		}
		Comparator comp = new DesComparator();
		Collections.sort(resultList, comp);
		for(int i = 0; i < resultList.size(); i++){
			Pair p = resultList.get(i);
			//System.out.println(p.v+"---"+p.n);
		}
		for(int i = 0; i < resultList.size(); i++){
			if(tk < k)
				result.add(resultList.get(i).n);
			else
				break;
			tk++;
		}
		g.setM_result(result);
		return 0;
	}
	
	public int onlineTopUser(int k, double[] theta){
		if(k > g.getM_N())
			return -1;
		List<Pair> resultList = new ArrayList<Pair>();
		int tk = 0;
		List<Integer> result = new ArrayList<Integer>();
		for(int n = 0; n < g.getM_N(); n++){
			Pair a = new Pair();
			a.n = n;
			a.v = Math.abs(theta[n]);
			resultList.add(a);
		}
		Comparator comp = new DesComparator();
		Collections.sort(resultList, comp);
		for(int i = 0; i < resultList.size(); i++){
			Pair p = resultList.get(i);
			//System.out.println(p.v+"---"+p.n);
		}
		for(int i = 0; i < resultList.size(); i++){
			if(tk < k)
				result.add(resultList.get(i).n);
			else
				break;
			tk++;
		}
		g.setM_onlineTheta(result);
		return 0;
	}
	
	/*Get the precise of the result. */
	public double getPrecise(){
		int count = 0;
		double precise;
		for(int i = 0; i < g.getM_K(); i++){
			if(g.getM_nodes().get(g.getM_result().get(i)).ifSource == true)
				count++;
		}
		precise = count/g.getM_K();
		return precise*100;
	}
	
	/* Find the closest source and calculate hops.
	   'r' is the start point;
	   'ps' is the found closest source;
	   'pd' is the distance;
	   'visited' is the visited source. */
	public int calHops(int r, int ps, int pd, Set<Integer> visited){
		for(int i = 0; i < g.m_nodes.size(); i++)
			g.getM_nodes().get(i).setState(0);
		if(g.getM_nodes().get(r).ifSource == true){
			ps = r;
			pd = 0;
			visited.add(r);
			return pd;
		}
		Queue q = new LinkedList<Integer>(); //infected nodes
		q.add(r);
		g.getM_nodes().get(r).setDepth(0);
		g.getM_nodes().get(r).setState(1);
		
		while(q.isEmpty() == false){
			int id = (Integer) q.element();
			q.remove();
			int pre_depth = g.getM_nodes().get(id).depth;
			for(int j = 0; j < m_edges.get(id).size(); j++){
				if(g.getM_nodes().get(m_edges.get(id).get(j).getTo_id()).getState() == 0){
					if(g.getM_nodes().get(m_edges.get(id).get(j).getTo_id()).ifSource == true){
						ps = m_edges.get(id).get(j).getTo_id();
						pd = pre_depth + 1;
						//System.out.println(pd+"====");
						visited.add(m_edges.get(id).get(j).getTo_id());
						return pd;
					}
					g.getM_nodes().get(m_edges.get(id).get(j).getTo_id()).setState(1);
					g.getM_nodes().get(m_edges.get(id).get(j).getTo_id()).setDepth(pre_depth+1);
					q.add(m_edges.get(id).get(j).getTo_id());
				}
			}
				
		}	
		return pd;
	}
	
	public double offlineAveDistance(){
		Set<Integer> visited = new HashSet<Integer>();
		visited.clear();
		Vector<Integer> dis;
		int source = 0;
		int distance;
		double s = 0;
		double ave_dis;
		for(int i = 0; i < g.getM_result().size(); i++){
			distance = 0;
			distance = calHops(g.getM_result().get(i), source, distance, visited);
			s = s + distance;
		}
		//System.out.println(s+"---"+g.getM_result().size());
		ave_dis = s / g.getM_result().size();
		return ave_dis;
	}
	
	public double onlineAveDistance(){
		Set<Integer> visited = new HashSet<Integer>();
		visited.clear();
		int source = 0;
		int distance;
		double s = 0;
		double ave_dis;
		for(int i = 0; i < g.getM_onlineTheta().size(); i++){
			distance = 0;
			distance = calHops(g.getM_onlineTheta().get(i), source, distance, visited);
			s = s + distance;
		}
		ave_dis = s / g.getM_onlineTheta().size();
		return ave_dis;
	}
	
}
