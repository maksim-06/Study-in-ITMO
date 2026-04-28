package org.example.rest;


import org.example.bean.AuthBean;
import org.example.bean.PointBean;
import org.example.dto.ErrorResponse;
import org.example.dto.PointRequest;
import org.example.dto.PointResponse;
import org.example.entity.PointResult;
import org.example.entity.User;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/points")
public class PointResource {
    @EJB
    private PointBean pointBean;

    @EJB
    private AuthBean authBean;


    @POST
    @Path("/check")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response checkPoint(PointRequest pointRequest,
                               @Context HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return Response.status(401)
                    .entity(new ErrorResponse("Not authenticated"))
                    .build();
        }

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return Response.status(401)
                    .entity(new ErrorResponse("Session expired"))
                    .build();
        }

        User user = authBean.findUserById(userId);
        if (user == null) {
            return Response.status(401)
                    .entity(new ErrorResponse("User not found"))
                    .build();
        }

        PointResult result = pointBean.savePoint(pointRequest.getX(), pointRequest.getY(), pointRequest.getR(), user);
        if (result != null) {
            return Response.ok()
                    .entity(new PointResponse(result))
                    .build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Not found......"))
                    .build();
        }
    }


    @GET
    @Path("/my-points")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserPoints(@Context HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return Response.status(401).entity("Not authenticated").build();
        }

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return Response.status(401).entity("Session expired").build();
        }

        User user = authBean.findUserById(userId);
        if (user == null) {
            return Response.status(401).entity("User not found").build();
        }

        List<PointResult> points = pointBean.getUserPoints(userId);

        List<Map<String, Object>> result = new ArrayList<>();
        for (PointResult point : points) {
            Map<String, Object> pointMap = new HashMap<>();
            pointMap.put("x", point.getX());
            pointMap.put("y", point.getY());
            pointMap.put("r", point.getR());
            pointMap.put("isShoot", point.getIsShoot());
            pointMap.put("timestamp", point.getTimestamp());
            result.add(pointMap);
        }

        return Response.ok(result).build();
    }
}