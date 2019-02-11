package com.jwt.rest;
 
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.xml.bind.annotation.*;

import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONObject;

import com.sun.jersey.core.util.Base64;


/**
 * A test rest service that can be deployed to apache tomcat.
 * @author doug bass
 *
 */
@Path("/hello")
public class HelloWorldService {
  
/*    @GET
    @Path("/{name}")
    public Response getMsg(@PathParam("name") String name) {
  
        String output = "Welcome   : " + name;
  
        return Response.status(200).entity(output).build();
  
    }
  
*/
	
	/**
     * Return the specified record.
     * @param apikey the api token for our application. This is a unique id for each application we integrate
     * @param input the json with fields recordId and inputParams (as specified in the config json), eg:
     * {"recordId":"TP-325","inputParams":{"project":"TP", "issueType":"10102"}}
     * @return the response in the "data" field, eg, { "data": { "summary": "Sample test 1", "description": "Sample test 1", "status": "Open", "priority": "1", "severity": "2", "id": "1" } }
     * response status of 201 is success, json contains {"result":"success"}
     */
    // http://localhost:8080/RESTfullApp/rest/hello/1/get_record?apikey=abc
    @POST @Consumes("application/json") @Produces("application/json")
    @Path("/1/application/record/get")
    public Response getRecord(@QueryParam("apikey") String apikey, final String input) {
  
        String output = "Get Record service, apikey   : " + apikey;
        
        try {
        JSONObject o = new JSONObject(input);
	     String recordId = o.getString("recordId");
	     JSONObject inputParams = o.getJSONObject("inputParams");
	     output += ", recordId="+recordId + ", inputParams="+inputParams;
        } catch (Exception e) {
        	e.printStackTrace();
        }
        System.out.println("Rcvd srguments are: " + output);
        String result =
        		"{\"data\":\n"+
        		"    {\n"+
        		"		\"name\": \"John Doe\",\n"+
        		"		\"position\": \"Clerk\",\n"+
        		"		\"status\": \"Active\",\n"+
        		"		\"Salary\": \"30000\",\n"+
        		"		\"address\": \"10 main st, woburn, MA\",\n"+
        		"		\"id\": \"11234\"\n"+
          		"    }\n"+
          		"}\n";
        return Response.status(201).entity(result).build();
  
    }
    
	/**
     * Find the records that match the specified criteria like greater than modified date
     * @param apikey the api token for our application. This is a unique id for each application we integrate
     * @param input the json with fields lastModifiedTime and inputParams (as specified in the config json), eg:
     * { "lastModifiedTime": "2016-11-28 12:00:00", "inputParams": { "project": "TP", "issueType": "10102" } }
     * @return the record count in the totalrecords field and the records in the data array
	 * { "totalrecords": 2, "data": [{ "id": "1", "fields": { "summary": "Sample test 1", "description": "Sample test 1", "status": "Open", "priority": "1", "severity": "2", "id": "1" } }, { "id": "2", "fields": { "summary": "Sample test 2", "description": "Sample test 2", "status": "Open", "priority": "3", "severity": "5", "id": "2" } } ] }     
	 * * response status of 201 is success, json contains {"result":"success"}
     */
    // http://localhost:8080/RESTfullApp/rest/hello/1/application/record/find?apikey=abc
    @POST @Consumes("application/json") @Produces("application/json")
    @Path("/1/application/record/find")
    public Response findRecords(@QueryParam("apikey") String apikey, final String input) {
  
    	System.out.println("Called find service, apikey="+apikey+", input args="+input);
        String output = "Get Record service, apikey   : " + apikey;
        
        try {
        JSONObject o = new JSONObject(input);
	     String lastModifiedTime = o.getString("lastModifiedTime");
	     JSONObject inputParams = o.getJSONObject("inputParams");
	     output += ", lastModifiedTime="+lastModifiedTime + ", inputParams="+inputParams;
        } catch (Exception e) {
        	e.printStackTrace();
        }
        System.out.println("Rcvd srguments are: " + output);
        String result =
        		"{\"data\":\n"+
        		"    [{\n"+
        		"		\"Summary\": \"John Doe\",\n"+
        		"		\"position\": \"Clerk\",\n"+
        		"		\"status\": \"Active\",\n"+
        		"		\"Salary\": \"30000\",\n"+
        		"		\"address\": \"10 main st, woburn, MA\",\n"+
        		"		\"id\": \"11234\"\n"+
          		"    }\n"+
          		"]}\n";
        return Response.status(201).entity(result).build();
  
    }
    
