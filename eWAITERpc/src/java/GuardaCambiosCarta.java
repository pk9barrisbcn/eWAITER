/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
import javax.servlet.MultipartConfigElement;
import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPFile;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import it.sauronsoftware.ftp4j.FTPListParseException;*/
import java.io.*;
import java.security.MessageDigest;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;

/**
 *
 * @author Peter
 */
@WebServlet(urlPatterns = {"/GuardaCambiosCarta"})
public class GuardaCambiosCarta extends HttpServlet {

    private Connection con;
    private Statement set;
    private ResultSet rs;
    String cad;
    String pas;

    @Override
    public void init(ServletConfig cfg) throws ServletException {//esto es la conexion
        String sURL = "jdbc:mysql://db4free.net";
        //String sURL="jdbc:mysql://localhost/a7416073_ewaiter";
        super.init(cfg);
        //String userName = "root";
        //String password = "";
        String userName = "ewaiter";
        String password = "ewaiterroot100";
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection(sURL, userName, password);
            System.out.println("Se ha conectado");
        } catch (Exception e) {
            System.out.println("No se ha conectado");
        }
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException, IllegalStateException {//todo lo que hago con java
        // Obtener la sesion

        // Guardar el nombre del cliente en la sesión
        // para poderlo utilizar en el siguiente servlet
        String precio = (String) request.getParameter("precio");
        String nombre = (String) request.getParameter("nombre");
        String descripcion = (String) request.getParameter("descripcion");
        String categoriaPrincipal = (String) request.getParameter("CatPrin");
        String subcategoria = (String) request.getParameter("CatSub");
        String id = (String) request.getParameter("id");
        String imagen = (String) request.getParameter("foto");
        boolean existe = false;

        try {
            String consulta = "UPDATE ewaiter.Producto SET Nombre = '" + nombre + "', Precio = " + precio + ", Descripcion = '" + descripcion + "' WHERE ID_Producto = " + id + " ;";
            Statement sentenciadelinsert = con.createStatement();
            sentenciadelinsert.executeUpdate(consulta);

            existe = true;

            rs.close();
            set.close();
        } catch (Exception e) {
            System.out.println("No lee de la tabla");
        }
    }   
/*
        //SUBIR A LA CARPETA INTERNA
        //esto lo hacemos para que luego se pueda encontrar la imagen deseada a subir
 /*
//Ruta donde se guardara el fichero
		File destino = new File("C:\\Devtroce\\Uploads\\");
		ServletRequestContext src = new ServletRequestContext(request);
 
		if(ServletFileUpload.isMultipartContent(src)){
			DiskFileItemFactory factory = new DiskFileItemFactory((1024*1024),destino);
			ServletFileUpload upload = new  ServletFileUpload(factory);
 
			java.util.List lista;    
                        lista = upload.parseRequest(request);
                        File file = null;
			java.util.Iterator it = lista.iterator();
 
			while(it.hasNext()){
				FileItem item=(FileItem)it.next();
				if(item.isFormField())
					out.println(item.getFieldName()+"<br>");
				else
				{
					file=new File(item.getName());
					item.write(new File(destino,file.getName()));
					out.println("Fichero subido");
				} // end if
			} // end while
		} // end if
*/



        /*
        String url= "F:\\M13 Projecte\\netbeans\\eWAITER\\eWAITERpc\\web\\Recursos\\ImgTemp";
        DiskFileItemFactory df = new DiskFileItemFactory();
        df.setSizeThreshold(15360);//tamaño máximo del archivo
        df.setRepository(new File(url));//le decimos la ruta donde lo debe guardar
        ServletFileUpload upload = new ServletFileUpload(df);
        
        try{
            List<FileItem> partes = upload.parseRequest(request);
            
        }catch(Exception e){
            
        }
        
         
        //SUBIR AL FTP EXTERNO
        FTPClient ftp = new FTPClient();

        try {
            ftp.connect("ewaiter.netau.net");
            ftp.login("a9467752", "ewaiterroot100");
            //FTPFile[] list = ftp.list("public_html/fotos/carta"); -- lo usamos para ver si conecta

            String fotillo = "C:\\Users\\Adri\\Pictures\\1.jpg";
            ftp.changeDirectory("/public_html/fotos/carta");
            ftp.upload(new java.io.File(fotillo));

            System.out.println("hola");

            //nos los creemos
        } catch (FTPIllegalReplyException ex) {
            Logger.getLogger(GuardaCambiosCarta.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FTPException ex) {
            Logger.getLogger(GuardaCambiosCarta.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GuardaCambiosCarta.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FTPDataTransferException ex) {
            Logger.getLogger(GuardaCambiosCarta.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FTPAbortedException ex) {
            Logger.getLogger(GuardaCambiosCarta.class.getName()).log(Level.SEVERE, null, ex);
        }
        //dejamos de creer

        try {
            set = con.createStatement();
            if (existe) {
                response.sendRedirect(response.encodeRedirectURL("./Entrada/opAdministrador/carta/carta.jsp"));
            } else {
                response.sendRedirect(response.encodeRedirectURL("./Entrada/opAdministrador/carta/carta.jsp#Error_no_esperado_sry"));
            }
            rs.close();
            set.close();
        } catch (Exception e) {
            System.out.println("No inserta ni modifica la tabla");
        }
        // Llamada al servlet que nos visualiza
        // las estadísticas de jugadores
*/
     // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
