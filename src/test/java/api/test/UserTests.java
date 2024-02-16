package api.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoint.UserEndPoints;
import api.payload.User;
import io.restassured.response.Response;

public class UserTests {
	
	Faker faker;
	User userPayload;
	
	public Logger logger;
	
	@BeforeClass
	public void setup(){ 
		faker = new Faker();
		userPayload = new User();
		
		userPayload.setId(faker.idNumber().hashCode());
		userPayload.setUsername(faker.name().username());
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setPassword(faker.internet().password(5,10));
		userPayload.setPhone(faker.phoneNumber().cellPhone());
		
		
		// logs
		logger = LogManager.getLogger(this.getClass());
		logger.debug("debuging...");
		logger.info("infoing...");
	}
	
	@Test(priority=1)
	public void testPostUser() {		
		logger.info("************ Executing: testPostUser() ************");
		Response response = UserEndPoints.createUser(userPayload);
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(), 200);
		
		logger.info("************ Executed: testPostUser() ************");
	}
	
	@Test(priority=2)
	public void testGetUserByName() {
		logger.info("************ Executing: testGetUserByName() ************");
		Response response = UserEndPoints.readUser(userPayload.getUsername());
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(), 200);
		logger.info("************ Executed: testGetUserByName() ************");
	}
	
	@Test(priority=3)
	public void testUpdateUserByName() {
		logger.info("************ Executing: testUpdateUserByName() ************");
		// update data using payload
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		
		Response response = UserEndPoints.updateUser(userPayload.getUsername(), userPayload);
		response.then().log().all();
		response.then().log().body().statusCode(200);
		
		Assert.assertEquals(response.getStatusCode(), 200);
		
		// Checking data after update
		Response responseAfterUpdate = UserEndPoints.readUser(userPayload.getUsername());
		Assert.assertEquals(responseAfterUpdate.getStatusCode(), 200);
		logger.info("************ Executed: testUpdateUserByName() ************");
	}
	
	@Test(priority=4)
	public void testDeleteUserByName() {
		logger.info("************ Executing: testDeleteUserByName() ************");

		Response response = UserEndPoints.deleteUser(userPayload.getUsername());
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(), 200);
		logger.info("************ Executed: testDeleteUserByName() ************");
	}

}
