package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;

import helpers.JsonHelper;
import software.amazon.awssdk.services.lambda.model.GetAccountSettingsRequest;
import software.amazon.awssdk.services.lambda.model.GetAccountSettingsResponse;
import software.amazon.awssdk.services.lambda.model.ServiceException;
import software.amazon.awssdk.services.lambda.LambdaAsyncClient;
import software.amazon.awssdk.services.lambda.model.AccountUsage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.StringBuilder;
import java.util.Map;
import java.util.List;
import java.util.concurrent.CompletableFuture;

// Handler value: example.Handler
public class Handler implements RequestHandler<SQSEvent, Object>{
  private static final Logger logger = LoggerFactory.getLogger(Handler.class);
  private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
  private static final LambdaAsyncClient lambdaClient = LambdaAsyncClient.create();
  public Handler(){
    CompletableFuture<GetAccountSettingsResponse> accountSettings = lambdaClient.getAccountSettings(GetAccountSettingsRequest.builder().build());
    try {
      GetAccountSettingsResponse settings = accountSettings.get();
    } catch(Exception e) {
      e.getStackTrace();
    }
  }
  @Override
  public Object handleRequest(SQSEvent event, Context context)
  {
    String response = "";
    // call Lambda API
    logger.info("Getting account settings");
    CompletableFuture<GetAccountSettingsResponse> accountSettings =
        lambdaClient.getAccountSettings(GetAccountSettingsRequest.builder().build());
    // log execution details
    logger.info("ENVIRONMENT VARIABLES: {}", gson.toJson(System.getenv()));
    logger.info("CONTEXT: {}", gson.toJson(context));
    logger.info("EVENT: {}", gson.toJson(event));


    // process event
    List<SQSMessage> records = event.getRecords();

    if(records != null && records.size() > 0) {
      for (SQSMessage msg : event.getRecords()) {
        logger.info(msg.getBody());
      }
    }
    // process Lambda API response
    try {
      GetAccountSettingsResponse settings = accountSettings.get();
      response = gson.toJson(settings.accountUsage());
      logger.info("Account usage: {}", response);
    } catch(Exception e) {
      e.getStackTrace();
    }

    BusinessResponse bres = new BusinessResponse();
    bres.accountUsage = response;
    bres.event = event;
    bres.context = context;

    AwsResponse res = new AwsResponse();
    res.isBase64Encoded = false;
    res.statusCode = 200;
    res.body = JsonHelper.stringify(bres);
    res.headers = new Object();

    return res;
  }
}