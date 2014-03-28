package com.whiteskylabs.muledeployment.util;

public class Constants {

	// MMC server property keys
	public static final String MMC_USERNAME = "username";
	public static final String MMC_PASSWORD = "password";
	public static final String MMC_HOST = "host";
	public static final String MMC_PORT = "muleServerPort";
	public static final String MMC_REPOS_LOCATION = "mmcRepositoryLocation";
	public static final String MMC_SERVER_ID = "muleServerID";
	public static final String MMC_DEPLOYMENTS_LOCATION = "deploymentsLocation";
	public static final String MMC_CLEAR_CACHE_FLAG = "mmcClearCacheFlag";
	public static final String MMC_DELETE_LOGS_FILES_FLAG = "deleteLogFilesFlag";
	public static final String MMC_TMP_DIR = "muleMMCTmpDirectory";
	public static final String MMC_LOGS_DIR = "muleMMCLogsDirectory";
	public static final String OPERATING_SYSTEM = "operatingSystem";

	public static final String MMC_CLUSTER_ID = "muleMMCClusterID";

	public static final String MMC_MULE_APPS_SRC_DIR = "muleApplicationsSourceDirectory";

	// Mule standalone server property keys
	// Local and Remote Standalone Server flags
	public static final String IS_LOCAL_STANDALONE = "localMuleStandaloneServer";
	public static final String IS_REMOTE_STANDALONE = "remoteMuleStandaloneServer";

	// Local Standalone details
	public static final String STANDALONE_APPS_DIR = "local.muleStandaloneAppsDirectory";
	public static final String STANDALONE_TMP_DIR = "local.muleStandaloneTmpDirectory";
	public static final String STANDALONE_LOGS_DIR = "local.muleStandaloneLogsDirectory";

	// Remote Standalone details
	public static final String REMOTE_STANDALONE_APPS_DIR = "remote.muleStandaloneAppsDirectory";
	public static final String REMOTE_STANDALONE_TMP_DIR = "remote.muleStandaloneTmpDirectory";
	public static final String REMOTE_STANDALONE_LOGS_DIR = "remote.muleStandaloneLogsDirectory";

	// Remote(where standalone running) FTP server details
	public static final String REMOTE_FTP_SERVER_HOSTNAME = "remote.FTPServer.hostname";
	public static final String REMOTE_FTP_SERVER_PORT = "remote.FTPServer.port";
	public static final String REMOTE_FTP_SERVER_USERNAME = "remote.FTPServer.username";
	public static final String REMOTE_FTP_SERVER_PASSWORD = "remote.FTPServer.password";

	public static final String STANDALONE_CLEAR_CACHE_FLAG = "standaloneClearCacheFlag";
	public static final String STANDALONE_DELETE_LOGS_FILE_FLAG = "deleteLogFilesFlag";
	public static final String STANDALONE_MULE_APPS_SRC_DIR = "muleApplicationsSourceDirectory";
	public static final String STANDALONE_ANCHOR_FILE_SUFFIX = "deploymentAnchorFileNameSuffix";
	public static final String MULE_APP_LOG_FILE_PREFIX = "muleAppLogFileNamePrefix";
	public static final String ZERO_DOWN_TIME_FLAG= "zeroDownTimeFlag";
	
	// Get deployments response JSON object keys
	public static final String MULE_APP_LIST_DATA = "data";
	public static final String MULE_APP_LIST_HREF = "href";
	public static final String MULE_TOTAL_APPS = "total";
	public static final String MULE_APP_LIST_NAME = "name";

	public static final String MULE_APP_FILE_EXT = ".zip";
}
