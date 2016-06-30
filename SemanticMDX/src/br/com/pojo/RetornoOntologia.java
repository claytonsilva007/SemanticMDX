package br.com.pojo;

import java.util.ArrayList;

public class RetornoOntologia {
	public ArrayList<String> instances;
	public ArrayList<String> classes;
	public ArrayList<String> superClasses;
	public ArrayList<String> equivalentClasses;
	
	public ArrayList<String> getInstances() {
		return instances;
	}
	public void setInstances(ArrayList<String> instances) {
		this.instances = instances;
	}
	public ArrayList<String> getClasses() {
		return classes;
	}
	public void setClasses(ArrayList<String> classes) {
		this.classes = classes;
	}
	public ArrayList<String> getSuperClasses() {
		return superClasses;
	}
	public void setSuperClasses(ArrayList<String> superClasses) {
		this.superClasses = superClasses;
	}
	public ArrayList<String> getEquivalentClasses() {
		return equivalentClasses;
	}
	public void setEquivalentClasses(ArrayList<String> equivalentClasses) {
		this.equivalentClasses = equivalentClasses;
	}
}
