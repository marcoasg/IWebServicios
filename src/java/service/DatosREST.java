/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import javax.ejb.Stateless;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.json.*;

/**
 *
 * @author adry1
 */
@Stateless
@Path("datos")
public class DatosREST {
    
    @Context
    private HttpServletResponse response;
    
    private void addHeaders(){
        response.addHeader("Access-Control-Allow-Origin", "*");
    }

    @GET
    @Path("fuentes")
    @Produces(MediaType.APPLICATION_JSON)
    public String fuentesREST() throws MalformedURLException, IOException, JSONException {
        installTrustManager();
        addHeaders();
        URL url = new URL("https://datosabiertos.malaga.eu/recursos/ambiente/fuentesaguapotable/da_medioAmbiente_fuentes-4326.geojson");
        return getGeojson(url).toString();
    }
    
    @GET
    @Path("musculacion")
    @Produces(MediaType.APPLICATION_JSON)
    public String musculacionREST() throws MalformedURLException, IOException, JSONException {
        installTrustManager();
        addHeaders();
        URL url = new URL("https://datosabiertos.malaga.eu/recursos/deportes/equipamientos/da_deportesZonasMusculacion-4326.geojson");
        return getGeojson(url).toString();
    }
    
    @GET
    @Path("aparcamientos_bici")
    @Produces(MediaType.APPLICATION_JSON)
    public String aparcamientosREST() throws MalformedURLException, IOException, JSONException {
        installTrustManager();
        addHeaders();
        URL url = new URL("https://datosabiertos.malaga.eu/recursos/transporte/EMT/EMTubicaparcbici/da_aparcamientosBiciEMT-4326.geojson");
        return getGeojson(url).toString();
    }
    
    @GET
    @Path("carriles_bici")
    @Produces(MediaType.APPLICATION_JSON)
    public String carrilesBici() throws MalformedURLException, IOException, JSONException {
        installTrustManager();
        addHeaders();
        URL url = new URL("https://datosabiertos.malaga.eu/recursos/transporte/trafico/da_carrilesBici-4326.geojson");
        return getGeojson(url).toString();
    }
    
    @GET
    @Path("plazas_libres/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String aparcamientosLibresREST(@PathParam("id") Integer id) throws MalformedURLException, IOException, JSONException {
        installTrustManager();
        URL url = new URL("https://datosabiertos.malaga.eu/api/3/action/datastore_search?resource_id=3bb304f9-9de3-4bac-943e-7acce7e8e8f9");     
        return getFeature("NUM_LIBRES", getRecords(getGeojson(url)), id);
    }
    
    @GET
    @Path("fuente_cercana/{x}/{y}")
    @Produces(MediaType.APPLICATION_JSON)
    public String fuente_cercana(@PathParam("x") Double x, @PathParam("y") Double y ) throws MalformedURLException, IOException, JSONException {
        installTrustManager();
        addHeaders();
        URL url = new URL("https://datosabiertos.malaga.eu/recursos/ambiente/fuentesaguapotable/da_medioAmbiente_fuentes-4326.geojson");
        return masCercano(x,y,getFeatures(getGeojson(url))).toString();
    }
    
    @GET
    @Path("bici_cercana/{x}/{y}")
    @Produces(MediaType.APPLICATION_JSON)
    public String bici_cercana(@PathParam("x") Double x, @PathParam("y") Double y ) throws MalformedURLException, IOException, JSONException {
        installTrustManager();
        addHeaders();
        URL url = new URL("https://datosabiertos.malaga.eu/recursos/transporte/EMT/EMTubicaparcbici/da_aparcamientosBiciEMT-4326.geojson");
        return masCercano(x,y,getFeatures(getGeojson(url))).toString();
    }
    
    @GET
    @Path("musculacion_cercana/{x}/{y}")
    @Produces(MediaType.APPLICATION_JSON)
    public String musculacion_cercana(@PathParam("x") Double x, @PathParam("y") Double y ) throws MalformedURLException, IOException, JSONException {
        installTrustManager();
        addHeaders();
        URL url = new URL("https://datosabiertos.malaga.eu/recursos/deportes/equipamientos/da_deportesZonasMusculacion-4326.geojson");
        return masCercano(x,y,getFeatures(getGeojson(url))).toString();
    }
        
    
    public void installTrustManager(){
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {

		public java.security.cert.X509Certificate[] getAcceptedIssuers()
		{
		    return null;
		}
                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
		{
		                //No need to implement.
		}
		public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
		{
		                //No need to implement.
                }
            }
        };

		// Install the all-trusting trust manager
        try 
        {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } 
        catch (Exception e) 
        {
            System.out.println(e);
        }
    }

    private JSONObject getGeojson(URL url) throws JSONException, IOException {
        HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
        conn.addRequestProperty("Access-Control-Allow-Origin", "localhost:8080");
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputInline;
        StringBuffer response = new StringBuffer();
       
        while((inputInline = in.readLine())!= null){
            response.append(inputInline);
        }
        in.close();
        
        JSONObject json = new JSONObject(response.toString());
        return json;
    }
    
    private String getFeature( String record, JSONArray records, Integer id) throws JSONException {
        int i=0;
        while(i<records.length()){
            if(records.getJSONObject(i).getInt("ID_EXTERNO")==id){
                return records.getJSONObject(i).get(record).toString();
            }
            i++;
        }
        return "-1";
    }

    private JSONObject masCercano(Double x, Double y, JSONArray records) throws JSONException {
        int i=0;
        JSONObject jo = null;
        JSONObject res = null;
        Double dist = Double.MAX_VALUE;
        while(i<records.length()){
            jo = records.getJSONObject(i);
            if(distancia(jo.getJSONObject("geometry").getJSONArray("coordinates").getDouble(0),jo.getJSONObject("geometry").getJSONArray("coordinates").getDouble(1) , x, y)< dist){
                dist = distancia(jo.getJSONObject("geometry").getJSONArray("coordinates").getDouble(0),jo.getJSONObject("geometry").getJSONArray("coordinates").getDouble(1) , x, y);
                res = jo;
            }
            i++;
        }
        return res;
    }
    
    
    private JSONArray getFeatures(JSONObject json) throws JSONException{
        return json.getJSONArray("features");
    }
    
    private JSONArray getRecords(JSONObject json) throws JSONException{
        return json.getJSONObject("result").getJSONArray("records");
    }
    
    private Double distancia(double latitud, double longitud, Double x, Double y) {
        return Math.sqrt(Math.pow(latitud-x, 2) + Math.pow(longitud-y, 2));
    }

}