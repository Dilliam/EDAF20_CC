package accounts;

import static spark.Spark.*;

public class RESTServer {
	public static int PORT = 8888;
	
	public static void main(String[] args) throws InterruptedException {
		Database db = new Database();
		db.connect();

		port(PORT);
		
		get("/accounts", (req, res) -> db.getAccounts(req, res));
		get("/accounts/:id", (req, res) -> db.getAccount(req, res));
		
		patch("/accounts/:id", (req, res) -> db.updateAccount(req, res));
	}
}