import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class problem5_1 {

	public static Connection dbconn = null;

	public static void main(String[] argv) throws SQLException {

		/*
		 * this driver are avaliable at http://jdbc.postgresql.org it is also
		 * included in the zip package.
		 * 
		 * to compile this program you have to download and add it your
		 * buildpath.
		 * 
		 * If everything fails and executable are available at
		 * http://qwa.dk/problem5_1.jar
		 */

		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}

		/*
		 * Establishing connection
		 */

		try {
			dbconn = DriverManager.getConnection(
					"jdbc:postgresql://178.63.131.184:5432/dbwa2", "dbwa2",
					"diku2012");
		} catch (SQLException e) {
			System.out
					.println("Your internet connection or my server is dead.. :(");
			return;
		}

		/*
		 * If connection were successfully established, then run
		 * printCategoryTable(); and afterwards close the connection.
		 */

		if (dbconn != null) {
			System.out
					.println("Fetching and formating data, please wait a few seconds!!\n\n");
			printCategoryTable();
			dbconn.close();
		} else {
			System.out.println("Failed to make connection!");

		}
	}

	public static void printCategoryTable() throws SQLException {

		/*
		 * Create an int array, representing the (length+10) of the longest
		 * element in every column.
		 */
		PreparedStatement stmtGetLengthOfColumns = dbconn
				.prepareStatement("SELECT max(character_length((cast(categoryid as text)))) as lenid, max(character_length(trim(both ' ' from categoryname))) as lenname,max(character_length(trim(both ' ' from description))) as lendes FROM nw_category");
		ResultSet rsGetLengthOfColumns = stmtGetLengthOfColumns.executeQuery();

		int[] columnLengths = new int[3];

		rsGetLengthOfColumns.next();

		for (int i = 0; 3 > i; i++) {
			columnLengths[i] = rsGetLengthOfColumns.getInt(i + 1) + 10;
		}

		/*
		 * Executing the query to get the categories and resturnning data and
		 * metadata to resultsets.
		 */
		PreparedStatement pstmt = dbconn
				.prepareStatement("SELECT categoryid,categoryname,description from nw_category");
		ResultSet rs = pstmt.executeQuery();
		ResultSetMetaData rsmd = rs.getMetaData();

		/*
		 * Printf ColumnLabels
		 */
		for (int i = 0; rsmd.getColumnCount() > i; i++) {
			System.out.printf("|%-" + columnLengths[i] + "S",
					rsmd.getColumnLabel(i + 1));
		}
		System.out.print("|\n");

		/*
		 * Printf seperator between data and metadata
		 */
		for (int i = 0; (rsmd.getColumnCount()) > i; i++) {

			String sep = "";

			for (int j = 0; j < columnLengths[i]; j++) {
				sep = sep + "-";
			}
			System.out.print("+" + sep);
		}
		System.out.print("+\n");

		/*
		 * Printf data;
		 */

		while (rs.next()) {

			for (int i = 0; rsmd.getColumnCount() > i; i++) {
				System.out.printf("|%-" + columnLengths[i] + "s",
						rs.getString(i + 1));
			}
			System.out.print("|\n");
		}
	}
}
