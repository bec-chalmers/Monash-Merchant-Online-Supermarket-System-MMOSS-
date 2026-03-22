package controllers;

import entity.Customer;
import util.FileStorageUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddFundsController
{

    public boolean addFunds(Customer customer, double amount)
    {
        if (customer == null) return false;
        if (amount < 1.0 || amount > 1000.0) return false;

        String path = "data/Customer.txt";

        double oldBalance = customer.getBalance();
        double newBalance = oldBalance + amount;

        customer.setBalance(newBalance);

        try
        {
            List<List<String>> rows = FileStorageUtil.readMultiColumnFile(path);
            List<String> lines = new ArrayList<>();
            lines.add("email,password,first_name,last_name,mobile_number,date_of_birth,gender,building_number,street_number,street_name,suburb,state,postcode,is_student,balance");

            for (List<String> row : rows)
            {
                if (row == null || row.isEmpty())
                {
                    continue;
                }

                while (row.size() <=14)
                {
                    row.add("");
                }

                String email = row.getFirst();

                if (email != null && email.equalsIgnoreCase(customer.getEmail()))
                {
                    row.set(14, Double.toString(newBalance));
                }

                lines.add(String.join(",", row));
            }

            FileStorageUtil.writeToFile(path, lines);
            return true;
        }
        catch (IOException e)
        {
            customer.setBalance(oldBalance);
            return false;
        }
    }
}