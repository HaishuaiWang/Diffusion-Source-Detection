package com.uts.qcis.generation;

import java.io.FileWriter;
import java.io.IOException;

public class MainEntry {
	
	public int observationNumber;
	public static StringBuffer sb = new StringBuffer();
	public static StringBuffer sbTime = new StringBuffer();
	
	public int getObservationNumber() {
		return observationNumber;
	}

	public void setObservationNumber(int observationNumber) {
		this.observationNumber = observationNumber;
	}

	public double traceSources(int sourceNumber, double alpha, double timeWindow, int mu, double eta){
		
		//String fn = "D:\\Haishuai\\Databases\\SocialNetworks\\Synthetic-Graphs\\Forestfire\\forestfire_1000.dat";
		//String fn = "D:\\Haishuai\\Databases\\SocialNetworks\\Synthetic-Graphs\\Albert-Barabasi\\Albert_Barabasi_1000.txt";
		//String fn = "C:\\Users\\Haishuai\\Desktop\\SNS\\data\\graph\\small_world\\graph_small_world_1000.txt";
		//String fn = "D:\\Haishuai\\Databases\\SocialNetworks\\Synthetic-Graphs\\Erdos_Renyi\\Erdos_Renyi_1000.txt";
		//String fn = "D:\\Haishuai\\Databases\\SocialNetworks\\Real-World\\newTwitter.txt";
		String fn = "D:\\Haishuai\\Databases\\SocialNetworks\\Real-World\\newFacebook_1000.txt";
		//String fn = "D:\\Haishuai\\Databases\\SocialNetworks\\Real-World\\SinaWeibo\\part\\newWeibo_500.txt";
		Diffusion df = new Diffusion();
		
		df.setD(1, 0.001);
		
		//STEP I: Read the graph, and generate start time as 0;
		df.getGraph(fn);
		
		//STEP II: Sample source;
		df.generateRandomSource(sourceNumber);
		
		//STEP III: Generate propagation path and propagation time;
		df.simulateDiffusion();
		//df.findPropTimeWindow();
		
		//STEP IV: Sample observation and unobservation;
		df.sampleObservation(alpha);
		df.setTimeWindow(timeWindow);
		df.timeWindow();
		this.setObservationNumber(df.observationNumber());
		
		//STEP V: Generate propagation matrix;
		df.genPropagationMatirx();
		df.genUnPropagationMatix();
		
		//STEP VI: Calculate theta;
		df.setL1_mu(mu);
		df.setL1_eta(eta);
		//df.offlineTheta();
		df.onlineTheta();
		//df.offlineTopUser(sourceNumber);
		//System.out.println("The precise is: "+df.getPrecise()+"%");
		//System.out.println("Average Distance: "+df.offlineAveDistance());
		sb = df.returnOnlineStringBuffer();
		sbTime = df.returnRunningTimeBuffer();
		return df.offlineAveDistance();
	}
	
	public static void main(String[] args) throws IOException{	
		long startTime;
		long endTime;
		startTime = System.currentTimeMillis();
		MainEntry me = new MainEntry();
		
		String culpritFile = "culpritsTest.txt";
		String detectorFile = "detectorsTest.txt";
		String timeWindowFile = "timeWindowTest.txt";
		String muFile = "muTest.txt";
		String learningRateFile = "etaTest.txt";
		String runningTime = "runningTime.txt";
		double[] alphaArray = {0.05, 0.1, 0.15, 0.2, 0.25, 0.3, 0.35, 0.4, 0.5};
		double[] time_windowArray = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		int[] sourceNumberArray = {1, 2, 3, 4, 5};
		int[] muArray = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 15, 20, 25, 30, 35, 40, 45, 50};
		double[] learningRate = {0.1, 0.2, 0.3, 0.4, 0.5};
		StringBuffer s1 = new StringBuffer();
		StringBuffer s2 = new StringBuffer();
		StringBuffer s3 = new StringBuffer();
		StringBuffer s4 = new StringBuffer();
		StringBuffer s5 = new StringBuffer();
		StringBuffer s6 = new StringBuffer();
		s1.append("# of culprits testing...!\n\r").append("\r\n");
		s2.append("# of detectors testing...!").append("\r\n");
		s3.append("Monitoring time window testing...!").append("\r\n");
		s4.append("Parameter mu testing...!").append("\r\n");
		s5.append("Parameter learningRate testing...!").append("\r\n");
		s6.append("Running time...!").append("\r\n");
		FileWriter writer;
		
