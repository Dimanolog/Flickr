/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Servlet Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloWorld
*/

package com.github.dimanolog.flickr.backend;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CheckVersionServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("application/json");
        Version version = new Version();
        String ver = ResourceBundle.getBundle("backend").getString("flickr.version");
        String hardUpdate= ResourceBundle.getBundle("backend").getString("flickr.hard.update");
        version.setVersion(Integer.valueOf(ver));
        version.sethardUpdate(Boolean.valueOf(hardUpdate));
        new Gson().toJson(version, resp.getWriter());
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String name = req.getParameter("name");
        resp.setContentType("text/plain");
        if (name == null) {
            resp.getWriter().println("Please enter a name");
        }
        resp.getWriter().println("Hello " + name);
    }
}
