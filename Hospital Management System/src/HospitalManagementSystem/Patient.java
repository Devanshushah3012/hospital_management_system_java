package HospitalManagementSystem;

import java.sql.Connection; // Connection is an interface from java.sql package
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient
{

    private Connection connection; //private data member for instance sharing of DB from main method
    private Scanner sc;

    //creating public parametrized contructor to take the values that will come from the connection instance
    //passing objects as parameters of constructor

    public Patient(Connection connection, Scanner sc)
    {
        this.connection = connection;
        this.sc = sc;
    }



    //making method 1 : add patient()

    public void addPatient()
    {
        System.out.println("Enter Patient's Name : ");
        String name = sc.next();

        System.out.println("Enter Patient's Age : ");
        int age = sc.nextInt();

        System.out.println("Enter Patient's Gender : ");
        String gender = sc.next();


        // try catch block to handle the sqlexception as we are connecting to the database
        try
        {
            // creating the query to insert the data entered by the user above into DB

            String query = "INSERT INTO patients(name, age, gender) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query); // java.sql package has a method/interface "preparedstatement" that allows us to pass the SQL Query into DB

            //now we need to set the parameters of our query to pass into DB via preparedstatement
            preparedStatement.setString(1,name); //setting "name" at the 1st placeholder of our query to pass
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);

            //executing the query into database by using executeUpdate() method of preparedstatement method
            // and storing it into a int variable to store the count of affected rows as executeUpdate() method returns a count/int value

            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows>0)
            {
                System.out.println("Patient Added Successfully !!");
            }
            else {
                System.out.println("Failed to add patient :-(( ");
            }

        }catch (SQLException e) //making instance `e` of SQLException
        {
            e.printStackTrace(); //to print the coming exception data
        }
    }




    //making method 2 : view patients()

    public void viewPatients()
    {
        // creating a query to run into DB
        String query = "select * from patients";

        // try catch block to handle the sqlexception as we are connecting to the database
        try
        {
           PreparedStatement preparedStatement = connection.prepareStatement(query);
           // ResultSet is an interface in java.sql package that holds the table from DB and helps us print the data from get method using next() pointer
            ResultSet resultSet = preparedStatement.executeQuery();

            // formatting for table output
            System.out.println("Patients : ");
            System.out.println("+------------+-----------+----------+-----------+");
            System.out.println("| Patient id |    Name   |    Age   |   Gender  |");
            System.out.println("+------------+-----------+----------+-----------+");
            while(resultSet.next()) //sets the pointer to our table and helps print our data
            {
                //making some java local variables to store the data coming from DB
                // via get() method we can extract data from DB, just we need to provide the datatype & parameter will be the column name of the table in DB
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                //printf is used for formatted output & "%-12s" means to leave 12 spaces
                System.out.printf("|%-12s|%-11s|%-10s|%-11s|\n",id , name, age, gender);
                System.out.println("+------------+-----------+----------+-----------+");
            }

        }catch(SQLException e)
        {
            e.printStackTrace();
        }

    }


    //making method 3 : checking patients data whether they exist or not (boolean method)

    public boolean getPatientById(int id)
    {
        String query = "select * from patients WHERE id = ?";

        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            //passing id into query using set method of preparedstatement
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) //.next() shows that any data/row exists or not (boolean type)
            {
                return true;
            }
            else
            {
                return false;
            }


        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false; //coz this is a parameterized method with "boolean return type"
    }

}
