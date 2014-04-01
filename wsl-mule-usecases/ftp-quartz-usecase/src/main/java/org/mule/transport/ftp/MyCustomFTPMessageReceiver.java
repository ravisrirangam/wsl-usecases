package org.mule.transport.ftp;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import javax.resource.spi.work.Work;


import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPListParseEngine;
import org.apache.commons.net.ftp.FTPReply;
import org.mule.api.MessagingException;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.config.ThreadingProfile;
import org.mule.api.construct.FlowConstruct;
import org.mule.api.context.WorkManager;
import org.mule.api.endpoint.ImmutableEndpoint;
import org.mule.api.endpoint.InboundEndpoint;
import org.mule.api.exception.SystemExceptionHandler;
import org.mule.api.execution.ExecutionCallback;
import org.mule.api.execution.ExecutionTemplate;
import org.mule.api.lifecycle.CreateException;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.lifecycle.LifecycleState;
import org.mule.api.retry.RetryContext;
import org.mule.api.transport.Connector;
import org.mule.construct.Flow;
import org.mule.processor.strategy.SynchronousProcessingStrategy;
import org.mule.transport.AbstractConnector;
import org.mule.transport.AbstractPollingMessageReceiver;
import org.mule.util.lock.LockFactory;
//import org.mule.util.IOUtils;

public class MyCustomFTPMessageReceiver extends AbstractPollingMessageReceiver {
	private static final int FTP_LIST_PAGE_SIZE = 25;
	protected final FtpConnector connector;
	 protected final FilenameFilter filenameFilter;
	  protected final Set<String> scheduledFiles = Collections.synchronizedSet(new HashSet());
	  protected final Set<String> currentFiles = Collections.synchronizedSet(new HashSet());
	 private boolean poolOnPrimaryInstanceOnly;
	

	public MyCustomFTPMessageReceiver(Connector connector,
			FlowConstruct service, InboundEndpoint endpoint, long frequency,
			String moveToDir, String moveToPattern, long fileAge)
			throws CreateException {
		super(connector, service, endpoint);
		// TODO Auto-generated constructor stub
		this.setFrequency(frequency);

        this.connector = (FtpConnector) connector;

        if (endpoint.getFilter() instanceof FilenameFilter)
        {
            this.filenameFilter = (FilenameFilter) endpoint.getFilter();
        }
        else
        {
            this.filenameFilter = null;
        }
	}
	@Override
    protected void doInitialise() throws InitialisationException
    {
        boolean synchronousProcessing = false;
        if (getFlowConstruct() instanceof Flow)
        {
            synchronousProcessing = ((Flow)getFlowConstruct()).getProcessingStrategy() instanceof SynchronousProcessingStrategy;
        }
        this.poolOnPrimaryInstanceOnly = Boolean.valueOf(System.getProperty("mule.transport.ftp.singlepollinstance","false")) || (!synchronousProcessing && ((AbstractConnector)getConnector()).getReceiverThreadingProfile().isDoThreading());
    }

	
	@Override
	public void poll() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("In poll method");
		FTPClient client = connector.getFtp(endpoint.getEndpointURI());
		FTPFile[] files = listFiles(client) ;
		System.out.println("No of files received" + files.length);		
		if(files.length > 0  && null != files){
	    List<byte[]> contents = extarctFiles(files);
	    System.out.println("Contents of msg" + contents);		
		MuleMessage msg = createMuleMessage(contents,"UTF-8");
		//postProcess(client, files);
		this.routeMessage(msg);
		
		}
 
	}
	@Override
    protected boolean pollOnPrimaryInstanceOnly()
    {
        return poolOnPrimaryInstanceOnly;
    }
	private List<byte[]> extarctFiles(FTPFile[] files) throws Exception {
		List<byte[]> ret = new ArrayList<byte[]>();
		//try {
			FTPClient client = null;
			for (int i = 0; i < files.length; i++) {
				client = connector.getFtp(endpoint.getEndpointURI());
				System.out.println("No of times loop executed:" + i + " file name:" + files[i]);
				FTPFile file = files[i];
				InputStream is = client.retrieveFileStream(file.getName());
				byte[] b = IOUtils.toByteArray(is);
				ret.add(b);
				postProcess(client, file);
				System.out.println("In for loop array:"+ ret);

			}
		//} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		//} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		//}
		return ret;
	}
	protected FTPFile[] listFiles(FTPClient client)
		    throws Exception
		  {
		   
		    try
		    {
		     FTPListParseEngine engine = client.initiateListParsing();
		      FTPFile[] files = null;
		      List<FTPFile> v = new ArrayList();
		      while (engine.hasNext())
		      {
		        if (getLifecycleState().isStopping()) {
		          break;
		        }
		        files = engine.getNext(25);
		        if ((files == null) || (files.length == 0)) {
		          return files;
		        }
		        for (FTPFile file : files) {
		          if (file.isFile()) {
		             v.add(file);
		          }
		        }
		      }
		      return (FTPFile[])v.toArray(new FTPFile[v.size()]);
		    }
		    finally
		    {
		      if (client != null) {
		        this.connector.releaseFtp(this.endpoint.getEndpointURI(), client);
		      }
		    }
		  }
	protected void postProcess(FTPClient client, FTPFile file)
		    throws Exception
		  {
		if (!client.deleteFile(file.getName())) {
			      throw new IOException("Failed to delete file {0}. Ftp error: {1}");
			   
		}
		   
		  }
	@Override
    protected void doConnect() throws Exception
    {
        // no op
    }

    @Override
    public RetryContext validateConnection(RetryContext retryContext)
    {
        FTPClient client = null;
        try
        {
            client = connector.getFtp(endpoint.getEndpointURI());
            client.sendNoOp();
            client.logout();
            client.disconnect();

            retryContext.setOk();
        }
        catch (Exception ex)
        {
            retryContext.setFailed(ex);
        }
        finally
        {
            try
            {
                if (client != null)
                {
                    connector.releaseFtp(endpoint.getEndpointURI(), client);
                }
            }
            catch (Exception e)
            {
                if (logger.isDebugEnabled())
                {
                    logger.debug("Failed to release ftp client " + client, e);
                }
            }
        }

        return retryContext;
    }
        
    @Override
    protected void doDisconnect() throws Exception
    {
        // no op
    }

    @Override
    protected void doDispose()
    {
        // template method
    }
    public void release()
    {
        // no op
    }

}
