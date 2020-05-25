package com.example.analysetableinmysql.jmodule;

import android.util.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Table
{
    private ArrayList<String> indexTuple = new ArrayList<String>();
    private ArrayList<String> dataType = new ArrayList<>();
    private ArrayList<HashMap<String,String>> table = new ArrayList<>();

    public final static String INTEGER = "INT", FLOAT = "FLOAT", LONG = "BIGINT", DOUBLE = "DOUBLE", TEXT = "TEXT", NULL = "TINYINT";

    public Table(final String tableStream, final String lineSplit, final String rowSplit)
    {
        String rowString = tableStream.replace(rowSplit,"\n");
        String lineString = rowString.replace(lineSplit,",");
        BufferedReader stream = new BufferedReader(new StringReader(lineString));
        createTable(stream,1);
    }
    public Table(final File file, int startLine)
    {
        try
        {
            BufferedReader stream = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF8"));
            createTable(stream,startLine);
        }
        catch(Exception e)
        {
            System.err.println("cannot connect to file");
        }
    }

    public void createTable(BufferedReader br, int startLine)
    {
        table.add(null);
        dataType.add("Data type : ");

        try {
            for(int i = 1; i<startLine; i++)
                br.readLine();

            String[] index = br.readLine().split(",");

            for(String data : index)
            {
                if(data.charAt(0)==data.charAt(data.length()-1) && data.charAt(0) == '\"')
                    data = data.substring(1,data.length()-1);
                data.trim();
                indexTuple.add(data);
            }

            ArrayList<Integer> dataSize = new ArrayList<>();
            dataSize.add(null);

            {
                String getter;
                while ((getter = br.readLine())!=null)
                {
                    String[] datas = getter.split(",");
                    HashMap<String,String> tuple = new HashMap<>();
                    int i = 1;
                    for(String data :datas)
                    {
                        if(data.charAt(0)==data.charAt(data.length()-1) && data.charAt(0) == '\"')
                        data = data.substring(1,data.length()-1);
                        data.trim();

                        try{
                            if(dataSize.get(i)<data.length())
                                dataSize.set(i,data.length());
                        }
                        catch (IndexOutOfBoundsException e){
                            dataSize.add(data.length());
                            dataType.add(NULL);
                        }


                        if(data.toLowerCase().equals("na")||data.toLowerCase().equals("null")||data.equals(" "))
                        {
                            data = "NA";
                            tuple.put(indexTuple.get(i-1),data);
                            i++;
                            continue;
                        }

                        tuple.put(indexTuple.get(i-1),data);

                        String integer = null, floating = null;

                        if(dataSize.get(i)<10)
                        {
                            integer = INTEGER;
                            floating = FLOAT;
                        }
                        else
                        {
                            integer = LONG;
                            floating = DOUBLE;
                        }

                            try{
                                if(dataType.get(i)!=floating)
                                Integer.parseInt(data);
                                dataType.set(i,integer);
                            }
                            catch(Exception e)
                            {
                                try {
                                    Float.parseFloat(data);
                                    dataType.set(i,floating);
                                }
                                catch(Exception ex)
                                {
                                    if(dataSize.get(i)<64)
                                        dataType.set(i,"CHAR(" + dataSize.get(i) + ")");
                                    else if(dataSize.get(i)<10000)
                                        dataType.set(i,TEXT + "(" + dataSize.get(i) + ")");
                                    else if(dataSize.get(i) < 4000000)
                                        dataType.set(i,TEXT + "MEDIUMTEXT");
                                    else
                                        dataType.set(i,TEXT + "LONGTEXT");
                                }
                            }

                        i++;
                    }

                    table.add(tuple);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setNumberNull(Integer replaceNum)
    {

        for(int i = 1; i<table.size(); i++)
        {
            HashMap<String,String> tuple = table.get(i);

            for(int j = 0; j<indexTuple.size();j++)
            {
                String index = indexTuple.get(j);
                if(tuple.get(index).toUpperCase().equals("NULL") || tuple.get(index).toUpperCase().equals("NA"))
                {
                    if(dataType.get(j+1).equals("INT")||dataType.get(j+1).equals("FLOAT")||dataType.get(j+1).equals("BIGINT")||dataType.get(j+1).equals("DOUBLE"))
                    {
                        tuple.put(index,replaceNum.toString());
                    }
                }
            }
        }
    }

    public void showTable()
    {
        for(String index :indexTuple)
            System.out.print(index + "\t");
        System.out.print("\n");


        for(int i = 1; i<table.size(); i++)
        {
            HashMap<String,String> get = table.get(i);
            for (int j = 0; j < indexTuple.size(); j++)
                System.out.print(get.get(indexTuple.get(j)) + "\t");
            System.out.println("\n");
        }

        for(String type :dataType)
            System.out.print(type + "\t");
    }

    public ArrayList<String> getProperty()
    {
        return indexTuple;
    }

    public ArrayList<String> getDataType()
    {
        return dataType;
    }

    public ArrayList<String> getTuple(int index, String replaceNULL)
    {
        if(replaceNULL == null)
            replaceNULL = "NULL";
        HashMap<String,String> get;

        try
        {get = table.get(index);}
        catch(Exception e)
        {return null;}

        ArrayList<String> result = new ArrayList<>();
        for (int j = 0; j < indexTuple.size(); j++)
        {
            String data = get.get(indexTuple.get(j));
            if(data.toUpperCase().equals("NULL")||data.toUpperCase().equals("NA"))
                data = replaceNULL;
            result.add(data);
        }

        return result;
    }

    public ArrayList<String> getAttribute(String property, String replaceNULL)
    {
        if(indexTuple.indexOf(property) == -1)
            return null;

        if(replaceNULL == null)
            replaceNULL = "NULL";

        HashMap<String,String> get;

        ArrayList<String> result = new ArrayList<>();
        try
        {
            for (int i = 1; i < table.size(); i++)
            {
                get = table.get(i);
                String data = get.get(property);
                if(data.toUpperCase().equals("NULL")||data.toUpperCase().equals("NA"))
                    data = replaceNULL;
                result.add(data);
            }
        }
        catch(Exception e)
        {
            return null;
        }
        return result;
    }

    public int getSize()
    {
        return table.size()-1;
    }

    public boolean removeTuple(int index)
    {
        try {
            table.remove(index);
            return true;
        }
        catch(IndexOutOfBoundsException e){
            return false;
        }
    }
}
