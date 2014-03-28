package com.whiteskylabs.muledeployment.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.whiteskylabs.muledeployment.standalone.StandaloneDeploymentFacade;

public class DeploymentUtil {

	static Logger log = Logger.getLogger(StandaloneDeploymentFacade.class
			.getName());

	public static Properties prop;

	/**
	 * Loac properties file to get
	 * 
	 * @param propertiesFilePath
	 *            Absolute file path of properties file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void loadProperties(String propertiesFilePath)
			throws FileNotFoundException, IOException {
		prop = new Properties();
		prop.load(new FileInputStream(propertiesFilePath));
	}

	/**
	 * This is used to get value from property values
	 * 
	 * @param key
	 *            is property key
	 * @return It returns Value of the key
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws Exception
	 */
	public static String getPropertyvalue(String key)
			throws FileNotFoundException, IOException, NullPointerException {

		String value = new String();
		value = prop.getProperty(key);
		if (value == null) {
			throw new NullPointerException(
					"'"
							+ key
							+ "' key is not available in DeploymentConfiguration properties file");
		}
		return value;

	}

	/**
	 * Validate the source directory and get list Mule application zip files
	 * 
	 * @param sourceAppsDir
	 *            Location of source mule applications
	 * @param ext
	 *            extension of the mule application file ".zip"
	 * @return
	 * @throws Exception
	 */
	public static String[] validateAndGetListOfFileNames(String sourceAppsDir,
			String ext) throws Exception {

		log.debug("sourceAppsDir : " + sourceAppsDir);

		File dir = new File(sourceAppsDir);

		// Check is directory or not.
		if (dir.isDirectory() == false) {
			throw new Exception("Directory does not exists : " + sourceAppsDir);
		}

		// Make a filter with ".zip"
		GenericExtFilter filter = new GenericExtFilter(ext);
		String[] list = dir.list(filter);

		// If no zip files available throw an excception.
		if (list.length == 0) {
			log.error("No .zip files are available in '" + sourceAppsDir + "'");
			throw new Exception("No .zip files are available in '"
					+ sourceAppsDir + "'");
		}

		return list;
	}

	// Generic extension filter
	public static class GenericExtFilter implements FilenameFilter {

		private String ext;

		public GenericExtFilter(String ext) {
			this.ext = ext;
		}

		public boolean accept(File dir, String name) {
			return (name.endsWith(ext));
		}
	}

	/**
	 * Strip the extension of the file name
	 * 
	 * @param fileName
	 *            File name
	 * @return
	 */
	public static String stripExtension(String fileName) {
		// Handle null case specially.

		if (fileName == null)
			return null;

		// Get position of last '.'.

		int pos = fileName.lastIndexOf(".");

		// If there wasn't any '.' just return the string as is.

		if (pos == -1)
			return fileName;

		// Otherwise return the string, up to the dot.

		return fileName.substring(0, pos);
	}

	/**
	 * Get the current time to set as a version of the application.
	 * 
	 * @return
	 */
	public static String getApplicationVerion() {

		DateFormat df = new SimpleDateFormat("yyyyMMdd-HH:mm:ss");
		Date now = Calendar.getInstance().getTime();
		log.debug(df.format(now).toString());
		return df.format(now).toString();
	}

	/**
	 * Generates encoded username and password.
	 * 
	 * @param user
	 * @param pwd
	 * @return It returns encoded username and password.
	 * @throws Exception
	 */
	public static String getEncodedUsernamePwd(String user, String pwd)
			throws Exception {

		String authStr = user + ":" + pwd;
		String authEncoded = Base64.encodeBytes(authStr.getBytes());
		return authEncoded;

	}

	/**
	 * convert InputStream to String.
	 * 
	 * @param is
	 *            InputStream object generic response from REST api call.
	 * @return
	 */
	public static String getStringFromInputStream(InputStream is)
			throws Exception {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			throw new Exception(e.getMessage());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}

