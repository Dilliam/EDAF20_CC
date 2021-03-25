package accounts;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import spark.Request;
import spark.Response;

public class Database {
	private Connection connection;

	public void connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(
							"jdbc:mysql://localhost/niklas?user=niklas&password=&useSSL=false&autoReconnect=true");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public String getAccounts(Request req, Response res) {
		try (Statement statement = connection.createStatement()) {
			String sql = "SELECT * FROM accounts";
			ResultSet resultSet = statement.executeQuery(sql);
			return JSONizer.toJSON(resultSet, "accounts");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public String getAccount(Request req, Response res) {
		if (req.params("id") != null) {
			String sql = "SELECT * FROM accounts WHERE accountNo = ?";
			try (PreparedStatement ps = connection.prepareStatement(sql)) {
				ps.setString(1, req.params("id"));
				ResultSet resultSet = ps.executeQuery();
				return JSONizer.toJSON(resultSet, "account");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	public String updateAccount(Request req, Response res) {
		String id = req.params("id");
		String amount = req.queryParams("amount");
		if (id != null && amount != null) {
			String sql = "UPDATE accounts SET balance = balance + ? WHERE accountNo = ?";
			try (PreparedStatement ps = connection.prepareStatement(sql)) {
				ps.setInt(1, Integer.parseInt(amount));
				ps.setString(2, id);
				int affectedRows = ps.executeUpdate();
				if (affectedRows > 0) {
					return "{ \"status\": \"ok\" }\n";
				} else {
					return "{ \"status\": \"error\" }\n";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "{ \"status\": \"error\" }\n";
	}
}
