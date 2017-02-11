package com.vk.tottenham.template;

public enum Year {
    _2010("photo-15474997_399986176"), _2011("photo-15474997_399986179"), 
    _2012("photo-15474997_399986185"), _2013("photo-15474997_399986188"), 
    _2014("photo-15474997_399986192"), _2015("photo-15474997_399986196"),
    //TODO: the two below are fake
    _2016("photo-15474997_399986196"), _2017("photo-15474997_399986196");

    private String icon;

    private Year(String icon) {
        this.icon = icon;
    }

    public static Year fromFullYear(String season) {
        String year = season.substring(0, 4);
        return valueOf("_"+ year);
    }

    public String icon() {
        return icon;
    }
}
