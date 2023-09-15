package com.tickets.sync;

public class SyncConstant {
    public static final String ACTIVITY = "activity";
    public static final String AISLE = "aisle";
    public static final String ENTER = "enter";
    public static final String ENTRANCE = "entrance";
    public static final String VENUES = "venues";


    public enum NameSpaceEnum{
        ACTIVITY("com.tickets.mapper.VenueActiviesMapper"),
        AISLE("com.tickets.mapper.AisleMapper"),
        ENTER("com.tickets.mapper.EntersMapper"),
        ENTRANCE("com.tickets.mapper.EntranceManagementMapper"),
        VENUES("com.tickets.mapper.VenueManagementMapper");
        private String nameSpace;

        NameSpaceEnum(String nameSpace){
            this.nameSpace = nameSpace;
        }
        public String getNameSpace() {
            return nameSpace;
        }

    }

    public static String getCurrentNamespace(String appType){
        if(ACTIVITY.equals(appType)){
            return NameSpaceEnum.ACTIVITY.getNameSpace();
        }

        if(AISLE.equals(appType)){
            return NameSpaceEnum.AISLE.getNameSpace();
        }

        if(ENTER.equals(appType)){
            return NameSpaceEnum.ENTER.getNameSpace();
        }

        if(ENTRANCE.equals(appType)){
            return NameSpaceEnum.ENTRANCE.getNameSpace();
        }

        if(VENUES.equals(appType)){
            return NameSpaceEnum.VENUES.getNameSpace();
        }
        return null;
    }

}