    /*
     sync flow: 
     	connect (username, password, login_items)
     		isConnected
     			readUpdatedRecords
     				split_record collection
     					mule_transformations
     						update_record
     							disconnect
     */
  
    /**
     * Authenticate the specified user to our application at the url http://localhost:8080/RESTfullApp/rest/hello/1/application/login?apikey=abc
     * @param apikey the api token for our application. This is a unique id for each application we integrate
     * @param input the json with fields username and password and login_options, eg:
     * {"username":"doug","password":"today","login_options":{"domain":"dev"}}
     * @return the login token which will be passed to subsequent calls
     */
    // http://localhost:8080/RESTfullApp/rest/hello/1/application/login?apikey=abc
    @POST @Consumes("application/json") @Produces("application/json")
    @Path("/1/application/login")
    public Response connect(@QueryParam("apikey") String apikey, final String input) {
  
        String output = "Login service, apikey   : " + apikey + ", input args="+input;
        
        try {
        JSONObject o = new JSONObject(input);
	     String username = o.getString("username");
	     String password = new String(Base64.decode(o.getString("password")));
	     System.out.println("Clear text password="+o.getString("password"));
	     JSONObject inputParams = null;
	     if (o.containsKey("login_options"))
	    	 inputParams = o.getJSONObject("login_options");
	     output += ", username="+username + ", password="+password+", inputParams="+inputParams;
        } catch (Exception e) {
        	e.printStackTrace();
            return Response.status(500).entity("Input argument parse error").build();
        }
        System.out.println("Rcvd srguments are: " + output);
        
        // now go to your custom application and do a login and return the login token
        String loginToken = "{\"token\":\"12345\"}"; 
        return Response.status(200).cookie(new NewCookie(new Cookie("token","12345"))).entity(loginToken).build();
  
    }
    
  
    /**
     * Return success if a session exists
     * @param apikey the api token for our application. This is a unique id for each application we integrate
     * @param input the json with fields username and the session cookies, eg:
     * {"username":"doug","session":[{"name":"token","value":"token=12345;Version=1"}]}
     * @return json with fields statusCode, isConnected (true or false), and errorMessage
     */
    // http://localhost:8080/RESTfullApp/rest/hello/1/application/is_connected?apikey=abc
    @POST @Consumes("application/json") @Produces("application/json")
    @Path("/1/application/isConnected")
    public Response isConnected(@QueryParam("apikey") String apikey, final String input) {
  
        System.out.println("isConnected service, apikey   : " + apikey + ", inpiut args="+input);
        String output = "isConnected service, apikey   : " + apikey + ", input args="+input;
        try {
        JSONObject o = new JSONObject(input);
	     String username = o.getString("username");
	     JSONArray inputParams = o.getJSONArray("session");
	     output += ", username="+username + ", inputParams="+inputParams;
        } catch (Exception e) {
        	e.printStackTrace();
            return Response.status(500).entity("Input argument parse error").build();
        }
        
        // verify a valid session exists
        boolean sessionExists = true;
        if (sessionExists)
        	return Response.status(200).entity("{\"statusCode\":\"200\",\"isConnected\":\"true\",\"errorMessage\":\"\"}").build();
        else
        	return Response.status(200).entity("{\"statusCode\":\"200\",\"isConnected\":\"false\",\"errorMessage\":\"\"}").build();
  
    }
    
  
}
