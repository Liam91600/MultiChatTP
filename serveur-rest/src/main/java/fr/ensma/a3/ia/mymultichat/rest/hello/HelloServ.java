package fr.ensma.a3.ia.mymultichat.rest.hello;
// TODO: Documentation

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * HelloServ
 */
@Consumes(MediaType.TEXT_PLAIN)
@Produces(MediaType.TEXT_PLAIN)
@Path("hello")
public class HelloServ {

    @GET
    public Response ditHello() {
        return Response.ok(new String("Hello depuis le serveur REST !!!")).build();
    }
}
