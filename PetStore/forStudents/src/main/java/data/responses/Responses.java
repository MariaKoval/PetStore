package main.java.data.responses;

public class Responses {
    private long code;
    private String type;
    private String message;

    public long getCode() {
        return code;
    }

    public void setCode(long value) {
        this.code = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String value) {
        this.type = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String value) {
        this.message = value;
    }

    @Override
    public String toString() {
        return "{\"code\":" + code + ", \"type\":" + "\"" + type + "\"" + ", \"message\":" + "\"" + message + "\"" + "}";
    }
}

