package com.tcc2.fisiowhatched.ui.Pacientes;

public class Status {

    private String data;
    private String id;
    private String status;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Status() {
    }

    public Status(String data, String id, String status) {
        this.data = data;
        this.id = id;
        this.status = status;
    }
}
