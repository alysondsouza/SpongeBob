package DataMappers;

import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.YEARS;

public class AccountancyMapper {

    private static final String USERNAME = "guest";
    private static final String PASSWORD = "guest";
    private static final String CONN_STR = "jdbc:mysql://ali.cwic1cb4gyam.eu-central-1.rds.amazonaws.com";


    //2) CHANGE STATUS TO INACTIVE
    public void changeMemberStatusToInactive() {
        LocalDate today = LocalDate.now();

        System.out.println("Enter the Member ID you want to change status to 'Inactive':");
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

            //IF - Check if status is already 'Inactive'
            rs.first();
            String statusInactive = rs.getString("status");
            if (statusInactive.equals("Inactive")) {
                System.out.println("\nThis member is already inactive.");
            } else if (statusInactive.equals("Deleted")) {
                System.out.println("\nThis member has been deleted.");
            } else {

                //TRANSFORM SQL DATE TO JAVA LOCAL_DATE
                rs.first();
                java.sql.Date sqlDate = java.sql.Date.valueOf(String.valueOf(rs.getDate("paid_date")));
                LocalDate paid_date = sqlDate.toLocalDate();
                System.out.println("\nLast payment: " + paid_date);

                //CALCULATE DIFFERENCE BETWEEN LOCAL DATES
                long diff = DAYS.between(paid_date, today);
                int daysBetween = (int) diff;

                //IF - Check if subscription is overdue
                if (daysBetween > 365) {
                    System.out.println("Your subscription is overdue. You need to renew your subscription first.");
                } else {
                    //TRANSACTION WAITS FOR CONFIRMATION
                    System.out.print("\nAre you sure you want to change status for Membership #" + idx + " ? \nPress 1 to confirm or Press 0 to return to menu: ");
                    int confirmation = scan.nextInt();


                    if (confirmation == 1) {
                        try (
                                PreparedStatement preparedStatement = conn.prepareStatement("UPDATE sponge_bob.members SET status = 'Inactive', on_hold_payment = '500.0', on_hold_date = '" + today + "' WHERE id = ?") // 'Inactive'
                        ) {
                            preparedStatement.setInt(1, idx); //parameterindex= '?' og x= værdier  --> id=1
                            preparedStatement.executeUpdate();

                        } catch (Exception e) {
                            System.out.println("Query Error: " + e);
                        }

                        System.out.println("\nSTATUS CHANGED: \n");
                        System.out.printf("\nID: %-4d Name: %-30s  Phone: %-10d Birthdate: %-10s  Category: %-6s  Modality: %-10s  Discipline: %-10s  Status: Inactive  Payment: %6.2f  Paid: %-10s  On-hold payment: 500.00  On-hold paid: %-10s\n", rs.getInt("id"), (rs.getString("name") + " " + rs.getString("surname")), rs.getInt("phone"), rs.getDate("birthdate"), rs.getString("category"), rs.getString("modality"), rs.getString("discipline"), rs.getDouble("payment"), rs.getDate("paid_date"), today);

                    } else {
                        System.out.println("\nStatus NOT updated.");
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Query Error: " + e);
        }


    }

    //3) CHANGE STATUS TO ACTIVE
    public void changeMemberStatusToActive() {
        LocalDate today = LocalDate.now();

        System.out.println("Enter the Member ID you want to change status to 'Active':");
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

            //IF - Check if status is already 'Active'
            rs.first();
            String statusActive = rs.getString("status");
            if (statusActive.equals("Active")) {
                System.out.println("\nThis member is already Active.\n");
            } else if (statusActive.equals("Deleted")) {
                System.out.println("\nThis member has been deleted.");
            } else {
                //TRANSFORM SQL DATE TO JAVA LOCAL_DATE
                rs.first();
                java.sql.Date sqlDate = java.sql.Date.valueOf(String.valueOf(rs.getDate("on_hold_date")));
                LocalDate on_hold_date = sqlDate.toLocalDate();
                System.out.println("Membership on hold since: " + on_hold_date);

                //CALCULATE DIFFERENCE BETWEEN LOCAL_DATES
                long diff = DAYS.between(on_hold_date, today);
                int daysBetween = (int) diff;

                //IF - Check if subscription is overdue
                if (daysBetween > 365) {
                    System.out.println("\nYour subscription have been on hold for more than a year. You need to register a new subscription.");
                } else {
                    //TRANSACTION WAITS FOR CONFIRMATION
                    System.out.print("\nAre you sure you want to change status for Membership #" + idx + " ? \nPress 1 to confirm or Press 0 to return to menu: ");
                    int confirmation = scan.nextInt();


                    if (confirmation == 1) {
                        try (
                                PreparedStatement preparedStatement = conn.prepareStatement("UPDATE sponge_bob.members SET status = 'Active' WHERE id = ?") // 'Active'
                        ) {
                            preparedStatement.setInt(1, idx); //parameterindex= '?' og x= værdier  --> id=1
                            preparedStatement.executeUpdate();

                        } catch (Exception e) {
                            System.out.println("Query Error: " + e);
                        }

                        System.out.println("\nSTATUS CHANGED: \n");
                        System.out.printf("\nID: %-4d Name: %-30s  Phone: %-10d Birthdate: %-10s  Category: %-6s  Modality: %-10s  Discipline: %-10s  Status: Active  Payment: %6.2f  Paid: %-10s  On-hold payment: 500.00  On-hold paid: %-10s\n", rs.getInt("id"), (rs.getString("name") + " " + rs.getString("surname")), rs.getInt("phone"), rs.getDate("birthdate"), rs.getString("category"), rs.getString("modality"), rs.getString("discipline"), rs.getDouble("payment"), rs.getDate("paid_date"), today);

                    } else {
                        System.out.println("\nStatus NOT updated.");
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Query Error: " + e);
        }

    }

    //4) REGISTER PAYMENT
    public void subscriptionRenewal() {
        LocalDate today = LocalDate.now();
        long diff_paid;
        int daysBetween_paid;
        long diff_onHold;
        int daysBetween_onHold = 0;
        long a;
        int age;
        String category;
        double payment = 0;
        String paymentStr;

        System.out.println("\nEnter the Member ID you want to renew subscription:");
        Scanner scan = new Scanner(System.in);
        int idx = scan.nextInt();
        scan.nextLine();

        try (
                Connection conn = DriverManager.getConnection(CONN_STR, USERNAME, PASSWORD);
                Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = (stmt.executeQuery("SELECT * FROM sponge_bob.members WHERE id = " + idx))
        ) {

            rs.first();
            //IF - Check Deleted membership
            if (!rs.getString("status").equals("Deleted")) {

                while (rs.next()) {
                    System.out.printf("\nID: %-4d Name: %-30s  Phone: %-10d Birthdate: %-10s  Category: %-6s  Modality: %-10s  Discipline: %-10s  Status: %-8s  Payment: %6.2f  Paid: %-10s  On-hold payment: %6.2f  On-hold paid: %-10s\n", rs.getInt("id"), (rs.getString("name") + " " + rs.getString("surname")), rs.getInt("phone"), rs.getDate("birthdate"), rs.getString("category"), rs.getString("modality"), rs.getString("discipline"), rs.getString("status"), rs.getDouble("payment"), rs.getDate("paid_date"), rs.getDouble("on_hold_payment"), rs.getDate("on_hold_date"));
                }

                System.out.println();
                rs.first();

                //NAME + SURNAME
                String name = rs.getString("name");
                String surname = rs.getString("surname");

                //TRANSFORM SQL DATE TO JAVA LOCAL_DATE - BIRTHDATE
                java.sql.Date sqlDate_birthdate = java.sql.Date.valueOf(String.valueOf(rs.getDate("birthdate")));
                LocalDate birthdate = sqlDate_birthdate.toLocalDate();
                //CALCULATE DIFFERENCE BETWEEN LOCAL_DATES - PAID_DATE
                a = YEARS.between(birthdate, today);
                age = (int) a;

                //CATEGORY
                if (age < 18) {
                    category = "Junior";
                } else {
                    category = "Senior";
                }

                //STATUS
                String status = rs.getString("status");
                System.out.println("Membership: " + status);

                //SUBSCRIPTION
                if (age < 18) {
                    payment = 1000.00;
                } else if (age >= 18 && age < 60) {
                    payment = 1600.00;
                } else if (age >= 60) {
                    payment = 1600.00 * 0.75; //25% discount
                } else {
                    System.out.println("invalid entry");
                }

                //TRANSFORM SQL DATE TO JAVA LOCAL_DATE - PAID_DATE
                java.sql.Date sqlDate_paid = java.sql.Date.valueOf(String.valueOf(rs.getDate("paid_date")));
                LocalDate paid_date = sqlDate_paid.toLocalDate();
                //CALCULATE DIFFERENCE BETWEEN LOCAL_DATES - PAID_DATE
                diff_paid = DAYS.between(paid_date, today);
                daysBetween_paid = (int) diff_paid;
                System.out.println("Last payment: " + paid_date);

                //TRANSFORM SQL DATE TO JAVA LOCAL_DATE - ON_HOLD
                if (rs.getDate("on_hold_date") != null) {
                    java.sql.Date sqlDate_onHold = java.sql.Date.valueOf(String.valueOf(rs.getDate("on_hold_date")));
                    LocalDate on_hold_date = sqlDate_onHold.toLocalDate();
                    //CALCULATE DIFFERENCE BETWEEN LOCAL_DATES - ON_HOLD
                    diff_onHold = DAYS.between(on_hold_date, today);
                    daysBetween_onHold = (int) diff_onHold;
                    System.out.println("Membership on hold from: " + on_hold_date);
                }

                //IF - Check Inactive-Overdue and Active-Overdue
                if (status.equals("Inactive") && daysBetween_onHold > 365) {
                    System.out.println("\nThis member has been inactive for too long. A new membership is required.\n");
                } else if (status.equals("Active") && daysBetween_paid > 365) {
                    System.out.println("\nYour subscription is overdue. A new membership is required.\n");
                } else {

                    //PAID_DATE
                    System.out.println("\nMembership #" + idx + " : " + name + " " + surname + ", Age: " + age + ", Category: " + category + ", Subscription: " + payment);
                    System.out.print("Confirm date for the next payment: (YYYY-MM-DD): ");
                    paymentStr = scan.nextLine();
                    paid_date = LocalDate.parse(paymentStr);

                    //TRANSACTION WAITS FOR CONFIRMATION
                    System.out.println("\nPress 1 to confirm or Press 0 to return to menu: ");
                    int confirmation = scan.nextInt();
                    if (confirmation == 1) {
                        try (
                                PreparedStatement preparedStatement = conn.prepareStatement("UPDATE sponge_bob.members SET status = 'Active', category = ?, payment = ?, paid_date = ?, on_hold_payment = ?, on_hold_date = ? WHERE id = ?")
                        ) {
                            preparedStatement.setString(1, category); //parameterindex= '?' og x= værdier  --> id=1
                            preparedStatement.setDouble(2, payment);
                            preparedStatement.setDate(3, Date.valueOf(paid_date));
                            preparedStatement.setNull(4, Types.DOUBLE); //setNull
                            preparedStatement.setNull(5, Types.DATE); //setNull
                            preparedStatement.setInt(6, idx);
                            preparedStatement.executeUpdate();

                        } catch (Exception e) {
                            System.out.println("Query Error: " + e);
                        }

                        System.out.println("\nMEMBERSHIP RENEWED:");
                        System.out.printf("ID: %-4d Name: %-30s  Phone: %-10d Birthdate: %-10s  Category: %-6s  Modality: %-10s  Discipline: %-10s  Status: %-8s  Payment: %6.2f  Paid: %-10s  On-hold payment: %6.2f  On-hold paid: %-10s\n", rs.getInt("id"), (rs.getString("name") + " " + rs.getString("surname")), rs.getInt("phone"), rs.getDate("birthdate"), rs.getString("category"), rs.getString("modality"), rs.getString("discipline"), rs.getString("status"), rs.getDouble("payment"), rs.getDate("paid_date"), rs.getDouble("on_hold_payment"), rs.getDate("on_hold_date"));

                    } else {
                        System.out.println("\nStatus NOT updated.");
                    }
                }
            } else {
                System.out.println("\nThis member has been deleted.\n");
            }

        } catch (Exception e) {
            System.out.println("Query Error: " + e);
        }

    }

    //5) VIEW ACTIVE MEMBERS IN DEBT
    public void displayActiveMembersOverdueFromDb() {
        int counter = 0;
        try ( //'Connection', 'Statement' and 'ResultSet' are AUTO-CLOSABLE when with TRY-WITH-RESOURCES BLOCK (...)
              Connection conn = DriverManager.getConnection(CONN_STR, USERNAME, PASSWORD);
              Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
              ResultSet rs = (stmt.executeQuery("SELECT *, DATE_FORMAT(NOW(), '%YYYY-MM-DD') - DATE_FORMAT(paid_date, '%YYYY-MM-DD') - (DATE_FORMAT(NOW(), '00-%m-%d') < DATE_FORMAT(paid_date, '00-%m-%d')) AS overdue_by_year FROM sponge_bob.members WHERE status = 'Active'"))
        ) {
            System.out.println();
            while (rs.next()) {
                if (rs.getInt("overdue_by_year") > 0) {
                    System.out.printf("ID: %-4d Name: %-30s  Phone: %-10d Birthdate: %-10s  Category: %-6s  Modality: %-10s  Discipline: %-10s  Status: %-8s  Payment: %6.2f  Paid: %-10s  On-hold payment: %6.2f  On-hold paid: %-10s\n", rs.getInt("id"), (rs.getString("name") + " " + rs.getString("surname")), rs.getInt("phone"), rs.getDate("birthdate"), rs.getString("category"), rs.getString("modality"), rs.getString("discipline"), rs.getString("status"), rs.getDouble("payment"), rs.getDate("paid_date"), rs.getDouble("on_hold_payment"), rs.getDate("on_hold_date"));
                    counter++;
                }
            }
            rs.last();
            System.out.println("\nActive members in debt: " + counter);

        } catch (SQLException e) {
            System.out.println("Query Error: " + e);
        }
    }

    //6) VIEW INACTIVE MEMBERS IN DEBT
    public void displayInactiveMembersOverdueFromDb() {
        int counter = 0;
        try ( //'Connection', 'Statement' and 'ResultSet' are AUTO-CLOSABLE when with TRY-WITH-RESOURCES BLOCK (...)
              Connection conn = DriverManager.getConnection(CONN_STR, USERNAME, PASSWORD);
              Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
              ResultSet rs = (stmt.executeQuery("SELECT *, DATE_FORMAT(NOW(), '%YYYY-MM-DD') - DATE_FORMAT(on_hold_date, '%YYYY-MM-DD') - (DATE_FORMAT(NOW(), '00-%m-%d') < DATE_FORMAT(on_hold_payment, '00-%m-%d')) AS on_hold_overdue_by_year FROM sponge_bob.members WHERE status = 'Inactive'"))
        ) {
            System.out.println();
            while (rs.next()) {
                if (rs.getInt("on_hold_overdue_by_year") > 0) {
                    System.out.printf("ID: %-4d Name: %-30s  Phone: %-10d Birthdate: %-10s  Category: %-6s  Modality: %-10s  Discipline: %-10s  Status: %-8s  Payment: %6.2f  Paid: %-10s  On-hold payment: %6.2f  On-hold paid: %-10s\n", rs.getInt("id"), (rs.getString("name") + " " + rs.getString("surname")), rs.getInt("phone"), rs.getDate("birthdate"), rs.getString("category"), rs.getString("modality"), rs.getString("discipline"), rs.getString("status"), rs.getDouble("payment"), rs.getDate("paid_date"), rs.getDouble("on_hold_payment"), rs.getDate("on_hold_date"));
                    counter++;
                }
            }
            rs.last();
            System.out.println("\nInactive members in debt: " + counter);

        } catch (SQLException e) {
            System.out.println("Query Error: " + e);
        }
    }

}
