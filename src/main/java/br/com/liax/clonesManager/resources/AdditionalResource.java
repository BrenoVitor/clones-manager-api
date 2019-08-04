package br.com.liax.clonesManager.resources;

import java.beans.PropertyVetoException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.liax.clonesManager.bos.AdditionalBO;
import br.com.liax.clonesManager.exceptions.AgeNotIntervalException;
import br.com.liax.clonesManager.exceptions.NameNotMatchedException;
import br.com.liax.clonesManager.models.Additional;

@Path("/additional")
public class AdditionalResource {

	private static final Logger LOGGER = LogManager.getLogger(AdditionalResource.class);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Response findById(@PathParam("id") long id) {
		LOGGER.info("Begin process endpoint findById");
		Additional additional = null;
		ResponseBuilder response = null;
		try {
			additional = AdditionalBO.selectById(id);
			if (additional == null) {
				response = Response.status(Response.Status.NOT_FOUND).entity("Adicional não encontrado");
			} else {
				response = Response.ok().entity(additional);
			}
		} catch (ClassNotFoundException | SQLException | PropertyVetoException e) {
			response = Response.serverError().entity(e.getMessage());
			LOGGER.error(e.getMessage(), e);
		}
		LOGGER.info("End process endpoint findById");
		return response.build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Additional additional)
			throws FileNotFoundException, ClassNotFoundException, SQLException, IOException {
		LOGGER.info("Begin process endpoint create");
		ResponseBuilder response = null;
		try {
			long id = AdditionalBO.create(additional);
			if (id == 0) {
				response = Response.serverError().entity("Não foi possivel cadastrar um adicional");
			} else {
				response = Response.created(URI.create("/clone/" + id));
			}
		} catch (ClassNotFoundException | SQLException | NameNotMatchedException | AgeNotIntervalException | PropertyVetoException e) {
			response = Response.serverError().entity(e.getMessage());
			LOGGER.error(e.getMessage(), e);
		}
		LOGGER.info("End process endpoint create");
		return response.build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response list() {
		LOGGER.info("Begin process endpoint list");
		ResponseBuilder response = null;
		try {
			response = Response.ok().entity(AdditionalBO.selectAll());
		} catch (ClassNotFoundException | SQLException | PropertyVetoException e) {
			response = Response.serverError().entity(e.getMessage());
			LOGGER.error(e.getMessage(), e);
		}
		LOGGER.info("End process endpoint list");
		return response.build();
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Response update(@PathParam("id") long id, Additional additionalChanged) {
		LOGGER.info("Begin process endpoint update");
		ResponseBuilder response = null;
		Additional additionalInitial;
		try {
			additionalInitial = AdditionalBO.selectById(id);
			if (additionalInitial == null) {
				response = Response.status(Response.Status.NOT_FOUND).entity("Adicional não encontrado");
			}
			response = AdditionalBO.update(additionalChanged) > 0 ? Response.ok()
					: Response.serverError().entity("Não foi possivel atualizar um adicional");
		} catch (ClassNotFoundException | SQLException | NameNotMatchedException | AgeNotIntervalException | PropertyVetoException e) {
			response = Response.serverError().entity(e.getMessage());
			LOGGER.error(e.getMessage(), e);
		}

		LOGGER.info("End process endpoint update");
		return response.build();
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Response delete(@PathParam("id") long id) {
		LOGGER.info("Begin process endpoint delete");
		ResponseBuilder response = null;
		Additional additionalInitial;
		try {
			additionalInitial = AdditionalBO.selectById(id);
			if (additionalInitial == null) {
				response = Response.status(Response.Status.NOT_FOUND).entity("Adicional não encontrado");
			}
			response = AdditionalBO.delete(id) > 0 ? Response.ok()
					: Response.serverError().entity("Não foi possivel deletar o adicional");
		} catch (ClassNotFoundException | SQLException | PropertyVetoException e) {
			response = Response.serverError().entity(e.getMessage());
			LOGGER.error(e.getMessage(), e);
		}
		LOGGER.info("End process endpoint delete");
		return response.build();
	}

}
