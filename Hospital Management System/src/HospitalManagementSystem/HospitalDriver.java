package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalDriver
{
    //adding DB credentials & DB URL as private static final
    //private - for security purposes & no other class can use outside this class
    //static - to use them there is no need to create an object in main method of this class
    //final - constant value throughout the program

    private static final String url = "jdbc:mysql://localhost:3306/hospital_prod"; //via MySQL Workbench --> copy JDBC Connection string & instead of "?user=root" write the name of your DB
    private static final String username  = "root"; //you can check by command : SELECT USER(); in MySQL Command line
    private static final String password = "Dev@3012";




    //method 1 : checkdoctoravailability()

    public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection)
    {
        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";
        try {
            //pass the query
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            //set the values for query
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2, appointmentDate);

            //execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            //checking the output
            if(resultSet.next())
            {
                int count = resultSet.getInt(1);

                if(count==0)
                {
                    return true; //as this method is of boolean type
                }
                else
                {
                    return false;
                }
            }
        }catch(SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }



    //method 2 : bookAppointment()

    //passing the "objects" of DB connection and scanner class & patient and doctor class as parameters in method
    public static void bookAppointment(Patient patient, Doctor doctor, Connection connection, Scanner sc)
    {
        System.out.print("Enter Patient id : ");
        int patientId = sc.nextInt();

        System.out.print("Enter Doctor id : ");
        int doctorId = sc.nextInt();

        System.out.print("Enter Appointment Date | Req. Format -> YYYY-MM-DD : ");
        String appointmentDate = sc.next();

        //condition to check if doctor and patient already exist to book the appointment
        if(patient.getPatientById(patientId) && doctor.getDoctorById(doctorId))
        {
            if(checkDoctorAvailability(doctorId, appointmentDate, connection))
            {
                String appointmentQuery = "INSERT INTO appointments(patient_id, doctor_id, appointment_date) VALUES(?, ?, ?)";
                try
                {
                    //pass the query
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);

                    //set the values
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2, doctorId);
                    preparedStatement.setString(3, appointmentDate);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if(rowsAffected>0)
                    {
                        System.out.println("Appointment Booked !");
                    }
                    else
                    {
                        System.out.println("Failed to book appointment !");
                    }

                }catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                System.out.println("Doctor is not available on this date");
            }
        }
        else
        {
            System.out.println("Either doctor or patient doesn't exist");
        }


    }



    //main method

    public static void main(String[] args) {

        //installing all the necessary drivers for DB connection
        //register the MySQL driver using the forName() method that takes string as an input
        //generally the forname method of class "Class" takes classname in string and returns the object of that class
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e) // class named "Class" of java throws an exception of "ClassNotFoundException" so creating an instance of it as "e" to catch it in catch block
        {
            e.printStackTrace();
        }


        //invoking scanner class that will be used in Patient class

        Scanner sc = new Scanner(System.in);


        //connection with DB
        try
        {
            //making a instance of Drivermanager interface of java.sql by passing the credentials of DB as parameters and storing it in a instance of Connection interface of java.sql
            Connection connection = DriverManager.getConnection(url, username, password);

            //making the objects of Patient and doctor class and passing the parameters into their parameterized constructors
            Patient patient = new Patient(connection, sc);
            Doctor doctor = new Doctor(connection);

            //initiating while loop to display the main menu to the user by using switch case
            while(true)
            {
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patient");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.println("Enter your choice : ");
                int choice = sc.nextInt();

                switch(choice)
                {
                    case 1:
                        //add patient - calling method of "addPatient" from patient class with the help of patient class's object created above & similar for all
                        patient.addPatient();
                        System.out.println(); //for better output formatting
                        break;

                    case 2:
                        // view patient
                        patient.viewPatients();
                        System.out.println();
                        break;

                    case 3:
                        //view doctors
                        doctor.viewDoctors();
                        System.out.println();
                        break;

                    case 4:
                        //book appointment
                        bookAppointment(patient, doctor, connection, sc);
                        System.out.println();
                        break;

                    case 5:
                        System.out.println("Thanks for using our service !");
                        return;



                    default:
                        System.out.println("Enter valid choice !!!");
                        break;
                }
            }

        }catch(SQLException e) //when we invoke "getconnection" method an exception comes named "SQLException"
        {
            e.printStackTrace();
        }

    }
}
