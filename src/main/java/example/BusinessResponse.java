package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import helpers.JsonHelper;

public class BusinessResponse {
    public Object accountUsage;
    public APIGatewayProxyRequestEvent event;
    public Context context;

    public String toString() {
        return JsonHelper.stringify(this);
    }
}
