package cc.rinoux.server.service;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import cc.rinoux.server.annotation.Restful;


@Path("/calender/v1/")
@Restful
@Produces("appliaction/json")
public class CalenderService {

	@POST
	@Path("notes/{uid}/{cup_id}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	
	public void addNewNote(@PathParam("uid") Integer uid,
			@PathParam("cup_id") Integer cupId,
			@QueryParam("content") String noteContent,
			@QueryParam("date") String alertDate,
			@FormDataParam("note") InputStream noteInputStream,
			@FormDataParam("note") FormDataContentDisposition disposition){
		
	}

	
	

}
