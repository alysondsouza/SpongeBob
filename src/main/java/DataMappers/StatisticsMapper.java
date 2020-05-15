package DataMappers;

import Util.TimeCalc;

import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

public class StatisticsMapper {
    int db_id;
    String category = "";
    String discipline = "";

    TimeCalc calc = new TimeCalc();

    private static final String USERNAME = "guest";
    private static final String PASSWORD = "guest";
    private static final String CONN_STR = "jdbc:mysql://ali.cwic1cb4gyam.eu-central-1.rds.amazonaws.com";


    //2-3) REGISTER RESULTS
    public void registerDailyResultsIntoDb(int r_id, int id, String type, LocalDate date, int millisecond, String tournament, int qualification, String trainer) {

        try (
                Connection conn = DriverManager.getConnection(CONN_STR, USERNAME, PASSWORD);
                PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO sponge_bob.results (r_id, id, type, date, millisecond, tournament, qualification, trainer) values (?, ?, ?, ?, ?, ?, ?, ?)")
        ) {

            preparedStatement.setInt(1, r_id);
            preparedStatement.setInt(2, id);
            preparedStatement.setString(3, type);
            preparedStatement.setDate(4, Date.valueOf(date));
            preparedStatement.setInt(5, millisecond);
            preparedStatement.setNull(6, Types.VARCHAR); //setNull
            preparedStatement.setNull(7, Types.INTEGER); //setNull
            preparedStatement.setString(8, trainer);

            preparedStatement.execute();

        } catch (Exception e) {
            System.out.println("Query Error: " + e);
        }

        try (
                Connection conn = DriverManager.getConnection(CONN_STR, USERNAME, PASSWORD);
                Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = (stmt.executeQuery("SELECT * FROM sponge_bob.members WHERE id = " + id))
        ) {
            System.out.println("\nTRAINING RESULT ADDED TO:");
            while (rs.next()) {
                System.out.printf("ID: %-4d Name: %-30s  Category: %-6s  Discipline: %-10s\n", rs.getInt("id"), (rs.getString("name") + " " + rs.getString("surname")), rs.getString("category"), rs.getString("discipline"));
            }

        } catch (Exception e) {
            System.out.println("Query Error: " + e);
        }


    }

    //2-3a) FIND MEMBERSHIP
    public boolean findMemberByIdFromDb(int idx) {
        boolean validMember = true;
        try (
                Connection conn = DriverManager.getConnection(CONN_STR, USERNAME, PASSWORD);
                Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = (stmt.executeQuery("SELECT * FROM sponge_bob.members WHERE id = " + idx))
        ) {
            while (rs.next()) {
                System.out.printf("ID: %-4d Name: %-30s  Phone: %-10d Birthdate: %-10s  Category: %-6s  Modality: %-10s  Discipline: %-10s  Status: %-8s \n", rs.getInt("id"), (rs.getString("name") + " " + rs.getString("surname")), rs.getInt("phone"), rs.getDate("birthdate"), rs.getString("category"), rs.getString("modality"), rs.getString("discipline"), rs.getString("status"));
            }

            rs.first();
            String status = rs.getString("status");

            if (status.equals("Deleted")) {
                System.out.println("\nMember status: Deleted");
                validMember = false;
            } else if (status.equals("Inactive")) {
                System.out.println("\nMember status: Inactive");
                validMember = false;
            }

        } catch (Exception e) {
            System.out.println("Query Error: " + e);
        }
        return validMember;
    }

    //2-3b) READ FROM DB - GET NEW TRAINING REGISTER ID
    public int getNextTrainingIdFromDb() {
        try ( //'Connection', 'Statement' and 'ResultSet' are AUTO-CLOSABLE when with TRY-WITH-RESOURCES BLOCK (...)
              Connection conn = DriverManager.getConnection(CONN_STR, USERNAME, PASSWORD);
              Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
              ResultSet rs = (stmt.executeQuery("SELECT * FROM sponge_bob.results"))
        ) {
            rs.last();
            rs.getInt("r_id");
            db_id = rs.getInt("r_id") + 1;

        } catch (SQLException e) {
            System.out.println("Query Error: " + e);
        }
        return db_id;
    }


    //4) TOP 5
    public void top5ByCategoryDisciplineFromDb() {

        System.out.println("\nEnter the 'category' and the 'discipline' you wish to see the best results: ");

        Scanner scan = new Scanner(System.in);
        System.out.println("\nCategory: \n" +
                "1) For Junior Category\n" +
                "2) For Senior Category\n");
        int cat = scan.nextInt();
        switch (cat) {
            case 1:
                category = "Junior";
                break;
            case 2:
                category = "Senior";
                break;
            default:
                top5ByCategoryDisciplineFromDb();
                break;
        }

        System.out.println("\nDiscipline \n" +
                "1) For Butterfly.\n" +
                "2) For Crawl.\n" +
                "3) For Backstroke.\n" +
                "4) For Chest.\n");

        int dis = scan.nextInt();
        switch (dis) {
            case 1:
                discipline = "Butterfly";
                break;
            case 2:
                discipline = "Crawl";
                break;
            case 3:
                discipline = "Backstroke";
                break;
            case 4:
                discipline = "Chest";
                break;
            default:
                top5ByCategoryDisciplineFromDb();
                break;
        }

        System.out.println("\nTop 5: " + discipline + " - " + category);

        try (
                Connection conn = DriverManager.getConnection(CONN_STR, USERNAME, PASSWORD);
                Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = (stmt.executeQuery("SELECT * FROM sponge_bob.results r JOIN sponge_bob.members m ON r.id = m.id WHERE discipline = '" + discipline + "' AND m.category = '" + category + "' AND m.status = 'Active' ORDER BY millisecond ASC LIMIT 5"))
        ) {
            while (rs.next()) {
                System.out.printf("ID: %-4d Name: %-30s  Type: %-11s  Best result: %-9s  Date: %-10s  Trainer: %-10s  Qualification: %-2d  Tournament: %-20s\n", rs.getInt("id"), (rs.getString("name") + " " + rs.getString("surname")), rs.getString("type"), calc.millisecondCalcString(rs.getInt("millisecond")), rs.getDate("date"), rs.getString("trainer"), rs.getInt("qualification"), rs.getString("tournament"));
            }

        } catch (Exception e) {
            System.out.println("Query Error: " + e);
        }
    }

}
