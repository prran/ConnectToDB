
var mysql = require("mysql");
var express = require("express");
const StringBuilder = require("string-builder");
var sb = new StringBuilder();

function connectMySQL(HOST,DB,ID,PASSWORD)
{
    console.log("DATA SET \n");
    console.log("HOST :\t" + inputData.HOST + "\n");
    console.log("DB :\t" + inputData.DB + "\n");
    console.log("ID :\t" + inputData.ID + "\n");
    console.log("PASS :\t" + inputData.PASSWORD + "\n");

    var connection = mysql.createConnection({
        host     : HOST,
        user     : ID,
        password : PASSWORD,
        database : DB
      });

    console.log("Try to connect MySQL ... \n");
    connection.connect();

    return connection;
}

function working()
{
    app.post('/post',function(req,res){//만약에 데이터를 받았다고 한다면
        req.on('data', function(data){//이 데이터를
            inputData = JSON.parse(data);//inputData에 집어넣음
            var conn = connectMySQL(inputData.HOST,inputData.DB,inputData.ID,inputData.PASSWORD)
            console.log("try do query : "+ inputData.QUERY + "\n");
            
            try
            {
                conn.query(inputData.QUERY, function (error, results, fields) {

                    sb = null;
                    sb = new StringBuilder();

                    for (var keyNm in results[0])
                    {sb.append(keyNm + ",");}
                    sb.append("/");
                
                    for (var i = 0; i < results.length; i++) {

                        console.log("Line " + (i+1) + " : ")

                        for ( var keyNm in results[i]) {
                            sb.append(results[i][keyNm] + ",")
                            console.log(results[i][keyNm])
                        }
                        sb.append("/");
                    }

                    var wannaValue = sb.toString()

                    console.log("return is" + wannaValue)
                    res.writeHead(200, { 'Content-Type' : 'text/plain'})
                    res.write(wannaValue)
                    res.end()
                    sb.clear()

                });

                conn.end();
            }
            catch(exception) {
                console.error(exception); 
                conn.end();
            }
        });

        //res.set('Content-Type', 'text/html');
        //res.send(sb.toString());
        //res.json(undefinedData);

    });
    
    
}
var app = express();
working();
console.log("server will be wait a request ...");

app.listen(3000);
//process.exit(0);