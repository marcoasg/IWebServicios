/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import entity.Hilo;
import java.util.Date;
import java.util.List;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Trigi
 */
@Stateless
@Path("entity.hilo")
public class HiloFacadeREST extends AbstractFacade<Hilo> {

    @Context
    private HttpServletResponse response;

    private void addHeaders() {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PATCH, PUT, DELETE, OPTIONS");
    }

    @PersistenceContext(unitName = "IngWebServiciosBDPU")
    private EntityManager em;

    public HiloFacadeREST() {
        super(Hilo.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Hilo entity) {
        addHeaders();
        System.err.println(entity.getUsuario().getEmail());
        entity.setFecha(new Date());
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Hilo entity) {
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
    public Hilo find(@PathParam("id") Integer id) {
        addHeaders();
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Hilo> findAll() {
        addHeaders();
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Hilo> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
    @Path("hilosPorMensajesUsuario/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Hilo> hilosPorMensajesUsuario(@PathParam("id") String id) {
        addHeaders();
        return em.createNamedQuery("Hilo.hilosPorMensajesUsuario").setParameter("id", id).getResultList();
    }

    @GET
    @Path("tema/{tema}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Hilo> hilosPorTema(@PathParam("tema") String tema) {
        addHeaders();
        return em.createNamedQuery("Hilo.findByTema").setParameter("tema", tema).getResultList();
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

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
