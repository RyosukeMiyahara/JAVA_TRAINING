package ex03_01;

public class PassengerVehicle extends Vehicle {
	private int sheet;
	private int passenger;

	PassengerVehicle(int capacity, int people)
	{
		sheet = capacity;
		passenger = people;
	}

	public int getSheet()
	{
		return sheet;
	}

	public int getPassenger()
	{
		return passenger;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PassengerVehicle test1 = new PassengerVehicle(4, 3);
		System.out.println("Sheets number: " + test1.getSheet());
		System.out.println("Passenger number: " + test1.getPassenger());

		System.out.println("");

		PassengerVehicle test2 = new PassengerVehicle(8, 5);
		System.out.println("Sheets number: " + test2.getSheet());
		System.out.println("Passenger number: " + test2.getPassenger());

	}

}
