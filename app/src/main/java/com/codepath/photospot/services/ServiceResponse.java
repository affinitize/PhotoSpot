package com.codepath.photospot.services;

/**
 * Created by dfrie on 4/5/2017.
 */

import org.apache.commons.lang3.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class ServiceResponse<T> {

    public enum ResponseCode {
        OK(200),
        NO_CONTENT(204),
        BAD_REQUEST(300),
        NOT_MODIFIED(304),
        UPDATE_ISSUE(400),
        UNAUTHORIZED(401),
        UNPROCESSABLE_ENTITY(422),
        RESOURCE_LOCKED(423),
        SERVER_ERROR(500);

        private int code;

        private ResponseCode(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

    public enum MessageSeverity {
        INFO, WARNING, ERROR;
    }

    private T t;
    private ResponseCode statusCode;
    private List<String> messageList = new ArrayList<>();

    public ResponseCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(ResponseCode statusCode) {
        this.statusCode = statusCode;
    }

    public void set(T t) {
        this.t = t;
    }

    public T get() {
        return t;
    }

    public List<String> getMessages() {
        return messageList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String s: messageList) {
            if (sb.length() > 0) {
                sb.append("; ");
            }
            sb.append(s);
        }
        return "ServiceResponse [t=" + t + ", statusCode=" + statusCode
                + ", messageList=" + sb.toString() + "]";
    }

    /**
     * @param code
     * @param message
     * @param severityType
     *            creates a String with code, message, severityType and adds to
     *            messgeList object.
     */
    public void addMessage(ResponseCode code, String message,
                           MessageSeverity severity) {
        String str = "%s: %s (%s)";
        messageList.add(str.format(severity.name(), message, code.name()));
        if (code.ordinal() > getStatusCode().ordinal()) {
            setStatusCode(code);
        }
    }

    /**
     * This method checks whether request is OK(200) and there is no message
     * returned from the service.
     *
     * @return boolean: true if everything is fine, client code can proceed to
     *         get() result set; otherwise, false
     */
    public boolean isResponseSuccess() {
        if (getStatusCode().equals(ResponseCode.OK)
                && (getMessages() == null || getMessages().size() == 0)) {
            return true;
        }
        return false;
    }

    /**
     * Use this method to log an exception and set message for UI
     *
     * @param ex
     * @param message
     */
    public void logException(Exception ex, String message) {
        setStatusCode(ResponseCode.SERVER_ERROR);

        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(message)) {
            sb.append(message);
        }
        if (ex != null) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            sb.append("\n").append(sw.toString());
        }

        addMessage(ResponseCode.SERVER_ERROR, sb.toString(), MessageSeverity.ERROR);

        ////log.error(sb.toString());
    }
}
