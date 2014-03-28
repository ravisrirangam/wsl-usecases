package com.whiteskylabs.muledeployment.mmc;

import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.whiteskylabs.muledeployment.util.Constants;
import com.whiteskylabs.muledeployment.util.DeploymentUtil;

public class MMCDeploymentFacade {

    //property variables.
	private String username;
	private String password;
	private String hostname;
	private String port;
	private String version;
	private String mmcRepositoryLocation;
	private String muleServerID;
	private String muleClusterID;
	private String deploymentsLocation;
	private String operatingSystem;
	
	static Logger log = Logger.getLogger(MMCDeploymentFacade.class.getName());
	
	public MMCDeploymentFacade() throws FileNotFoundException, IOException {
		// Set server details to current object. 
		this.setUsername(DeploymentUtil.getPropertyvalue(Constants.MMC_USERNAME));
		this.setPassword(DeploymentUtil.getPropertyvalue(Constants.MMC_PASSWORD));
		this.setHostname(DeploymentUtil.getPropertyvalue(Constants.MMC_HOST));
		this.setPort(DeploymentUtil.getPropertyvalue(Constants.MMC_PORT));
		this.setVersion(DeploymentUtil.getApplicationVerion());
		this.setMmcRepositoryLocation(DeploymentUtil.getPropertyvalue(Constants.MMC_REPOS_LOCATION));
		this.setMuleServerID(DeploymentUtil.getPropertyvalue(Constants.MMC_SERVER_ID));
		this.setMuleClusterID(DeploymentUtil.getPropertyvalue(Constants.MMC_CLUSTER_ID));
		this.setDeploymentsLocation(DeploymentUtil.getPropertyvalue(Constants.MMC_DEPLOYMENTS_LOCATION));
		this.setOperatingSystem(DeploymentUtil.getPropertyvalue(Constants.OPERATING_SYSTEM));
		
	}
	
	/** This method is used to deploy mule application to a target mule ESB server.
	 *  This method runs following 3 curl commands.
	 *  	1) Upload mule application to mule server repository.
	 *  	2) Create deployment with uploaded application and keeps in UNDEPLOYED status.
	 *  	3) Deploy the created deployment.
	 * @throws Exception
	 */
	public void deploy(String appFilePath, String muleAppName) throws Exception{
		
		
		log.info("Deployment running on '"+this.getOperatingSystem()+"' Operating system");
		
		// Upload mule application to mule server repository.
		String applicationVersionID = uploadMuleApp(appFilePath, muleAppName);
		
		// Create deployment with uploaded application and keeps in UNDEPLOYED status
		String deploymentRef = createDeployment(applicationVersionID, muleAppName);
	
		// Deploy the created deployment.
		deployMuleApp(deploymentRef, muleAppName);
		
	}
	
	/** This method is used to upload Mule application(zip file) to Mule repository.  
	 * @return It returns a application version ID which will be used to create the deployment.
	 * @throws Exception
	 */
	public String uploadMuleApp(String appFilePath, String muleAppName) throws Exception{
		
		log.info("================================================================================");
		log.info("Uploading '"+muleAppName+"' application to Mule server repository");
		log.info("================================================================================");
		
		String repositoryURL = "http://"+this.getHostname()+":"+this.getPort()+this.mmcRepositoryLocation;
		String reposcommand;
		String reposOutputString;
	    String applicationVersionID;
		// Prepare the curl command based on operation system where we are executing this application.
		if(this.operatingSystem.equals(Constants.OPERATING_SYSTEM)){
			reposcommand = "curl --basic -u "+this.getUsername()+":"+this.getPassword()
    				+ " -F file=@"+appFilePath
    				+ " -F name="+muleAppName+""
    				+ " -F version="+this.getVersion()+""
    				+ " --header \"Content-Type: multipart/form-data\" "
    				+ repositoryURL;
		}else{
			reposcommand = "curl --basic -u "+this.getUsername()+":"+this.getPassword()
    				+ " -F file=@"+appFilePath
    				+ " -F name="+muleAppName+""
    				+ " -F version="+this.getVersion()+""
    				+ " --header 'Content-Type: multipart/form-data' "
    				+ repositoryURL;
		}
	    
	    log.debug("Upload mule app to repository command : "+reposcommand);
	    	
    	// upload mule application to mule server repository.
	    Process reposCurlProc = Runtime.getRuntime().exec(reposcommand);
	    DataInputStream reposCurlIn = new DataInputStream(reposCurlProc.getInputStream());
        if((reposOutputString = reposCurlIn.readLine()) != null) {
        	
            log.debug("Uploaded application details : "+reposOutputString);
            JSONObject uploadedAppJsonResponseObject = new JSONObject(reposOutputString);
            
            // Get uploaded application version id from JSON response.
            applicationVersionID = uploadedAppJsonResponseObject.get("versionId").toString();
            log.debug("Uploaded Application version ID : "+applicationVersionID);
        }else{
        	throw new Exception("Mule application has not been uploaded. Please check respective mule server repository");
        }
        log.info("================================================================================");
		log.info("'"+muleAppName+"' application has been uploaded successfully");
		log.info("================================================================================");    
		return applicationVersionID;
	}

