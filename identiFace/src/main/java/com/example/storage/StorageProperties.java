package com.example.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {
	  // we can setup location here

    private String location = "d:\\testupload";

    //private String location = "d:\testupload";

    public String getLocation() {

        return location;

    }


    public void setLocation(String location) {

        this.location = location;

    }
}
