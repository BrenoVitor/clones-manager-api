package br.com.liax.clonesManager.resources;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.Properties;

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

import br.com.liax.clonesManager.bos.CloneBO;
import br.com.liax.clonesManager.daos.CloneDAO;
import br.com.liax.clonesManager.exceptions.AgeNotIntervalException;
import br.com.liax.clonesManager.exceptions.NameNotMatchedException;
import br.com.liax.clonesManager.models.Clone;
import br.com.liax.clonesManager.utils.LoadProperties;

@Path("/clone")
public class CloneResource {

	private static final String CLONE_DELETE_DEFAULT_ERROR = "Não foi possivel deletar o clone";

	private static final String CLONE_DELETE_ERROR = "clone.delete.error";

	private static final String CLONE_CREATE_ERROR_DEFAULT_MESSAGE = "Não foi possivel atualizar um clone";

	private static final String CLONE_UPDATE_ERROR = "clone.update.error";

	private static final String CLONE_CREATE_ERROR_DEAFULT_MESSAGE = "Não foi possivel cadastrar um clone";

	private static final String CLONE_CREATE_ERROR_MESSAGE = "clone.create.error";

	private static final String CLONE_NOT_FOUND_DEFAULT_MESSAGE = "Clone não encontrado";

	private static final String CLONE_NOT_FOUND_MESSAGE = "clone.not.found";

	private static final Logger LOGGER = LogManager.getLogger(CloneResource.class);

	private static final Properties PROPERTIES = LoadProperties.getProperties();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Response findById(@PathParam("id") final long id) {
		LOGGER.info("Begin process endpoint findById");
		Clone clone = null;
		ResponseBuilder response = null;
		try {
			clone = CloneBO.selectById(id);
			if (clone == null) {
				response = cloneNotFoudResponse();
			} else {
				response = Response.ok().entity(clone);
			}
		} catch (final Exception e) {
			response = serverErrorResponse(e.getMessage());
			LOGGER.error(e.getMessage(), e);
		}
		LOGGER.info("End process endpoint findById");
		return response.build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(final Clone clone)
			throws FileNotFoundException, ClassNotFoundException, SQLException, IOException {
		LOGGER.info("Begin process endpoint create");
		ResponseBuilder response = null;
		try {
			long id = CloneBO.create(clone);
			if (id == 0) {
				response = serverErrorResponse(PROPERTIES.getProperty(CLONE_CREATE_ERROR_MESSAGE,CLONE_CREATE_ERROR_DEAFULT_MESSAGE));
			} else {
				response = Response.created(URI.create("/clone/" + id));
			}
		} catch (final Exception e) {
			response = serverErrorResponse(e.getMessage());
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
			response = Response.ok().entity(CloneBO.selectAll());
		} catch (final Exception e) {
			response = serverErrorResponse(e.getMessage());
			LOGGER.error(e.getMessage(), e);
		}
		LOGGER.info("End process endpoint list");
		return response.build();
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Response update(@PathParam("id") final long id, final Clone cloneChanged) {
		LOGGER.info("Begin process endpoint update");
		ResponseBuilder response = null;
		Clone cloneInitial;
		try {
			cloneInitial = CloneDAO.selectById(id);
			if (cloneInitial == null) {
				response = cloneNotFoudResponse();
			} else {
				response = CloneBO.update(cloneChanged) > 0 ? Response.ok()
						: serverErrorResponse(PROPERTIES.getProperty(CLONE_UPDATE_ERROR, CLONE_CREATE_ERROR_DEFAULT_MESSAGE));
			}
		} catch (final Exception e) {
			response = serverErrorResponse(e.getMessage());
			LOGGER.error(e.getMessage(), e);
		}

		LOGGER.info("End process endpoint update");
		return response.build();
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Response delete(@PathParam("id") final long id) {
		LOGGER.info("Begin process endpoint delete");
		ResponseBuilder response = null;
		Clone cloneInitial;
		try {
			cloneInitial = CloneBO.selectById(id);
			if (cloneInitial == null) {
				response = cloneNotFoudResponse();
			} else {
				response = CloneBO.delete(id) > 0 ? Response.ok()
						: serverErrorResponse(PROPERTIES.getProperty(CLONE_DELETE_ERROR,CLONE_DELETE_DEFAULT_ERROR));
			}
		} catch (final Exception e) {
			response = serverErrorResponse(e.getMessage());
			LOGGER.error(e.getMessage(), e);
		}
		LOGGER.info("End process endpoint delete");
		return response.build();
	}

	private ResponseBuilder cloneNotFoudResponse() {
		return Response.status(Response.Status.NOT_FOUND)
				.entity(PROPERTIES.getProperty(CLONE_NOT_FOUND_MESSAGE, CLONE_NOT_FOUND_DEFAULT_MESSAGE));
	}
	
	private ResponseBuilder serverErrorResponse(final String message) {
		return Response.serverError().entity(message);
	}

}
