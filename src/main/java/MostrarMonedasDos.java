import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.sql.*;
import java.util.Enumeration;

@WebServlet("/MostrarCli") //context //nombre del servlet
public class MostrarMonedasDos extends HttpServlet {
    Connection connection;

    public void init(ServletConfig config) {
        ServletContext context = config.getServletContext();

        //mostramos los parametros del contexto
        Enumeration<String> attributeNames = context.getInitParameterNames();

        while(attributeNames.hasMoreElements()) {
            String eachName = attributeNames.nextElement();
            System.out.println("Context Param name: " + eachName);
        }

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager
                    .getConnection(context.getInitParameter("dbUrl"), context.getInitParameter("dbUser"), context.getInitParameter("dbPassword"));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }




    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) {
        try {
            Statement stmt = connection.createStatement();
            res.setContentType("text/html");
            PrintWriter out = res.getWriter();
            ResultSet rs = stmt
                    .executeQuery("SELECT id, nombre FROM moneda\n" +
                            "ORDER BY id ASC;");
            out.println("<html>");
            out.println("<body>");
            out.println("<h1 style=\"color:red;\">  Las monedas son  </h1>");

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");

                out.println("<p>ID = " + id + "</p>");
                out.println("<p>NOMBRE = " + nombre + "</p>");
                out.println("/////////////////////////////////////////////");
            }
            out.println("</body>");
            out.println("</html>");
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

    }

    public void destroy() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
