import DataMappers.AccountancyMapper;
import DataMappers.StatisticsMapper;
import DataMappers.MembershipMapper;
import Model.Results;
import Util.DB_Connect;
import Model.Member;
import Util.TimeCalc;

import java.util.Scanner;

public class Sponge_Bob_Club {
    boolean sqlCheck = true; //Avoid unnecessary connections

    Member member = new Member();
    Results results = new Results();
    MembershipMapper mDB = new MembershipMapper();
    AccountancyMapper aDB = new AccountancyMapper();
    StatisticsMapper sDB = new StatisticsMapper();


    public static void main(String[] args) {
        System.out.println("\nWelcome to Sponge Bob Swimming Club!\n");

        Sponge_Bob_Club main = new Sponge_Bob_Club();
        main.mainMenuSwitch();
    }

    //MAIN MENU - SWITCH
    public void mainMenuSwitch() {
        System.out.println();

        showMenuDisplay();
        Scanner scan = new Scanner(System.in);
        int input = scan.nextInt();
        switch (input) {
            case 1:
                System.out.println("MEMBERSHIP OVERVIEW");
                membershipSwitch();
                break;
            case 2:
                System.out.println("ACCOUNTANCY OVERVIEW");
                accountancySwitch();
                break;
            case 3:
                System.out.println("TOURNAMENT RESULTS");
                statisticsSwitch();
                break;
            default:
                mainMenuSwitch();
                break;
        }
    }

    //MAIN MENU DISPLAY
    public void showMenuDisplay() {
        initalizeSQLDB();//CHECK IF MYSQL IS STILL CONNECTED
        System.out.print("Press: \n" +
                "1) Membership. \n" +
                "2) Accountancy. \n" +
                "3) Statistics. \n\n" +
                "Enter your option: ");
    }


    //1) MEMBERSHIP SWITCH
    public void membershipSwitch() {
        System.out.println();

        showMembershipDisplay();
        Scanner scan = new Scanner(System.in);
        int input = scan.nextInt();
        switch (input) {
            case 1:
                member.userRegistryInput();
                membershipSwitch();
                break;
            case 2:
                mDB.displayAllSwimmersFromDb();
                membershipSwitch();
                break;
            case 3:
                mDB.displayActiveSwimmersFromDb();
                membershipSwitch();
                break;
            case 4:
                mDB.displayInactiveSwimmersFromDb();
                membershipSwitch();
                break;
            case 5:
                memberSearchSwitch();
                membershipSwitch();
                break;
            case 6:
                mDB.deleteMemberFromDb();
                membershipSwitch();
                break;
            case 0:
                mainMenuSwitch();
                break;

            default:
                membershipSwitch();
        }
    }

    //1) MEMBERSHIP DISPLAY
    public void showMembershipDisplay() {
        initalizeSQLDB();//CHECK IF MYSQL IS STILL CONNECTED
        System.out.print("Press: \n" +
                "1) Register a new member. \n" +
                "2) View all members. \n" +
                "3) View active members. \n" +
                "4) View inactive members. \n" +
                "5) Find member. \n" +
                "6) Delete member. \n" +
                "0) Return to Main Menu. \n\n" +
                "Enter your option: ");
    }

    //2) ACCOUNTANCY SWITCH
    public void accountancySwitch() {
        System.out.println();

        showAccountancyDisplay();
        Scanner scan = new Scanner(System.in);
        int input = scan.nextInt();
        switch (input) {
            case 1:
                memberSearchSwitch();
                accountancySwitch();
                break;
            case 2:
                aDB.changeMemberStatusToInactive();
                accountancySwitch();
                break;
            case 3:
                aDB.changeMemberStatusToActive();
                accountancySwitch();
                break;
            case 4:
                aDB.subscriptionRenewal();
                accountancySwitch();
                break;
            case 5:
                aDB.displayActiveMembersOverdueFromDb();
                accountancySwitch();
                break;
            case 6:
                aDB.displayInactiveMembersOverdueFromDb();
                accountancySwitch();
                break;
            case 0:
                mainMenuSwitch();
                break;
            default:
                accountancySwitch();
        }
    }

    //2) ACCOUNTANCY DISPLAY
    public void showAccountancyDisplay() {
        initalizeSQLDB();//CHECK IF MYSQL IS STILL CONNECTED
        System.out.print("Press: \n" +
                "1) Find member. \n" +
                "2) Change membership status to 'Inactive'. \n" +
                "3) Change membership status to 'Active'. \n" +
                "4) Register payment of yearly subscription. \n" +
                "5) View active members overdue. \n" +
                "6) View inactive members overdue. \n" +
                "0) Return to Main Menu. \n\n" +
                "Enter your option: ");
    }

    //3) STATISTICS SWITCH
    public void statisticsSwitch() {
        System.out.println();

        showStatisticsDisplay();
        Scanner scan = new Scanner(System.in);
        int input = scan.nextInt();
        switch (input) {
            case 1:
                memberSearchSwitch();
                statisticsSwitch();
                break;
            case 2:
                results.userDailyTrainingInput();
                statisticsSwitch();
                break;
            case 3:
                results.userChampionshipTrainingInput();
                statisticsSwitch();
                break;
            case 4:
                sDB.top5ByCategoryDisciplineFromDb();
                statisticsSwitch();
                break;
              case 0:
                mainMenuSwitch();
                break;
            default:
                statisticsSwitch();
        }
    }

    //3) STATISTICS DISPLAY
    public void showStatisticsDisplay() {
        initalizeSQLDB();//CHECK IF MYSQL IS STILL CONNECTED
        System.out.print("Press: \n" +
                "1) Find member. \n" +
                "2) Register daily trainning results. \n" +
                "3) Register competition results. \n" +
                "4) Top 5 Results. \n" +
                "0) Return to Main Menu. \n\n" +
                "Enter your option: ");
    }

    //SUB_MENU: SEARCH SWITCH
    public void memberSearchSwitch() {
        System.out.println();

        showMemberSearchDisplay();
        Scanner scan = new Scanner(System.in);
        int input = scan.nextInt();
        switch (input) {
            case 1:
                mDB.findMemberByIdFromDb();
                memberSearchSwitch();
                break;
            case 2:
                mDB.findMemberByNameFromDb();
                memberSearchSwitch();
                break;
            case 3:
                mDB.findMemberByPhoneFromDb();
                memberSearchSwitch();
                break;
            case 4:
                mDB.findMemberByBirthdateFromDb();
                memberSearchSwitch();
                break;
            case 0:
                mainMenuSwitch();
                break;

            default:
                memberSearchSwitch();
        }
    }

    //SUB_MENU: SEARCH DISPLAY
    public void showMemberSearchDisplay() {
        initalizeSQLDB();//CHECK IF MYSQL IS STILL CONNECTED
        System.out.print("Press: \n" +
                "1) Find member by ID. \n" +
                "2) Find member by name. \n" +
                "3) Find member by phone. \n" +
                "4) Find member by birthdate. \n" +
                "0) Return to Main Menu. \n\n" +
                "Enter your option: ");
    }

    private void initalizeSQLDB() {
        if (sqlCheck == true) {//IF TRUE, IT WON'T OPEN A NEW CONNECTION
            try {
                sqlCheck = false;
                DB_Connect.getInstance(); // Checking Connection
                System.out.println("SQL connected. \n");
            } catch (Exception e) {
                System.out.println("MYSQL ERROR! :" + e);
                sqlCheck = true;
            }
        }
    }

}
