package Model;

import DataMappers.MembershipMapper;
import DataMappers.StatisticsMapper;
import Util.TimeCalc;

import java.time.LocalDate;
import java.util.Scanner;

public class Results {

    int r_id;
    int id;
    String type;
    LocalDate date;
    int millisecond;
    String tournament = null;
    int qualification;
    String trainer;
    String dateStr;
    String millisec;
    int confirmation;
    boolean validMember = true;

    MembershipMapper mDB = new MembershipMapper();
    StatisticsMapper sDB = new StatisticsMapper();

    public Results() {
    }

    public Results(int id, String type, LocalDate date, int millisecond, String tournament, int qualification, String trainer) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.millisecond = millisecond;
        this.tournament = tournament;
        this.qualification = qualification;
        this.trainer = trainer;
    }

    //INPUT - DAILY RESULTS
    public void userDailyTrainingInput() {
        System.out.println("Enter the 'ID' of the member you wish to register the results: ");
        Scanner scan = new Scanner(System.in);
        int id = scan.nextInt();

        //CHECK ID RANGE
        if (id < 1 || id > mDB.getNextIdFromDb()) {
            System.out.println("\nInvalid ID.");
        } else {

            //CHECK DELETED OR INACTIVE MEMBERSHIP
            validMember = sDB.findMemberByIdFromDb(id);
            if (validMember) {
                //ID CONFIRMATION
                System.out.println("\nIs it the right Member? \nPress 1 to confirm or Press 0 to return to menu: ");
                confirmation = scan.nextInt();
                scan.nextLine(); //bug

                if (confirmation == 1) {
                    System.out.println("\nPlease enter the following data as requested:\n" +
                            "(For daily training results: date, best result time and trainer.)\n");

                    //DATE
                    System.out.println("Date: (YYYY-MM-DD)");
                    dateStr = scan.nextLine();
                    date = LocalDate.parse(dateStr);

                    //MILLISECOND
                    System.out.println("Best result time: in minutes\" seconds' and milliseconds (mm\"ss'ms)");
                    millisec = scan.nextLine();

                    TimeCalc calc = new TimeCalc();
                    millisecond = calc.millisecondCalcInteger(millisec);

                    //TRAINER
                    System.out.println("Trainer: ");
                    trainer = scan.nextLine();

                    //TRAINING ID
                    r_id = sDB.getNextTrainingIdFromDb();

                    //PRINT
                    System.out.println();
                    System.out.println("Best result: " + millisec + ", Date: " + date + ", Trainer: " + trainer);


                    //ENTRY CONFIRMATION
                    System.out.println("Press 1 to confirm or Press 0 to return to menu: ");
                    confirmation = scan.nextInt();
                    if (confirmation == 1) {
                        sDB.registerDailyResultsIntoDb(r_id, id, "Training", date, millisecond, tournament, qualification, trainer);
                    } else {
                        System.out.println("Membership NOT confirmed.\n");
                    }
                } else {
                    System.out.println("Membership NOT confirmed.\n");
                }
            }
        }
    }


    //INPUT - CHAMPIONSHIP
    public void userChampionshipTrainingInput() {
        System.out.println("Enter the 'ID' of the member you wish to register the results: ");
        Scanner scan = new Scanner(System.in);
        int id = scan.nextInt();

        //CHECK ID RANGE
        if (id < 1 || id > mDB.getNextIdFromDb()) {
            System.out.println("\nInvalid ID.");
        } else {

            //CHECK DELETED OR INACTIVE MEMBERSHIP
            validMember = sDB.findMemberByIdFromDb(id);
            if (validMember) {

                //ID CONFIRMATION
                System.out.println("\nIs it the right Member? \nPress 1 to confirm or Press 0 to return to menu: ");
                confirmation = scan.nextInt();
                scan.nextLine(); //bug

                if (confirmation == 1) {
                    System.out.println("\nPlease enter the following data as requested:\n" +
                            "(For competition results: date, championship result time, championship name, podium and trainer.)\n");

                    //DATE
                    System.out.println("Date: (YYYY-MM-DD)");
                    dateStr = scan.nextLine();
                    date = LocalDate.parse(dateStr);

                    //MILLISECOND
                    System.out.println("Best result time: in minutes\" seconds' and milliseconds (mm\"ss'ms)");
                    millisec = scan.nextLine();

                    TimeCalc calc = new TimeCalc();
                    millisecond = calc.millisecondCalcInteger(millisec);

                    //TOURNAMENT
                    System.out.println("Championship name: ");
                    tournament = scan.nextLine();

                    //QUALIFICATION
                    System.out.println("Position at the podium: ");
                    qualification = scan.nextInt();
                    scan.nextLine();

                    //TRAINER
                    System.out.println("Trainer: ");
                    trainer = scan.nextLine();

                    //TRAINING ID
                    r_id = sDB.getNextTrainingIdFromDb();

                    //PRINT
                    System.out.println();
                    System.out.println("Best result: " + millisec + ", Date: " + date + ", Championship: " + tournament + ", Podium: " + qualification + ", Trainer: " + trainer);

                    //ENTRY CONFIRMATION
                    System.out.println("Press 1 to confirm or Press 0 to return to menu: ");
                    confirmation = scan.nextInt();
                    if (confirmation == 1) {
                        sDB.registerDailyResultsIntoDb(r_id, id, "Training", date, millisecond, tournament, qualification, trainer);
                    } else {
                        System.out.println("Membership NOT confirmed.\n");
                    }
                } else {
                    System.out.println("Membership NOT confirmed.\n");
                }
            }
        }
    }

}
