package app;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.Persistence;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.appengine.api.utils.SystemProperty;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import data.Riista;

@WebServlet(
    name = "HelloAppEngine",
    urlPatterns = {"/hellogoogle1"}
)
public class HelloAppEngine extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException {

    response.setContentType("text/plain");
    response.setCharacterEncoding("UTF-8");
    PrintWriter out=response.getWriter();
    
    DataSource pool=null;
    Connection conn=null;
    if (SystemProperty.environment.value() ==SystemProperty.Environment.Value.Production) {  
    	out.println("Production version");
    	pool=this.getPool();
		try {
			conn=pool.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    else {    
    	try {
        	out.println("Development version");
			conn=this.getDevConnection();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    try {
    	if (conn!=null) {
		Statement stmt=conn.createStatement();
		stmt.executeUpdate("insert into riista(laji) values(\"Lapasotka6\")");
		ResultSet RS=stmt.executeQuery("select * from riista");
		while (RS.next()) {
			out.println(RS.getString("laji"));
		}
		conn.close();
    	}
    	else {
    		out.println("Ei yhteyttä tietokantaan!");
    	}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

  }
  
  public DataSource getPool() {
	// The configuration object specifies behaviors for the connection pool.
	  HikariConfig config = new HikariConfig();

	  // Configure which instance and what database user to connect with.
	  config.setJdbcUrl(String.format("jdbc:mysql:///%s", "hellogoogle1")); //e.g. hellogoogle1
	  config.setUsername("root"); // e.g. "root", "postgres"
	  config.setPassword("kukkuu"); // e.g. "my-password"

	  // For Java users, the Cloud SQL JDBC Socket Factory can provide authenticated connections.
	  // See https://github.com/GoogleCloudPlatform/cloud-sql-jdbc-socket-factory for details.
	  config.addDataSourceProperty("socketFactory", "com.google.cloud.sql.mysql.SocketFactory");
	  config.addDataSourceProperty("cloudSqlInstance", "rare-keep-252905:europe-west3:hellogoogle1mysql");



	  config.addDataSourceProperty("useSSL", "false");

	  // ... Specify additional connection properties here.
	  // ...

	  
	  // Initialize the connection pool using the configuration object.
	  DataSource pool = new HikariDataSource(config);
	  return pool;
  }
  public Connection getDevConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
	  Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
//	  Class.forName("com.mysql.jdbc.Driver").newInstance();
	  Connection conn=java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/hellogoogle1", "pena", /*"kukkuu");//*/ System.getProperty("salasana"));
//	  Connection conn=java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/hellogoogle1?useSSL=true&user=pena&password=kukkuu");
	  return conn;
  }
}