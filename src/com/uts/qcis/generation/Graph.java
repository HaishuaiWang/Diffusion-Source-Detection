package com.uts.qcis.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Graph {
	
	public int m_N; //Number of nodes in graph
	public int m_K; //Number of seed
	public int m_D; //Number of detectors
	public int m_O; //Number of observation
	public int m_U; //Number of unobservation
	public int m_ob_th;
	public int m_unob_th;
	public int mu;
	
	public Vector<Node> m_nodes = new Vector<Node>();
	public Vector<Integer> m_sources = new Vector<Integer>();
	public Vector<Double> m_delay;
	public Vector<Integer> m_detector = new Vector<Integer>();
	public Vector<Integer> m_observation = new Vector<Integer>();
	public Vector<Integer> m_unobservation = new Vector<Integer>();
	public List<Integer> m_result = new ArrayList<Integer>(); //The source
	public List<Integer> m_onlineTheta = new ArrayList<Integer>();
	
	public double m_precise;
	public double m_ave_dis;
	public double m_delay_mu;
	public double m_delay_sigma;
	public double m_A[][];
	public double m_B[][];
	public double m_theta[];
	public double m_begin_time;
	public double m_window;
	public double m_lambda;
	public double m_epsilon;
	public double eta;
	
	public StringBuffer onlineStringBuffer = new StringBuffer();
	public StringBuffer runningTimeBuffer = new StringBuffer();
	public List<Edge> friends;
	
	public int getM_N() {
		return m_N;
	}
	public void setM_N(int m_N) {
		this.m_N = m_N;
	}
	public int getM_K() {
		return m_K;
	}
	public void setM_K(int m_K) {
		this.m_K = m_K;
	}
	public int getM_O() {
		return m_O;
	}
	public void setM_O(int m_O) {
		this.m_O = m_O;
	}
	public Vector<Node> getM_nodes() {
		return m_nodes;
	}
	public void setM_nodes(Node n) {
		m_nodes.add(n);
	}
	public Vector<Integer> getM_sources() {
		return m_sources;
	}
	public void setM_sources(Vector<Integer> m_sources) {
		this.m_sources = m_sources;
	}
	public double getM_delay_mu() {
		return m_delay_mu;
	}
	public void setM_delay_mu(double m_delay_mu) {
		this.m_delay_mu = m_delay_mu;
	}
	public double getM_delay_sigma() {
		return m_delay_sigma;
	}
	public void setM_delay_sigma(double m_delay_sigma) {
		this.m_delay_sigma = m_delay_sigma;
	}
	public Vector<Integer> getM_observation() {
		return m_observation;
	}
	public void setM_observation(Vector<Integer> m_observation) {
		this.m_observation = m_observation;
	}
	public double[][] getM_A() {
		return m_A;
	}
	public void setM_A(double[][] m_A) {
		this.m_A = m_A;
	}
	public int getM_U() {
		return m_U;
	}
	public void setM_U(int m_U) {
		this.m_U = m_U;
	}
	public int getM_ob_th() {
		return m_ob_th;
	}
	public void setM_ob_th(int m_ob_th) {
		this.m_ob_th = m_ob_th;
	}
	public int getM_unob_th() {
		return m_unob_th;
	}
	public void setM_unob_th(int m_unob_th) {
		this.m_unob_th = m_unob_th;
	}
	public Vector<Integer> getM_unobservation() {
		return m_unobservation;
	}
	public void setM_unobservation(Vector<Integer> m_unobservation) {
		this.m_unobservation = m_unobservation;
	}
	public double getM_window() {
		return m_window;
	}
	public void setM_window(double m_window) {
		this.m_window = m_window;
	}
	public double[][] getM_B() {
		return m_B;
	}
	public void setM_B(double[][] m_B) {
		this.m_B = m_B;
	}
	public double getM_begin_time() {
		return m_begin_time;
	}
	public void setM_begin_time(double m_begin_time) {
		this.m_begin_time = m_begin_time;
	}
	public double getM_lambda() {
		return m_lambda;
	}
	public void setM_lambda(double m_lambda) {
		this.m_lambda = m_lambda;
	}
	public double getM_epsilon() {
		return m_epsilon;
	}
	public void setM_epsilon(double m_epsilon) {
		this.m_epsilon = m_epsilon;
	}
	public double[] getM_theta() {
		return m_theta;
	}
	public void setM_theta(double[] m_theta) {
		this.m_theta = m_theta;
	}
	public Vector<Integer> getM_detector() {
		return m_detector;
	}
	public void setM_detector(Vector<Integer> m_detector) {
		this.m_detector = m_detector;
	}
	public int getM_D() {
		return m_D;
	}
	public void setM_D(int m_D) {
		this.m_D = m_D;
	}
	public List<Integer> getM_result() {
		return m_result;
	}
	public void setM_result(List<Integer> m_result) {
		this.m_result = m_result;
	}
	public List<Integer> getM_onlineTheta() {
		return m_onlineTheta;
	}
	public void setM_onlineTheta(List<Integer> m_onlineTheta) {
		this.m_onlineTheta = m_onlineTheta;
	}
	public int getMu() {
		return mu;
	}
	public void setMu(int mu) {
		this.mu = mu;
	}
	public double getEta() {
		return eta;
	}
	public void setEta(double eta) {
		this.eta = eta;
	}
	public StringBuffer getOnlineStringBuffer() {
		return onlineStringBuffer;
	}
	public void setOnlineStringBuffer(StringBuffer onlineStringBuffer) {
		this.onlineStringBuffer = onlineStringBuffer;
	}
	public StringBuffer getRunningTimeBuffer() {
		return runningTimeBuffer;
	}
	public void setRunningTimeBuffer(StringBuffer runningTimeBuffer) {
		this.runningTimeBuffer = runningTimeBuffer;
	}
	
}
