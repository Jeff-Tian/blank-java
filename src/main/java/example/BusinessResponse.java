package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import helpers.JsonHelper;

public class BusinessResponse {
    public Object accountUsage;
    public SQSEvent event;
    public Context context;

    public String toString() {
        return JsonHelper.stringify(this);
    }
}