	/** This method is used to create the deployment 
	 *  with uploaded mule application in mule repository
	 *  and keeps in UNDEPLOYED status.
	 * @param applicationVersionID is version ID of the uploaded mule applicaiton.
	 * @return
	 * @throws Exception
	 */
	public String createDeployment(String applicationVersionID, String muleAppName)
					throws Exception{

		log.info("================================================================================");
		log.info("Creating deployment of '"+muleAppName+"' in Mule server");
		log.info("================================================================================");

		String deploymentRef = new String();
		String deploymentsURL = "http://"+this.getHostname()+":"+this.getPort()+this.getDeploymentsLocation();
		
		// Invoke Create Deployment REST API
		HttpURLConnection httpcon = (HttpURLConnection) ((new URL(deploymentsURL).openConnection()));
		httpcon.setDoOutput(true);
		httpcon.setRequestProperty("Authorization", "Basic " + DeploymentUtil.getEncodedUsernamePwd(this.getUsername(),this.getPassword()));
		httpcon.setRequestProperty("Content-Type", "application/json");
		httpcon.setRequestMethod("POST");
		httpcon.connect();

		String[] serverID = {this.getMuleServerID()};
		String[] clusterID = {this.getMuleClusterID()};
		String[] applicationId = {applicationVersionID};
		JSONObject createDeployData = new JSONObject();
		
		// Set required values to call Create Deployment API 
		createDeployData.put("name", muleAppName);
		if (InvokeMMCDeployment.isDeploymentTargetMMC) {
			createDeployData.put("servers",serverID );
		}
		
		// If target server is Cluster
		else if (InvokeMMCDeployment.isDeploymentTargetCluster) {
			createDeployData.put("clusters",clusterID );
		}

		// If target server is not provided
		else {
			throw new Exception("Target server(MMC or Cluster) is not provided");
		}
		createDeployData.put("applications", applicationId);
		
		byte[] outputBytes = createDeployData.toString().getBytes("UTF-8");
		OutputStream os = httpcon.getOutputStream();
		os.write(outputBytes);
		os.close();
		

        InputStream createdeploymentCurlIn = (InputStream) httpcon.getInputStream();
        String createDeploymentoutputString = DeploymentUtil.getStringFromInputStream(createdeploymentCurlIn);
        log.debug("createDeploymentoutputString : "+createDeploymentoutputString);
        
        JSONObject createDeploymentJsonResObject = new JSONObject(createDeploymentoutputString);

        // Get Deployment reference from the 'Create Deployment' response            
        deploymentRef = createDeploymentJsonResObject.get("href").toString();
        log.debug("Deployement reference : "+deploymentRef);
        createdeploymentCurlIn.close();

        log.info("================================================================================");
		log.info("Deployment of '"+muleAppName+"' has been created successfully");
		log.info("================================================================================");
        
        return deploymentRef;
	}

	/** This method is used deploy the created deployment.
	 * @param deploymentRef is reference of the created Deployment
	 * @return
	 * @throws Exception
	 */
	public String deployMuleApp(String deploymentRef, String muleAppName) throws Exception{
		
		log.info("================================================================================");
		log.info("Deploying '"+muleAppName+"' in Mule server");
		log.info("================================================================================");
		
		String deployAckMsg = new String();

		// Invoke '/deploy' API to deploy the created Deployment 
		URL url = new URL(deploymentRef+"/deploy");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Authorization", "Basic " + DeploymentUtil.getEncodedUsernamePwd(this.getUsername(),this.getPassword()));
        
        InputStream deployInputstream = (InputStream) connection.getInputStream();
        // Get deployed mule application status
        deployAckMsg = DeploymentUtil.getStringFromInputStream(deployInputstream);
        log.info(deployAckMsg);
        deployInputstream.close();
        
        log.info("================================================================================");
		log.info("'"+muleAppName+"' deployed successfully");
		log.info("================================================================================");
		return deployAckMsg;
	}
	
