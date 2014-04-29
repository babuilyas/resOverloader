package com.misopes.resoverloader;

import java.io.File;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.res.XModuleResources;
import android.os.Environment;
import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;

public class engine implements IXposedHookZygoteInit,
		IXposedHookInitPackageResources {

	private static String MODULE_PATH = null;

	@Override
	public void initZygote(StartupParam startupParam) throws Throwable {
		MODULE_PATH = startupParam.modulePath;

		XposedBridge.log("Im xygot " + startupParam.modulePath);

	}

	@Override
	public void handleInitPackageResources(InitPackageResourcesParam resparam)
			throws Throwable {
		
		File appFolder = new File(Environment.getExternalStorageDirectory()
				.getPath()
				+ "/resoverloader/"
				+ resparam.packageName);

		// replacements if exists
		if (!appFolder.exists())
			return;
		else
			XposedBridge.log("resOverloader found app " + resparam.packageName);			
		
		File stringsFile = new File( appFolder.getPath() + "/strings.xml");		
			
		if (stringsFile.exists()) {
			
			  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		      DocumentBuilder db = dbf.newDocumentBuilder();
		      Document document = db.parse(stringsFile);	      
		      
		     NodeList a =  document.getElementsByTagName("string");
		     
		     for(int i = 0; i<a.getLength();i++)
		     {
		    	Node b = a.item(i);
		    	
		    	resparam.res.setReplacement(
		    			resparam.packageName, 
		    			"string", 
		    			b.getAttributes().item(0).getNodeValue(), 
		    			b.getTextContent());
		    	
		    	//System.out.println(b.getTextContent());
		    	//System.out.println(b.getAttributes().item(0).getNodeValue());
		     }
		}
		else
			XposedBridge.log("strings.xml file not found under " + resparam.packageName);
	}

}