	/**
	 * Parse Json to Map Object
	 * 
	 * @param json
	 *            json object
	 * @param out
	 *            Map object
	 * @return map object
	 * @throws JSONException
	 */
	public static Map<String, Object> parseJSONToMap(JSONObject json,
			Map<String, Object> out) throws JSONException {
		Iterator<String> keys = json.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			Object val = null;
			try {
				JSONObject value = json.getJSONObject(key);
				parseJSONToMap(value, out);
			} catch (Exception e) {
				val = json.get(key);
			}

			if (val != null) {
				out.put(key, val);
			}
		}
		return out;
	}

	/**
	 * Delete the provided mule application folder in corresponding 'tmp'
	 * directory
	 * 
	 * @param muleAppName
	 *            Mule application name
	 * @param muleServerTmpDir
	 *            location of the 'tmp' directory
	 */
	public static void clearMuleAppCache(String muleAppName,
			String muleServerTmpDir) {

		// Strip the extension from the mule application file name
		String muleAppFileNameWithOutExtension = DeploymentUtil
				.stripExtension(muleAppName);

		File muleAppTmpdir = new File(muleServerTmpDir
				+ muleAppFileNameWithOutExtension);

		// if mule application exists in tmp directory.
		if (muleAppTmpdir.isDirectory()) {
			// if mule application directory is not deleted
			if (!muleAppTmpdir.delete()) {
				log.error("Can't remove " + muleAppTmpdir.getAbsolutePath());
			} else {
				log.info("'" + muleAppTmpdir.getAbsolutePath()
						+ "' cache deleted successfully");
			}
			// if mule application is not existed
		} else {
			log.info("No cache exists for '" + muleAppTmpdir.getAbsolutePath()
					+ "' application");
		}

	}

	/**
	 * Delete the provided mule application log files in 'logs' directory
	 * 
	 * @param muleAppName
	 * @param muleServerLogsDir
	 * @param logFilePrefix
	 */
	public static void deleteMuleAppLogFiles(String muleAppName,
			String muleServerLogsDir, String logFilePrefix) {

		// Get mule standalone logs directory location
		File muleStandaloneLogsDir = new File(muleServerLogsDir);

		final String muleAppLogFileName = new StringBuffer(logFilePrefix)
				.append(DeploymentUtil.stripExtension(muleAppName)).toString();
		// Delete the undeployed mule application log files
		for (File file : muleStandaloneLogsDir.listFiles()) {
			if (file.getName().startsWith(muleAppLogFileName)) {
				if (!file.delete()) {
					log.error("Can't remove " + file.getAbsolutePath());
				} else {
					log.info("'" + file.getAbsolutePath()
							+ "' deleted successfully");
				}
			}
		}

	}

	/**
	 * Get FTPClient logged in
	 * 
	 * @param deploymentUtil
	 * @return ftp object
	 * @throws Exception
	 */
	public static FTPClient getFTPClient() throws Exception {
		// Get Remote FTP server details
		String userName = DeploymentUtil
				.getPropertyvalue(Constants.REMOTE_FTP_SERVER_USERNAME);
		String password = DeploymentUtil
				.getPropertyvalue(Constants.REMOTE_FTP_SERVER_PASSWORD);
		String hostname = DeploymentUtil
				.getPropertyvalue(Constants.REMOTE_FTP_SERVER_HOSTNAME);
		int port = Integer.parseInt(DeploymentUtil
				.getPropertyvalue(Constants.REMOTE_FTP_SERVER_PORT));

		log.info("Logging into FTP server [host: " + hostname + ", port: "
				+ port + "]");
		FTPClient ftp = Configuration.getFTPClient(userName, password,
				hostname, port);
		return ftp;
	}

	/**
	 * Removes a non-empty directory by delete all its sub files and sub
	 * directories recursively. And finally remove the directory.
	 */
	public static void removeFTPServerDirectory(FTPClient ftpClient,
			String tmpMuleAppPath, String currentDir) throws IOException {
		String dirToList = tmpMuleAppPath;
		if (!currentDir.equals("")) {
			dirToList += "/" + currentDir;
		}

		FTPFile[] subFiles = ftpClient.listFiles(dirToList);

		if (subFiles != null && subFiles.length > 0) {
			for (FTPFile aFile : subFiles) {
				String currentFileName = aFile.getName();
				if (currentFileName.equals(".") || currentFileName.equals("..")) {
					// skip parent directory and the directory itself
					continue;
				}
				String filePath = tmpMuleAppPath + "/" + currentDir + "/"
						+ currentFileName;
				if (currentDir.equals("")) {
					filePath = tmpMuleAppPath + "/" + currentFileName;
				}

				if (aFile.isDirectory()) {
					// remove the sub directory
					removeFTPServerDirectory(ftpClient, dirToList, currentFileName);
				} else {
					// delete the file
					boolean deleted = ftpClient.deleteFile(filePath);
					if (deleted) {
						log.debug("DELETED the file: " + filePath);
					} else {
						log.debug("CANNOT delete the file: " + filePath);
					}
				}
			}

			// finally, remove the directory itself
			boolean removed = ftpClient.removeDirectory(dirToList);
			if (removed) {
				log.debug("REMOVED the directory: " + dirToList);
			} else {
				log.debug("CANNOT remove the directory: " + dirToList);
			}
		}
	}

	public static void deleteLocalDir(File file) throws IOException {

		if (file.isDirectory()) {

			// directory is empty, then delete it
			if (file.list().length == 0) {

				file.delete();
				log.debug("Directory is deleted : " + file.getAbsolutePath());

			} else {

				// list all the directory contents
				String files[] = file.list();

				for (String temp : files) {
					// construct the file structure
					File fileDelete = new File(file, temp);

					// recursive delete
					deleteLocalDir(fileDelete);
				}

				// check the directory again, if empty then delete it
				if (file.list().length == 0) {
					file.delete();
					log.debug("Directory is deleted : "
							+ file.getAbsolutePath());
				}
			}

		} else {
			// if file, then delete it
			file.delete();
			log.debug("File is deleted : " + file.getAbsolutePath());
		}
	}

}
