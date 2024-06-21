package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Doctor
{

    private Connection connection; //private data member for instance sharing of DB from main method

    //creating public parametrized contructor to take the values that will come from the connection instance
    //passing objects as parameters of constructor

    public Doctor(Connection connection)
    {
        this.connection = connection;
    }



    //doctors will get added directly from DB ADMIN so no need of adding doctor method by user



    //making method 1 : view doctors()

    public void viewDoctors()
    {
        // creating a query to run into DB
        String query = "select * from doctors";

        // try catch block to handle the sqlexception as we are connecting to the database
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // ResultSet is an interface in java.sql package that holds the table from DB and helps us print the data from get method using next() pointer
            ResultSet resultSet = preparedStatement.executeQuery();

            // formatting for table type output
            System.out.println("Doctors : ");
            System.out.println("+------------+------------------------------+----------------------+");
            System.out.println("| Doctors id |            Name              |  Specialization      |");
            System.out.println("+------------+------------------------------+----------------------+");

            while(resultSet.next()) //sets the pointer to our table and helps print our data
            {
                //making some java local variables to store the data coming from DB
                // via get() method we can extract data from DB, just we need to provide the datatype & parameter will be the column name of the table in DB
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String specialization = resultSet.getString("specialization");

                //printf is used for formatted output & "%-12s" means to leave 12 spaces
                System.out.printf("|%-12s|%-30s|%-22s|\n", id, name, specialization);
                System.out.println("+------------+------------------------------+----------------------+");

            }

        }catch(SQLException e)
        {
            e.printStackTrace();
        }

    }


    //making method 2 : checking Doctor's data whether they exist or not (boolean method)

    public boolean getDoctorById(int id)
    {
        String query = "select * from doctors WHERE id = ?";

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
