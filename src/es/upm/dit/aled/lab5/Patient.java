package es.upm.dit.aled.lab5;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import es.upm.dit.aled.lab5.gui.EmergencyRoomGUI;
import es.upm.dit.aled.lab5.gui.Position2D;

/**
 * Models a patient in a hospital ER. Each Patient is characterized by its
 * number (which must be unique), its current location and a protocol. The
 * protocol is a List of Transfers. Each Patient also has an index to indicate
 * at which point of the protocol they are at the current time.
 * 
 * Patients are Threads, and therefore must implement the run() method.
 * 
 * Each Patient is represented graphically by a dot of diameter 10 px, centered
 * in a given position and with a custom color.
 * 
 * @author rgarciacarmona
 */
public class Patient extends Thread {

	private int number;
	private List<Transfer> protocol;
	private int indexProtocol;
	private Area location;
	private Position2D position;
	private Color color;

	public Patient(int number, Area initialLocation) {
		this.number = number;
		this.protocol = new ArrayList<Transfer>();
		this.indexProtocol = 0;
		this.location = initialLocation;
		this.position = initialLocation.getPosition();
		this.color = Color.GRAY; // Default color
	}

	public int getNumber() {
		return number;
	}

	public List<Transfer> getProtocol() {
		return protocol;
	}

	public Area getLocation() {
		return location;
	}

	public void setLocation(Area location) {
		this.location = location;
	}
	
	public Position2D getPosition() {
		return position;
	}

	public void setPosition(Position2D position) {
		this.position = position;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void addToProtocol(Transfer transfer) {
		this.protocol.add(transfer);
	}

	private void advanceProtocol() {
		Transfer transfer = protocol.get(indexProtocol);
		indexProtocol++;
		System.out.println("Patient " + this.number + " is moving from " + this.location + " to " + transfer.getTo());
		EmergencyRoomGUI.getInstance().animateTransfer(this, transfer);
		this.location = transfer.getTo();
	}

	private void attendedAtLocation() {
		try {
			System.out.println("Patient " + this.number + " is being attended at " + this.location);
			sleep(this.location.getTime());
		} catch (InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt(); // Restore interrupted status
		}
	}

	@Override
	public void run() {
		location.enter(this);
		attendedAtLocation();
		location.exit(this);
		
		while(indexProtocol < protocol.size()) {
			advanceProtocol();
			this.location.enter(this);
			attendedAtLocation();
			this.location.exit(this);
		}
		EmergencyRoomGUI.getInstance().removePatient(this);
	}
}