package com.vk.tottenham.jaxb;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DateAdapter extends XmlAdapter<String, Date> {
                                                                //Wed, 05 Oct 16 17:18:00 GMT
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yy HH:mm:ss z");

    @Override
    public String marshal(Date v) throws Exception {
        synchronized (dateFormat) {
            return dateFormat.format(v);
        }
    }

    @Override
    public Date unmarshal(String v) throws Exception {
        synchronized (dateFormat) {
            return dateFormat.parse(v);
        }
    }

}
