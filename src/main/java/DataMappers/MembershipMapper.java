package DataMappers;

import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

public class MembershipMapper {
    static int db_id;

    private static final String USERNAME = "guest";
    private static final String PASSWORD = "guest";
    private static final String CONN_STR = "jdbc:mysql://ali.cwic1cb4gyam.eu-central-1.rds.amazonaws.com";


    //1) REGISTER NEW MEMBER
    public void registerMemberIntoDb(int id, String name, String surname, int phone, LocalDate birthdate, String category, String modality, String discipline, String status, double payment, LocalDate paid_date, double on_hold_payment, LocalDate on_hold_date) {
        try (
                Connection conn = DriverManager.getConnection(CONN_STR, USERNAME, PASSWORD);
                PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO sponge_bob.members (id, name, surname, phone, birthdate, category, modality, discipline, status, payment, paid_date, on_hold_payment, on_hold_date) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")
        ) {

            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, surname);
            preparedStatement.setInt(4, phone);
            preparedStatement.setDate(5, Date.valueOf(paid_date));
            preparedStatement.setString(6, category);
            preparedStatement.setString(7, modality);
            preparedStatement.setString(8, discipline);
            preparedStatement.setString(9, status);
            preparedStatement.setDouble(10, payment);
            preparedStatement.setDate(11, Date.valueOf(paid_date));
            preparedStatement.setNull(12, Types.DOUBLE); //setNull
            preparedStatement.setNull(13, Types.DATE); //setNull

            preparedStatement.execute();
            conn.close();
            System.out.println("\nMEMBER ADDED:\n" +
                    "ID: " + id +
                    ", Name: " + name + " " + surname +
                    ", Phone: " + phone +
                    ", Birthdate: " + birthdate +
                    ", Category: " + category +
                    ", Modality: " + modality +
                    ", Discipline: " + discipline +
                    ", Status: " + status +
                    ", Payment: " + payment +
                    ", Payment date: " + paid_date
            );

        } catch (Exception e) {
            System.out.println("Querry Error: " + e);
        }
    }

    //1a) READ FROM DB - GET NEW MEMBER ID
    public int getNextIdFromDb() {
        try ( //'Connection', 'Statement' and 'ResultSet' are AUTO-CLOSABLE when with TRY-WITH-RESOURCES BLOCK (...)
              Connection conn = DriverManager.getConnection(CONN_STR, USERNAME, PASSWORD);
              Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
              ResultSet rs = (stmt.executeQuery("SELECT * FROM sponge_bob.members"))
        ) {
            rs.last();
            rs.getInt("id");
            db_id = rs.getInt("id") + 1;

        } catch (SQLException e) {
            System.out.println("Query Error: " + e);
        }
        return db_id;
    }

    //2) VIEW ALL MEMBERS
    public void displayAllSwimmersFromDb() {
        try ( //'Connection', 'Statement' and 'ResultSet' are AUTO-CLOSABLE when with TRY-WITH-RESOURCES BLOCK (...)
              Connection conn = DriverManager.getConnection(CONN_STR, USERNAME, PASSWORD);
              Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
              ResultSet rs = (stmt.executeQuery("SELECT * FROM sponge_bob.members"))
        ) {
            System.out.println();
            while (rs.next()) {

//                ALTERNATIVE WAY TO PRINT MEMBERS FROM DB:
//                StringBuffer buffer = new StringBuffer();
//                buffer.append("Swimmer ID: " + rs.getInt("id") +
//                        "- Name: " + rs.getString("name") + " " + rs.getString("surname") +
//                        ", Phone: " + rs.getInt("phone") +
//                        ", Birthdate: " + rs.getDate("birthdate") +
//                        ", Category: " + rs.getString("category") +
//                        ", Modality: " + rs.getString("modality") +
//                        ", Discipline: " + rs.getString("discipline") +
//                        ", Status: " + rs.getString("status") +
//                        ", Payment: " + rs.getDouble("payment") +
//                        ", Paid: " + rs.getDate("paid_date") +
//                        ", On-hold payment: " + rs.getDouble("on_hold_payment") +
//                        ", On-hold start date: " + rs.getDate("on_hold_date"));
//                System.out.println(buffer);

                System.out.printf("ID: %-4d Name: %-30s  Phone: %-10d Birthdate: %-10s  Category: %-6s  Modality: %-10s  Discipline: %-10s  Status: %-8s  Payment: %6.2f  Paid: %-10s  On-hold payment: %6.2f  On-hold paid: %-10s\n", rs.getInt("id"), (rs.getString("name") + " " + rs.getString("surname")), rs.getInt("phone"), rs.getDate("birthdate"), rs.getString("category"), rs.getString("modality"), rs.getString("discipline"), rs.getString("status"), rs.getDouble("payment"), rs.getDate("paid_date"), rs.getDouble("on_hold_payment"), rs.getDate("on_hold_date"));
            }

            rs.last();
            System.out.println("\nAll registered members: " + rs.getRow());

        } catch (SQLException e) {
            System.out.println("Query Error: " + e);
        }
    }

    //3) VIEW ALL ACTIVE MEMBERS
    public void displayActiveSwimmersFromDb() {
        try ( //'Connection', 'Statement' and 'ResultSet' are AUTO-CLOSABLE when with TRY-WITH-RESOURCES BLOCK (...)
              Connection conn = DriverManager.getConnection(CONN_STR, USERNAME, PASSWORD);
              Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
              ResultSet rs = (stmt.executeQuery("SELECT * FROM sponge_bob.members WHERE status = 'Active'"))
        ) {
            System.out.println();
            while (rs.next()) {
                System.out.printf("ID: %-4d Name: %-30s  Phone: %-10d Birthdate: %-10s  Category: %-6s  Modality: %-10s  Discipline: %-10s  Status: %-8s  Payment: %6.2f  Paid: %-10s  On-hold payment: %6.2f  On-hold paid: %-10s\n", rs.getInt("id"), (rs.getString("name") + " " + rs.getString("surname")), rs.getInt("phone"), rs.getDate("birthdate"), rs.getString("category"), rs.getString("modality"), rs.getString("discipline"), rs.getString("status"), rs.getDouble("payment"), rs.getDate("paid_date"), rs.getDouble("on_hold_payment"), rs.getDate("on_hold_date"));
            }

            rs.last();
            System.out.println("\nActive members: " + rs.getRow());

        } catch (SQLException e) {
            System.out.println("Query Error: " + e);
        }
    }

    //4) VIEW ALL INACTIVE MEMBERS
    public void displayInactiveSwimmersFromDb() {
        try ( //'Connection', 'Statement' and 'ResultSet' are AUTO-CLOSABLE when with TRY-WITH-RESOURCES BLOCK (...)
              Connection conn = DriverManager.getConnection(CONN_STR, USERNAME, PASSWORD);
              Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
              ResultSet rs = (stmt.executeQuery("SELECT * FROM sponge_bob.members WHERE status = 'Inactive'"))
        ) {
            System.out.println();
            while (rs.next()) {
                System.out.printf("ID: %-4d Name: %-30s  Phone: %-10d Birthdate: %-10s  Category: %-6s  Modality: %-10s  Discipline: %-10s  Status: %-8s  Payment: %6.2f  Paid: %-10s  On-hold payment: %6.2f  On-hold paid: %-10s\n", rs.getInt("id"), (rs.getString("name") + " " + rs.getString("surname")), rs.getInt("phone"), rs.getDate("birthdate"), rs.getString("category"), rs.getString("modality"), rs.getString("discipline"), rs.getString("status"), rs.getDouble("payment"), rs.getDate("paid_date"), rs.getDouble("on_hold_payment"), rs.getDate("on_hold_date"));
            }

            rs.last();
            System.out.println("\nInactive members: " + rs.getRow());

        } catch (SQLException e) {
            System.out.println("Query Error: " + e);
        }
    }

    //5a) FIND MEMBER BY ID
    public void findMemberByIdFromDb() {
        System.out.println("Enter the 'ID' of the member you wish to see the details: ");
        Scanner scan = new Scanner(System.in);
        int idx = scan.nextInt();

        try (
                Connection conn = DriverManager.getConnection(CONN_STR, USERNAME, PASSWORD);
                Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = (stmt.executeQuery("SELECT * FROM sponge_bob.members WHERE id = " + idx))
        ) {
            System.out.println();
            while (rs.next()) {
                System.out.printf("ID: %-4d Name: %-30s  Phone: %-10d Birthdate: %-10s  Category: %-6s  Modality: %-10s  Discipline: %-10s  Status: %-8s  Payment: %6.2f  Paid: %-10s  On-hold payment: %6.2f  On-hold paid: %-10s\n", rs.getInt("id"), (rs.getString("name") + " " + rs.getString("surname")), rs.getInt("phone"), rs.getDate("birthdate"), rs.getString("category"), rs.getString("modality"), rs.getString("discipline"), rs.getString("status"), rs.getDouble("payment"), rs.getDate("paid_date"), rs.getDouble("on_hold_payment"), rs.getDate("on_hold_date"));
            }

        } catch (Exception e) {
            System.out.println("Query Error: " + e);
        }
    }

    //5b) FIND MEMBER BY NAME
    public void findMemberByNameFromDb() {
        System.out.println("Enter the 'name' or 'surname' of the member you wish to see the details: ");
        Scanner scan = new Scanner(System.in);
        String namex = scan.nextLine();

        try (
                Connection conn = DriverManager.getConnection(CONN_STR, USERNAME, PASSWORD);
                Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = (stmt.executeQuery("SELECT * FROM sponge_bob.members WHERE name LIKE '%" + namex + "%' OR surname LIKE '%" + namex + "%'"))
        ) {
            System.out.println();
            while (rs.next()) {
                System.out.printf("ID: %-4d Name: %-30s  Phone: %-10d Birthdate: %-10s  Category: %-6s  Modality: %-10s  Discipline: %-10s  Status: %-8s  Payment: %6.2f  Paid: %-10s  On-hold payment: %6.2f  On-hold paid: %-10s\n", rs.getInt("id"), (rs.getString("name") + " " + rs.getString("surname")), rs.getInt("phone"), rs.getDate("birthdate"), rs.getString("category"), rs.getString("modality"), rs.getString("discipline"), rs.getString("status"), rs.getDouble("payment"), rs.getDate("paid_date"), rs.getDouble("on_hold_payment"), rs.getDate("on_hold_date"));
            }

        } catch (Exception e) {
            System.out.println("Query Error: " + e);
        }
    }

    //5c) FIND MEMBER BY PHONE
    public void findMemberByPhoneFromDb() {
        System.out.println("Enter the 'phone' of the member you wish to see the details: ");
        Scanner scan = new Scanner(System.in);
        String phonex = scan.nextLine();

        try (
                Connection conn = DriverManager.getConnection(CONN_STR, USERNAME, PASSWORD);
                Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = (stmt.executeQuery("SELECT * FROM sponge_bob.members WHERE phone LIKE '%" + phonex + "%'"))
        ) {
            System.out.println();
            while (rs.next()) {
                System.out.printf("ID: %-4d Name: %-30s  Phone: %-10d Birthdate: %-10s  Category: %-6s  Modality: %-10s  Discipline: %-10s  Status: %-8s  Payment: %6.2f  Paid: %-10s  On-hold payment: %6.2f  On-hold paid: %-10s\n", rs.getInt("id"), (rs.getString("name") + " " + rs.getString("surname")), rs.getInt("phone"), rs.getDate("birthdate"), rs.getString("category"), rs.getString("modality"), rs.getString("discipline"), rs.getString("status"), rs.getDouble("payment"), rs.getDate("paid_date"), rs.getDouble("on_hold_payment"), rs.getDate("on_hold_date"));
            }

        } catch (Exception e) {
            System.out.println("Query Error: " + e);
        }
    }

    //5d) FIND MEMBER BY BIRTHDATE
    public void findMemberByBirthdateFromDb() {
        System.out.println("Enter the 'date of birth' of the member you wish to see the details: (YYYY) or (YYYY-MM-DD)");
        Scanner scan = new Scanner(System.in);
        String birthdatex = scan.nextLine();

        try (
                Connection conn = DriverManager.getConnection(CONN_STR, USERNAME, PASSWORD);
                Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = (stmt.executeQuery("SELECT * FROM sponge_bob.members WHERE birthdate LIKE '%" + birthdatex + "%'"))
        ) {
            System.out.println();
            while (rs.next()) {
                System.out.printf("ID: %-4d Name: %-30s  Phone: %-10d Birthdate: %-10s  Category: %-6s  Modality: %-10s  Discipline: %-10s  Status: %-8s  Payment: %6.2f  Paid: %-10s  On-hold payment: %6.2f  On-hold paid: %-10s\n", rs.getInt("id"), (rs.getString("name") + " " + rs.getString("surname")), rs.getInt("phone"), rs.getDate("birthdate"), rs.getString("category"), rs.getString("modality"), rs.getString("discipline"), rs.getString("status"), rs.getDouble("payment"), rs.getDate("paid_date"), rs.getDouble("on_hold_payment"), rs.getDate("on_hold_date"));
            }

        } catch (Exception e) {
            System.out.println("Query Error: " + e);
        }
    }

    //6) DELETE MEMBER BY ID
    public void deleteMemberFromDb() {
        String status = "";

        System.out.println("Enter the Member ID you want to delete:");
        Scanner scan = new Scanner(System.in);
        int idx = scan.nextInt();

        try (
                Connection conn = DriverManager.getConnection(CONN_STR, USERNAME, PASSWORD);
                Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = (stmt.executeQuery("SELECT * FROM sponge_bob.members WHERE id = " + idx))
        ) {
            while (rs.next()) {
                System.out.printf("\nID: %-4d Name: %-30s  Phone: %-10d Birthdate: %-10s  Category: %-6s  Modality: %-10s  Discipline: %-10s  Status: %-8s  Payment: %6.2f  Paid: %-10s  On-hold payment: %6.2f  On-hold paid: %-10s\n", rs.getInt("id"), (rs.getString("name") + " " + rs.getString("surname")), rs.getInt("phone"), rs.getDate("birthdate"), rs.getString("category"), rs.getString("modality"), rs.getString("discipline"), rs.getString("status"), rs.getDouble("payment"), rs.getDate("paid_date"), rs.getDouble("on_hold_payment"), rs.getDate("on_hold_date"));
            }

            rs.first();
            status = rs.getString("status");

        } catch (Exception e) {
            System.out.println("Query Error: " + e);
        }

        if (status.equals("Deleted")) {
            System.out.println("\nOperation is not valid.\nMembership has already been archived.");
        } else {

            //TRANSACTION WAITS FOR CONFIRMATION
            System.out.print("\nAre you sure you want to delete Membership #" + idx + " ? \nPress 1 to confirm or Press 0 to return to menu: ");
            int confirmation = scan.nextInt();
            switch (confirmation) {
                case 1:
                    try (
                            Connection conn = DriverManager.getConnection(CONN_STR, USERNAME, PASSWORD);
                            PreparedStatement preparedStatement = conn.prepareStatement("UPDATE sponge_bob.members SET status = 'Deleted', payment = null, paid_date = null, on_hold_payment = null, on_hold_date = null WHERE id = ?")
                            //PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM sponge_bob.members WHERE id = ?")
                    ) {
                        preparedStatement.setInt(1, idx); // 1.parameterindex= '?' og x= 'value'  --> id(?)=idx
                        preparedStatement.executeUpdate();
                        System.out.println("MEMBER DELETED");

                    } catch (Exception e) {
                        System.out.println("Query Error: " + e);
                    }
                    break;
                default:
                    System.out.println("Membership NOT deleted. \n");
                    break;
            }
        }
    }

}
