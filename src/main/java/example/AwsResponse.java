package example;

import helpers.JsonHelper;

public class AwsResponse {
    public boolean isBase64Encoded ;
    public int statusCode;
    public Object headers;
    public String body;

    public String toString(){
        return JsonHelper.stringify(this);
    }
}
