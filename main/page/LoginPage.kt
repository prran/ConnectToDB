package com.example.analysetableinmysql.page

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.fragment_login.*
import java.sql.DriverManager
import java.sql.SQLException
import android.app.AlertDialog
import android.util.Log
import android.widget.Button
import com.example.analysetableinmysql.LoginCheck
import com.example.analysetableinmysql.MainActivity
import com.example.analysetableinmysql.R
import java.sql.Connection as Connection




class LoginPage : Fragment(){

    private lateinit var mysql : LoginCheck

    override fun onCreateView(inflater : LayoutInflater, container: ViewGroup?, savedInstanceState : Bundle?): View {

        try
        {Class.forName("com.mysql.jdbc.Driver")}
        catch (e: ClassNotFoundException)
        { Log.e("class","class not found"); }


        return inflater.inflate(com.example.analysetableinmysql.R.layout.fragment_login, container, false)
    }

    override fun onStart() {
        super.onStart()

        login_button.setOnClickListener {
            val URL = "jdbc:mysql://" + server_domain_text.text + "/" + use_database_text.text
            val ID = id_text.text.toString()
            val PASSWORD = password_text.text.toString()
            val HOST = server_domain_text.text.toString()
            val DB = use_database_text.text.toString()

            mysql = LoginCheck()
            mysql.setConnection(URL,ID,PASSWORD)
            var isconn = mysql.execute().get();

            val act = activity as MainActivity

            if(isconn)
                act.pageChange(TableListPage(HOST,DB,ID,PASSWORD))
            else
            {
                val builder = AlertDialog.Builder(act)
                builder.setTitle("failed").setMessage("접속에 실패했습니다.")
                val alertDialog = builder.create()
                alertDialog.show()
            }

        }
    }

}