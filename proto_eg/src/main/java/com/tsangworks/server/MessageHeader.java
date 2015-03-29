package com.tsangworks.server;

/**
 *
 */
public class MessageHeader
{

    private Integer recordSize;

    public void init(String messageHeaderString)
    {
        byte position = 0;
        recordSize = 440;
    }

    public Integer getRecordSize() {
        return recordSize;
    }

    public void setRecordSize(int recordSize)
    {
        this.recordSize = recordSize;
    }
}
