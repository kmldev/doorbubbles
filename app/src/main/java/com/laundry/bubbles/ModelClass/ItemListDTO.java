package com.laundry.bubbles.ModelClass;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemListDTO implements Serializable {



         String   service_id="";
         String   service_name="";
        ArrayList<ItemServiceDTO>services= new ArrayList<>();

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public ArrayList<ItemServiceDTO> getServices() {
        return services;
    }

    public void setServices(ArrayList<ItemServiceDTO> services) {
        this.services = services;
    }
}
