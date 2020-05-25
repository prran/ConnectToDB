package com.example.analysetableinmysql.page

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.analysetableinmysql.ConnectToServer
import com.example.analysetableinmysql.MainActivity
import com.example.analysetableinmysql.R
import com.example.analysetableinmysql.SQLOverListener
import com.example.analysetableinmysql.jmodule.Table
import kotlinx.android.synthetic.main.enter_table_button.*
import kotlinx.android.synthetic.main.fragment_table_list.*
import java.sql.Connection

class TableListPage(vararg netInfo: String) : Fragment()
{
    private val netInfo = netInfo
    private var ConnectToServer = ConnectToServer(netInfo)
    private var viewgroup : ViewGroup? = null


    override fun onCreateView(inflater : LayoutInflater, container: ViewGroup?, savedInstanceState : Bundle?): View {
        viewgroup = container
        return inflater.inflate(com.example.analysetableinmysql.R.layout.fragment_table_list, container, false)
    }

    override fun onStart() {
        super.onStart()

        ConnectToServer.execute("show tables;")

        ConnectToServer.setSQLOverListener(object : SQLOverListener {
            override fun onSQLOvered() {
                var tableList = ConnectToServer.get();
                var table = Table(tableList,",","/");
                table.removeTuple(table.size)

                var list = table.getAttribute("Tables_in_AWSdb",null)
                table.showTable()

                val INFALTE = Context.LAYOUT_INFLATER_SERVICE;
                val li = context?.getSystemService(INFALTE) as LayoutInflater

                for(text in list)
                {
                    val button : Button = li.inflate(R.layout.enter_table_button,null,true) as Button
                    button.text = text
                    button.setOnClickListener {
                        val act = activity as MainActivity
                            act.pageChange(TableInfoPage(netInfo))

                    }
                    list_layout.addView(button)
                }
            }
        })

    }
}