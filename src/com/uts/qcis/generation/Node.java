package com.uts.qcis.generation;

import java.util.ArrayList;
import java.util.List;

public class Node 
{
  public char label;
  public boolean visited=false;
  
  public boolean ifSource;
  public boolean ifDetected;
  public boolean ifObserved;
  public boolean ifUnobserved;
  public int depth; //Depth from the source
  public int id;
  public int state;
  public double time;
  public List<Edge> friends;
  //List<Edge> friendsList;
  
  public Node(){
	  
  }
  
  public boolean isIfDetected() {
	return ifDetected;
}

public void setIfDetected(boolean ifDetected) {
	this.ifDetected = ifDetected;
}

public boolean isIfSource() {
	return ifSource;
}

public int getDepth() {
	return depth;
}


public void setDepth(int depth) {
	this.depth = depth;
}


public void setIfSource(boolean ifSource) {
	this.ifSource = ifSource;
}


public boolean isIfObserved() {
	return ifObserved;
}


public void setIfObserved(boolean ifObserved) {
	this.ifObserved = ifObserved;
}


public boolean isIfUnobserved() {
	return ifUnobserved;
}


public void setIfUnobserved(boolean ifUnobserved) {
	this.ifUnobserved = ifUnobserved;
}


public int getId() {
	return id;
}


public void setId(int id) {
	this.id = id;
}


public int getState() {
	return state;
}


public void setState(int state) {
	this.state = state;
}


public double getTime() {
	return time;
}


public void setTime(double time) {
	this.time = time;
}

public Node(char l)
  {
    this.label=l;
  }

public List<Edge> getFriends() {
	return friends;
}
public void setFriends(Edge e) {
	friends.add(e);
}
}
