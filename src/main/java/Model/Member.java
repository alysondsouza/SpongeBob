package Model;

import DataMappers.MembershipMapper;

import java.time.LocalDate;
import java.time.Period;
import java.util.Scanner;

public class Member {

    int id;
    String name;
    String surname;
    int phone;
    String birthdateStr;
    LocalDate birthdate;
    String category = null;
    String modality = null;
    String discipline = null;
    String status = "Active";
    double payment;
    LocalDate paid_date = LocalDate.now();
    double on_hold_payment;
    LocalDate on_hold_date = null;
    int age;
    int mod;
    int dis;
    int confirmation;

    MembershipMapper sDB = new MembershipMapper();

    //CONSTRUCTOR
    public Member() {
    }

    public Member(int id, String name, String surname, int phone, LocalDate birthdate, String category, String modality, String discipline, String status, double payment, LocalDate paid_date, double on_holdpayment, LocalDate on_hold_date) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.birthdate = birthdate;
        this.category = category;
        this.modality = modality;
        this.discipline = discipline;
        this.status = status;
        this.payment = payment;
        this.paid_date = paid_date;
        this.on_hold_payment = on_holdpayment;
        this.on_hold_date = on_hold_date;
    }

    //METHOD: GET INPUT FROM USER FOR REGISTRY OF CUSTOMER
    public void userRegistryInput() {
        Scanner scan = new Scanner(System.in);
        System.out.println("\nPlease enter the following data as requested:\n" +
                "(Name, Surname, Birthdate, Phone, Modality - and if competitor: Discipline) \n");

        //NAME
        System.out.println("Name: ");
        name = scan.nextLine();
        System.out.println("Surname: ");
        surname = scan.nextLine();

        //PHONE
        System.out.println("Phone: ");
        phone = scan.nextInt();
        scan.nextLine();//bug

        //BIRTHDATE + AGE
        System.out.println("Birthdate: (YYYY-MM-DD)");
        birthdateStr = scan.nextLine();
        birthdate = LocalDate.parse(birthdateStr);
        age = Period.between(birthdate, LocalDate.now()).getYears();

        //CATEGORY
        if (age < 18) {
            category = "Junior";
        } else {
            category = "Senior";
        }

        //MODALITY + DISCIPLINE
        System.out.println("Choose the modality: \n" +
                "Enter 1: for Leisure. \n" +
                "Enter 2: for Competition.");
        mod = scan.nextInt();
        switch (mod) {
            case 1:
                modality = "Leisure";
                break;
            case 2:
                modality = "Competitor";
                break;
        }
        if (mod == 2) {
            System.out.println("Enter the Discipline: \n" +
                    "Enter 1: for Butterfly. \n" +
                    "Enter 2: for Crawl. \n" +
                    "Enter 3: for Backstroke. \n" +
                    "Enter 4: for Chest. ");
            dis = scan.nextInt();
        }
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
        }

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

        //ID
        id = sDB.getNextIdFromDb();

        //PRINT CONFIRMATION
        System.out.println("\n" +
                "ID: " + id +
                ", Name: " + name + " " + surname +
                ", Phone: " + phone +
                ", Birthdate: " + birthdate +
                ", Age: " + age +
                ", Category: " + category +
                ", Modality: " + modality +
                ", Discipline: " + discipline +
                ", Status: " + status +
                ", Payment: " + payment +
                ", Payment date: " + paid_date + "\n"
        );

        //TRANSACTION WAITS FOR CONFIRMATION
        System.out.print("\nIf this information is correct, Press 1 to confirm or Press 0 to return to menu: ");
        confirmation = scan.nextInt();
        switch (confirmation) {
            case 1:
                sDB.registerMemberIntoDb(id, name, surname, phone, birthdate, category, modality, discipline, status, payment, paid_date, on_hold_payment, on_hold_date);
                break;
            default :
                System.out.println("Entry NOT confirmed.\n");
                break;
        }
    }


    @Override
    public String toString() {
        return "Swimmer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", phone=" + phone +
                ", birthdate=" + birthdateStr +
                ", age=" + age +
                ", category='" + category + '\'' +
                ", modality='" + modality + '\'' +
                ", discipline='" + discipline + '\'' +
                ", status=" + status +
                ", payment=" + payment +
                ", paid_date=" + paid_date +
                '}';
    }
}
