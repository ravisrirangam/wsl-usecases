package org.mule.transport.ftp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleContext;
import org.mule.transport.AbstractMuleMessageFactory;
import org.mule.transport.file.FileConnector;

public class MyMessageFactory extends AbstractMuleMessageFactory
{

    private FTPClient ftpClient;
    private boolean streaming;

    public MyMessageFactory(MuleContext context)
    {
        super(context);
    }

    @Override
    protected Object extractPayload(Object transportMessage, String encoding) throws Exception
    {
        
        return transportMessage;
    }

    @Override
    protected Class<?>[] getSupportedTransportMessageTypes()
    {
        return new Class[]{FTPFile.class, List.class };
    }

    @Override
    protected void addProperties(DefaultMuleMessage message, Object transportMessage) throws Exception
    {
        super.addProperties(message, transportMessage);
        
       
    }

    public void setFtpClient(FTPClient ftpClient)
    {
        this.ftpClient = ftpClient;
    }

    public void setStreaming(boolean streaming)
    {
        this.streaming = streaming;
    }

}