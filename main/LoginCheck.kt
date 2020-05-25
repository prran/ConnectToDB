package com.example.analysetableinmysql

import android.app.AlertDialog
import android.widget.TextView
import android.os.AsyncTask
import android.util.Log
import java.lang.Exception
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement


class LoginCheck : AsyncTask<Void, Int, Boolean>() {

    lateinit var URL : String
    lateinit var ID : String
    lateinit var PASSWORD : String
    private var mysql : Connection? = null
    var isConn = false

    fun setConnection(URL:String,ID:String,PASSWORD:String)
    {
        this.URL = URL
        this.ID = ID
        this.PASSWORD = PASSWORD
    }

    override fun doInBackground(vararg strings: Void): Boolean? {

        connectToMySQL()

        return isConn
    }

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun onPostExecute(s: Boolean?) {
        super.onPostExecute(s)
    }

    override fun onCancelled(s: Boolean?) {
        super.onCancelled(s)
    }

    private fun connectToMySQL()
    {
        try
        {
            mysql = DriverManager.getConnection(URL, ID, PASSWORD)
            isConn = true
            Log.d("testt",""+isConn)
            mysql?.close()
        }
        catch (e : SQLException)
        {
            isConn = false
        }
    }

    fun isConnected() : Boolean
    {
        return isConn
    }
}