	/** Get list of all deployments in Mule Server
	 * @param serverId Id of Server
	 * @return list of deployments in json format
	 * @throws Exception
	 */
	public String getListAllMuleAppsData(String getListAllURL) throws Exception{
		
		// Get list of all deployments on the server by calling the respective
		
		URL url = new URL(getListAllURL);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        connection.setRequestProperty("Authorization", "Basic " + DeploymentUtil.getEncodedUsernamePwd(this.getUsername(),this.getPassword()));
        
        InputStream deployInputstream = (InputStream) connection.getInputStream();
        String listAllMuleApps = DeploymentUtil.getStringFromInputStream(deployInputstream);
        log.debug("List of All deployments in Server : "+listAllMuleApps);
        deployInputstream.close();
		
		return listAllMuleApps;
	}
	
	/** Get Href of a specified Mule Application
	 * @param muleAppName Name of the Mule application
	 * @param appsListJsonMsg List of all deployments in json format
	 * @return href of the specified mule application
	 * @throws JSONException
	 */
	public String getMuleAppRef(String muleAppName, String appsListJsonMsg ) throws JSONException{
		
		JSONObject appsListJsonObj = new JSONObject(appsListJsonMsg);
		Map<String, Object> appsListMapObj = new HashMap<String, Object>();
		Map<String, Object> appsDetailsdataObj = new HashMap<String, Object>();
	    
		// Parse Json object to map object to get required values easily. 
		DeploymentUtil.parseJSONToMap(appsListJsonObj, appsListMapObj);
	    log.debug(appsListMapObj.get(Constants.MULE_TOTAL_APPS) +" deployements are existed " );
		
		for (Entry<String, Object> entry : appsListMapObj.entrySet()) {
			
			// Get "data" object
			if(entry.getKey().toString().equals(Constants.MULE_APP_LIST_DATA)){
				
				JSONArray jsonArray = (JSONArray) entry.getValue();
				String muleAppHref;
				for (int j=0;j<jsonArray.length();j++){
					
					JSONObject jsonDataObj =  (JSONObject) jsonArray.get(j);
					
					DeploymentUtil.parseJSONToMap(jsonDataObj, appsDetailsdataObj);
					log.debug("datamap : "+appsDetailsdataObj);
					
					/* 
					 * If mule application name matches to the specified one then
					 * get the "href" of that object.
					 * 
					 * */
					if(appsDetailsdataObj.get("name").equals(muleAppName)){
						muleAppHref = appsDetailsdataObj.get(Constants.MULE_APP_LIST_HREF).toString();
						log.debug(muleAppName+"'s href : "+muleAppHref);
						return muleAppHref;
					}
				}
			}
		}
		return null;
	}
	
	/** Undeploy the mule application 
	 * @param muleApphref href of a mule application
	 * @throws Exception
	 */
	public void undeploy(String muleApphref, String muleAppName) throws Exception{
		
		// Undeploy the specified muel application
		URL url = new URL( muleApphref+"/undeploy");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setRequestProperty("Authorization", "Basic " + DeploymentUtil.getEncodedUsernamePwd(this.getUsername(),this.getPassword()));
        
        InputStream deployInputstream = (InputStream) connection.getInputStream();
        // Status of the undeployment.
        String undeployedStatus = DeploymentUtil.getStringFromInputStream(deployInputstream);
        log.info("'"+muleAppName+"' : "+undeployedStatus);
        deployInputstream.close();
	}
	
	/** Delete the deployment form deployments of server
	 * @param muleApphref href of the deployment
	 * @throws Exception
	 */
	public void deleteDeployment(String muleApphref, String muleAppName) throws Exception{
		
		// Delete the specified deployment
		URL url = new URL(muleApphref);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");
        connection.setDoInput(true);
        connection.setRequestProperty("Authorization", "Basic " + DeploymentUtil.getEncodedUsernamePwd(this.getUsername(),this.getPassword()));
        
        InputStream deployInputstream = (InputStream) connection.getInputStream();
        
        // Status of the deleted deployment 
        String deletedDeployementStatus = DeploymentUtil.getStringFromInputStream(deployInputstream);
        log.info("'"+muleAppName+"' : "+deletedDeployementStatus);
        deployInputstream.close();
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getMmcRepositoryLocation() {
		return mmcRepositoryLocation;
	}

	public void setMmcRepositoryLocation(String mmcRepositoryLocation) {
		this.mmcRepositoryLocation = mmcRepositoryLocation;
	}

	public String getMuleServerID() {
		return muleServerID;
	}

	public void setMuleServerID(String muleServerID) {
		this.muleServerID = muleServerID;
	}

	public String getMuleClusterID() {
		return muleClusterID;
	}

	public void setMuleClusterID(String muleClusterID) {
		this.muleClusterID = muleClusterID;
	}

	public String getDeploymentsLocation() {
		return deploymentsLocation;
	}

	public void setDeploymentsLocation(String deploymentsLocation) {
		this.deploymentsLocation = deploymentsLocation;
	}

	public String getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}
	
	
}
