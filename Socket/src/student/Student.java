package student;

public class Student {
	private int id;
	private String name;
	private int year;
	private double avg;

	public Student(int id, String name, int year, double avg) {

		this.id = id;
		this.name = name;
		this.year = year;
		this.avg = avg;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public double getAvg() {
		return avg;
	}

	public void setAvg(double avg) {
		this.avg = avg;
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", name=" + name + ", year=" + year + ", avg=" + avg + "]";
	}
}
