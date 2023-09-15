package com.tickets.tasks;

import java.util.List;

public interface DataHandlerRunnable extends Runnable{
    public DataHandlerRunnable setDataParam(List list, String clientId);
}
