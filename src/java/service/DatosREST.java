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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.json.*;

/**
 *
 * @author adry1
 */
@Stateless
@Path("datos")
public class DatosREST {

    @GET
    @Path("fuentes")
    @Produces(MediaType.APPLICATION_JSON)
    public String fuentesREST() throws MalformedURLException, IOException, JSONException {
        installTrustManager();
        URL url = new URL("https://datosabiertos.malaga.eu/api/3/action/datastore_search?resource_id=8d0f2d6e-b99d-42f1-929e-8bf25938af27");
        return getRecords(url).toString();
    }
    
    @GET
    @Path("musculacion")
    @Produces(MediaType.APPLICATION_JSON)
    public String musculacionREST() throws MalformedURLException, IOException, JSONException {
        installTrustManager();
        URL url = new URL("https://datosabiertos.malaga.eu/api/3/action/datastore_search?resource_id=15bc08d4-dad1-4bae-a644-682d474bc90e");
        return getRecords(url).toString();
    }
    
    @GET
    @Path("aparcamientos_bici")
    @Produces(MediaType.APPLICATION_JSON)
    public String aparcamientosREST(@PathParam("id") Integer id) throws MalformedURLException, IOException, JSONException {
        installTrustManager();
        URL url = new URL("https://datosabiertos.malaga.eu/api/3/action/datastore_search?resource_id=3bb304f9-9de3-4bac-943e-7acce7e8e8f9");
        return getRecords(url).toString();
    }
    
     @GET
    @Path("plazas_libres/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String aparcamientosLibresREST(@PathParam("id") Integer id) throws MalformedURLException, IOException, JSONException {
        installTrustManager();
        URL url = new URL("https://datosabiertos.malaga.eu/api/3/action/datastore_search?resource_id=3bb304f9-9de3-4bac-943e-7acce7e8e8f9");     
        return getRecord("NUM_LIBRES", getRecords(url), id);
    }
    
     @GET
    @Path("carriles_bici")
    @Produces(MediaType.APPLICATION_JSON)
    public String carrilesBici(@PathParam("id") Integer id) throws MalformedURLException, IOException, JSONException {
        installTrustManager();
        URL url = new URL("https://datosabiertos.malaga.eu/api/3/action/datastore_search?resource_id=9b3b8fe2-8c03-4420-b718-f36ea541f8fd");
        return getRecords(url).toString();
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

    private String getRecord( String record, JSONArray records, Integer id) throws JSONException {
        int i=0;
        while(i<records.length()){
            if(records.getJSONObject(i).getInt("ID")==id){
                return records.getJSONObject(i).get(record).toString();
            }
            i++;
        }
        return "-1";
    }

    private JSONArray getRecords(URL url) throws JSONException, IOException {
        HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputInline;
        StringBuffer response = new StringBuffer();
       
        while((inputInline = in.readLine())!= null){
            response.append(inputInline);
        }
        in.close();
        
        JSONObject json = new JSONObject(response.toString());
        JSONArray records = json.getJSONObject("result").getJSONArray("records");
        return records;
    }

}