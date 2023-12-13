package databaseConnection;

public class Test {
	public static void main(String[] args) {
		Database db = new Database();
		System.out.println(db.findAll());
		System.out.println(db.findByName("Nguyễn Bính"));
	}
}
