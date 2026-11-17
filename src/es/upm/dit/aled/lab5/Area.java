package es.upm.dit.aled.lab5;

import java.awt.Color;
import java.util.Objects;

import es.upm.dit.aled.lab5.gui.Position2D;

/**
 * Models an area in a hospital ER. Each Area is characterized by its name
 * (which must be unique), the time a patient must spend there to be treated (in
 * milliseconds), the capacity (maximum number of patients that can be treated
 * at the same time), the number of patients being treated at a given time, and
 * the number of patients currently waiting.
 * 
 * This class is a monitor that ensures that there is a thread safe way to enter
 * and exit the area.
 * 
 * Each Area is represented graphically by a square of side 120 px, centered in
 * a given position and with a custom color.
 * 
 * @author rgarciacarmona
 */
public class Area {

	protected String name;
	private int time;
	private Position2D position;
	private Color color;
	protected int capacity;
	protected int numPatients;
	protected int waiting;

	public Area(String name, int time, int capacity, Position2D position) {
		this.color = Color.GRAY; // Default color
		this.name = name;
		this.time = time;
		this.capacity = capacity;
		//---
		this.numPatients = 0;
		this.waiting = 0;
		//---
		this.position = position;
	}

	public String getName() {
		return name;
	}

	public int getTime() {
		return time;
	}

	public Position2D getPosition() {
		return position;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public int getNumPatients() {
		return this.numPatients;
	}
	
	public int getWaiting() {
		return this.waiting;
	}
	
	public synchronized void enter(Patient p) {
		waiting++;
		while(numPatients >= capacity) {
			try {
				wait();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}		
		}
		numPatients++;
		waiting--;
	}
	
	public synchronized void exit(Patient p) {
		numPatients--;
		notifyAll();
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Area other = (Area) obj;
		return Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return name;
	}
}