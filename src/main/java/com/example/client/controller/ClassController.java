package com.example.client.controller;


import com.example.client.model.Employees;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.logging.LoggingFeature;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class ClassController {

    private final String REST_API_lIST="http://localhost:8080/api/employees/list";
    private final String REST_API_CREAT="http://localhost:8080/api/employees/create";
    private final String REST_API_UPDATE="http://localhost:8080/api/employees/update";
    private final String REST_API_DELETE="http://localhost:8080/api/employees/delete/";
//    private final String REST_API_GET_ID="http://localhost:8080/api/user/list";

    @GetMapping(value = {"/","/listEmployees"})
    public String index(Model model){
        Client client = createJerseyRestClient();
        WebTarget target = client.target(REST_API_lIST);
        List<Employees> ls =  target.request(MediaType.APPLICATION_JSON_TYPE).get(List.class);
        model.addAttribute("lsEmployees",ls);
        return "ListEmployees";
    }

    private static Client createJerseyRestClient() {
        ClientConfig clientConfig = new ClientConfig();

        // Config logging for client side
        clientConfig.register( //
                new LoggingFeature( //
                        Logger.getLogger(LoggingFeature.DEFAULT_LOGGER_NAME), //
                        Level.INFO, //
                        LoggingFeature.Verbosity.PAYLOAD_ANY, //
                        10000));

        return ClientBuilder.newClient(clientConfig);
    }

    @GetMapping(value = "createnewemployees")
    public String createNewUser(){
        return "CreateEmployees";
    }

    @PostMapping(value = "saveemployees")
    public String saveemployees(@RequestParam String name, @RequestParam String salary){
        Employees e = new com.example.client.model.Employees();
        e.setName(name);
        e.setSalary(salary);

        String jsonClass = convertToJson(e);
        Client client = createJerseyRestClient();
        WebTarget target = client.target(REST_API_CREAT);
        Response response = target.request(MediaType.APPLICATION_JSON_TYPE)
        .post(Entity.entity(jsonClass,MediaType.APPLICATION_JSON));
        return "redirect:/listClass";
    }
    private static String convertToJson(com.example.client.model.Employees ex) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(ex);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("deleteEmployees")
    public String deleteClass(Integer id){
        Client client = createJerseyRestClient();
        WebTarget target = client.target(REST_API_DELETE + id);
        Response response = target.request(MediaType.APPLICATION_JSON_TYPE).delete();
        return "redirect:/listClass";
    }
}
