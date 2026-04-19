package org.example.finalproject.model;

import java.time.LocalDate;

public class Transaction
{
    int transactionId;
    LocalDate date;
    String type;
    String category;
    float amount;
    String description;

    public Transaction(int transactionId, LocalDate date, String type, String category, float amount, String description)
    {
        if(type.equalsIgnoreCase("income") || type.equalsIgnoreCase("expense"))
        {
            this.type = type;
        }
        else
        {
            this.type = "Income";
        }
        this.transactionId = transactionId;
        this.date = date;
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

    public float getAmount()
    {
        return amount;
    }

    public void setAmount(float amount)
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
