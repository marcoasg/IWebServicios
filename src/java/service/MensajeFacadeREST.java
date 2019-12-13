/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import entity.Mensaje;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Trigi
 */
@Stateless
@Path("entity.mensaje")
public class MensajeFacadeREST extends AbstractFacade<Mensaje> {

    @EJB
    private HiloFacadeREST hiloREST;

    @Context
    private HttpServletResponse response;

    private void addHeaders() {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type, Accept");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PATCH, PUT, DELETE, OPTIONS");
    }

    @PersistenceContext(unitName = "IngWebServiciosBDPU")
    private EntityManager em;

    public MensajeFacadeREST() {
        super(Mensaje.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Mensaje entity) {
        addHeaders();
        entity.setFecha(new Date());
        System.err.println(entity.getHilo().toString());
        super.create(entity);
    }

    @POST
    @Path("hilo/{hilo}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create2(@PathParam("hilo") Integer hilo, Mensaje entity) {
        //addHeaders();
        entity.setFecha(new Date());
        entity.setHilo(hiloREST.find(hilo));
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Mensaje entity) {
        addHeaders();
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        addHeaders();
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Mensaje find(@PathParam("id") Integer id) {
        addHeaders();
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Mensaje> findAll() {
        addHeaders();
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Mensaje> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        addHeaders();
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        addHeaders();
        return String.valueOf(super.count());
    }

    @GET
    @Path("from_hilo/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Mensaje> mensajesHilo(@PathParam("id") Integer id) {
        addHeaders();
        return em.createNamedQuery("Mensaje.findByHilo").setParameter("id", id).getResultList();
    }

    @GET
    @Path("findByIntervaloFechas/{fechaMinima}/{fechaMaxima}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Mensaje> findByIntervaloFechas(@PathParam("fechaMinima") Date fechaMinima, @PathParam("fechaMaxima") Date fechaMaxima) {
        addHeaders();
        return em.createNamedQuery("Mensaje.findByIntervaloFechas").setParameter("fechaMinima", fechaMinima).setParameter("fechaMaxima", fechaMaxima).getResultList();
    }

    @OPTIONS
    public Response opts() {
        Response r = Response.ok("")
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "Content-Type, Accept")
                .header("Access-Control-Allow-Methods", "GET, POST, PATCH, PUT, DELETE, OPTIONS")
                .build();
        return r;
    }

    @OPTIONS
    @Path("hilo/{hilo}")
    public Response opts2() {
        Response r = Response.ok("")
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "Content-Type, Accept")
                .header("Access-Control-Allow-Methods", "GET, POST, PATCH, PUT, DELETE, OPTIONS")
                .build();
        return r;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