		//Table 1: # of culprits testing.
//		for(int i = 0; i < sourceNumberArray.length; i++){
//			int sourceNumber = sourceNumberArray[i];
//			double alpha = 0.2;
//			double timeWindow = 3; 
//			int mu = 1;
//			s1.append("\r\n");
//			s1.append("Average Distance: "+ me.traceSources(sourceNumber, alpha, timeWindow, mu, 0.1)+" # of observations: "+me.getObservationNumber());
//		}
//		
//		writer = new FileWriter(culpritFile);
//		writer.write(s1.toString());
//		writer.close();
		
		//Table 2: # of detectors testing.
//		for(int i = 0; i < alphaArray.length; i++){
//			double alpha = alphaArray[i];
//			int sourceNumber = 2;
//			double timeWindow = 3;
//			int mu = 1;
//			s2.append("\r\n");
//			s2.append("Average Distance: "+ me.traceSources(sourceNumber, alpha, timeWindow, mu, 0.1)+" # of observations: "+me.getObservationNumber());
//		}
		
//		writer = new FileWriter(detectorFile);
//		writer.write(s2.toString());
//		writer.close();
		
		//Table 3: Monitoring time window testing.
//		for(int i = 0; i < time_windowArray.length; i++){
//			double alpha = 0.3;
//			int sourceNumber = 2;
//			int mu = 1;
//			double timeWindow = time_windowArray[i];
//			s3.append("\r\n");
//			s3.append("Average Distance: "+ me.traceSources(sourceNumber, alpha, timeWindow, mu, 0.1)+" # of observations: "+me.getObservationNumber());
//		}
//		
//		writer = new FileWriter(timeWindowFile);
//		writer.write(s3.toString());
//		writer.close();
//		
//		//Table 4: Parameter mu testing.
//		for(int i = 0; i < muArray.length; i++){
//			double alpha = 0.2;
//			int sourceNumber = 2;
//			double timeWindow = 3;
//			double eta = 0.1;
//			s4.append("\r\n");
//			s4.append("Average Distance: "+ me.traceSources(sourceNumber, alpha, timeWindow, muArray[i], eta)+" # of observations: "+me.getObservationNumber());
//		}
		
		//Table 5: Parameter learningRate testing.
//		for(int i = 0; i < learningRate.length; i++){
//			double alpha = 0.2;
//			int sourceNumber = 1;
//			double timeWindow = 2;
//			int mu = 1;
//			System.out.println("learning rate: "+learningRate[i]);
//			me.traceSources(sourceNumber, alpha, timeWindow, mu, learningRate[i]);
//			s5.append("\r\n");
//			s5.append("learning rate: "+learningRate[i]);
//			s5.append("\r\n");
//			s5.append(sb);
//		}
		
//		double alpha = 0.2;
//		int sourceNumber = 2;
//		double timeWindow = 3;
//		me.traceSources(sourceNumber, alpha, timeWindow, 1);

		//Table 6: Running time.
		double alpha = 0.2;
		int sourceNumber = 2;
		double timeWindow = 2;
		int mu = 1;

		me.traceSources(sourceNumber, alpha, timeWindow, mu, learningRate[0]);
		endTime = System.currentTimeMillis();
		double time = endTime - startTime;
		System.out.println("running time: "+time);
		s6.append("\r\n");
		s6.append("running time: "+time);
		s6.append("\r\n");
		s6.append(sbTime);
		
		writer = new FileWriter(runningTime);
		writer.write(s6.toString());
		writer.close();
		
	}
}
