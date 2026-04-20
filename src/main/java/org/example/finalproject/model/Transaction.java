package org.example.finalproject.model;

import java.time.LocalDate;

public class Transaction
{
    int transactionId;
    LocalDate date;
    String type;
    String category;
    double amount;
    String description;

    public Transaction(LocalDate date, String type, String category, double amount, String description)
    {
        this.date = date;
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.description = description;
    }

    public Transaction(int id, LocalDate date, String type, String category, double amount, String description)
    {
        this.transactionId = id;
        this.date = date;
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.description = description;
    }

    public int getTransactionId()
    {
        return transactionId;
    }

    public void setTransactionId(int transactionId)
    {
        this.transactionId = transactionId;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        if(type.equalsIgnoreCase("income") || type.equalsIgnoreCase("expense"))
        {
            this.type = type;
        }
        else
        {
            this.type = "Income";
        }
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public double getAmount()
    {
        return amount;
    }

    public void setAmount(double amount)
    {
        this.amount = amount;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
