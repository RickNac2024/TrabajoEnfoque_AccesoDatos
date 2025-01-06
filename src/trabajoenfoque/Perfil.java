/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package trabajoenfoque;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;




/**
 *
 * @author cealv
 */
public class Perfil {

    private String nif;
    private String adjudicatario;
    private String objetoGenerico;
    private String objeto;
    private String fechaAdjudicacion;
    private String importe;
    private String proveedoresConsultados;
    private String tipoContrato;

    public Perfil() {
    }

    public Perfil(String nif, String adjudicatario, String objetoGenerico, String objeto, String fechaAdjudicacion, String importe, String proveedoresConsultados, String tipoContrato) {
        this.nif = nif;
        this.adjudicatario = adjudicatario;
        this.objetoGenerico = objetoGenerico;
        this.objeto = objeto;
        this.fechaAdjudicacion = fechaAdjudicacion;
        this.importe = importe;
        this.proveedoresConsultados = proveedoresConsultados;
        this.tipoContrato = tipoContrato;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getAdjudicatario() {
        return adjudicatario;
    }

    public void setAdjudicatario(String adjudicatario) {
        this.adjudicatario = adjudicatario;
    }

    public String getObjetoGenerico() {
        return objetoGenerico;
    }

    public void setObjetoGenerico(String objetoGenerico) {
        this.objetoGenerico = objetoGenerico;
    }

    public String getObjeto() {
        return objeto;
    }

    public void setObjeto(String objeto) {
        this.objeto = objeto;
    }

    public String getFechaAdjudicacion() {
        return fechaAdjudicacion;
    }

    public void setFechaAdjudicacion(String fechaAdjudicacion) {
        this.fechaAdjudicacion = fechaAdjudicacion;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte(String importe) {
        this.importe = importe;
    }

    public String getProveedoresConsultados() {
        return proveedoresConsultados;
    }

    public void setProveedoresConsultados(String proveedoresConsultados) {
        this.proveedoresConsultados = proveedoresConsultados;
    }

    public String getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public void Insertar(Statement sentencia) throws SQLException {

        System.out.println("--- INICIO PARA INSERTAR CLIENTES");
        // Perfil personaje = new Perfil();

        String queryInsert;
        //int filasAfectadas;

        queryInsert = "INSERT INTO cliente "
                + "VALUES "
                + "('" + this.nif + "'"
                + ", '" + this.adjudicatario + "'"
                + ", '" + this.objetoGenerico + "'"
                + ", '" + this.objeto + "'"
                + ", '" + this.fechaAdjudicacion + "'"
                + ", '" + this.importe + "'"
                + ", '" + this.proveedoresConsultados + "'"
                + ", '" + this.tipoContrato + "')";

        sentencia.executeUpdate(queryInsert);
    }

    /**
     * @param usuario  usuario deL SGDB 
     * @param contrasena contraseña deL SGDB 
     * @return  tipo Statement para poder reutilizar su return en otras clases
     * @throws SQLException  controlar las excepciones
     */
    public static Statement conexion (String usuario, String contrasena) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/";
        Connection conexion = DriverManager.getConnection(url, usuario, contrasena);
        Statement sentencia = conexion.createStatement();
        return sentencia;
    }
    
    /**
     * 
     * @param sentencia  tipo Statement para poder establecer la conexion con la base de datos a utilizar
     * @param database  nombre de la base de datos a crear
     * @throws java.sql.SQLException  controlar las excepciones
    */
    public  void crearDataBase(Statement sentencia,String database) throws SQLException {
        String querydrop = "DROP DATABASE IF EXISTS " + database;
        String queryCreate = "CREATE DATABASE " + database;
        String queryuse = "USE " + database;
        
        sentencia.executeUpdate(querydrop);
        sentencia.executeUpdate(queryCreate);
        sentencia.executeUpdate(queryuse);
    }
    /**
     * 
     * @param sentencia  tipo Statement para poder establecer la conexion con la base de datos a utilizar
     * @throws SQLException  controlar las excepciones
     */
    public void crearTabla (Statement sentencia) throws SQLException{
        String crearTabla;
        crearTabla = """
                     CREATE TABLE CLIENTE (
                     nif VARCHAR (9) NOT NULL,
                     adjudicatario VARCHAR (100) NOT NULL,
                     objetoGenerico VARCHAR (100) NOT NULL,
                     objeto VARCHAR (100) NOT NULL,
                     fechaAdjudicacion VARCHAR (50) ,
                     importe VARCHAR (50) ,
                     proveedoresConsultados VARCHAR (50) NOT NULL,
                     tipoContrato VARCHAR (80) NOT NULL
                      )""";
        sentencia.executeUpdate(crearTabla);
    }

}
