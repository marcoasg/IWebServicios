import java.util.Set;
import javax.ws.rs.core.Application;

@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }
    private void addRestResourceClasses(Set<Class<?>> resources) {
        //add all resources classes
        resources.add(service.HiloFacadeREST.class);
        resources.add(service.MensajeFacadeREST.class);
        resources.add(service.UsuarioFacadeREST.class);
        resources.add(service.TemaFacadeREST.class);
    }
}