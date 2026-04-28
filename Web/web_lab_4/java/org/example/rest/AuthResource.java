package org.example.rest;


import org.example.bean.AuthBean;
import org.example.dto.AuthRequest;
import org.example.dto.AuthResponse;
import org.example.entity.User;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/auth")
public class AuthResource {

    @EJB
    private AuthBean authBean;


    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(AuthRequest request,
                             @Context HttpServletRequest httpRequest) {
        if (request.getLogin() == null || request.getPassword() == null) {
            return Response.status(400)
                    .entity(new AuthResponse("Login and password are required", null, null))
                    .build();
        }

        User user = authBean.registerUser(request.getLogin(), request.getPassword());

        if (user != null) {
            httpRequest.getSession().setAttribute("userId", user.getId());
            return Response.ok()
                    .entity(new AuthResponse(user.getId(), user.getLogin()))
                    .build();
        } else {
            return Response.status(409)
                    .entity(new AuthResponse( "User already exists",null,null))
                    .build();
        }
    }


    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(AuthRequest request,
                          @Context HttpServletRequest httpRequest) {
        if (request.getLogin() == null || request.getPassword() == null) {
            return Response.status(400)
                    .entity(new AuthResponse( "Login and password are required", null, null))
                    .build();
        }

        User user = authBean.login(request.getLogin(), request.getPassword());

        if (user != null) {
            httpRequest.getSession().setAttribute("userId", user.getId());
            return Response.ok()
                    .entity(new AuthResponse(user.getId(), user.getLogin()))
                    .build();
        } else {
            return Response.status(401).entity(
                    new AuthResponse( "Invalid username or password", null,null))
                    .build();
        }
    }


    @POST
    @Path("/logout")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response logout(@Context HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return Response.ok(new AuthResponse("Logged out successfully", null, null)).build();
    }
}
