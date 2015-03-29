package com.tsangworks.server;

/**
 *
 */
public abstract class AbstractServerHost
{

    protected boolean runningService = true;

    protected String messageBodyString = null;
    protected MessageHeader messageHeader;
    protected String messageHeaderString = null;

    protected long startHeader = 0;
    protected long endHeader = 0;
    protected long startBody = 0;
    protected long endBody = 0;

    protected abstract void runHost();
}
