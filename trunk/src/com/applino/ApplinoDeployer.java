//========================================================================
//$Id: HotDeployer.java 1113 2006-10-20 11:40:15Z janb $
//Copyright 2006 Mort Bay Consulting Pty. Ltd.
//------------------------------------------------------------------------
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//http://www.apache.org/licenses/LICENSE-2.0
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//========================================================================

package com.applino;

import org.mortbay.component.AbstractLifeCycle;
import org.mortbay.jetty.deployer.ConfigurationManager;
import org.mortbay.jetty.handler.ContextHandler;
import org.mortbay.jetty.handler.ContextHandlerCollection;
import org.mortbay.log.Log;
import org.mortbay.resource.Resource;
import org.mortbay.util.Scanner;
import org.mortbay.xml.XmlConfiguration;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

/**
 * The ApplinoDeployer class is the most involved modification to the servlet
 * container in that it scans the applini sub-directory for Applini (files
 * with a .aar sufix: i.e. Application Archive) and creates a new
 * ApplinoContext for each Applino which is found. Expanded .aar archives
 * are also supported. When a new ApplinoContext is instantiated or destroyed
 * (by deleting the Applino file) the corresponding entry is removed from the
 * task tray icon.
 */
public class ApplinoDeployer extends AbstractLifeCycle
{
    public final static String NAME="ConfiguredDeployer";
    private int _scanInterval=10;
    private Scanner _scanner;
    private ScannerListener _scannerListener;
    private Resource _configurationDir;
    private Map _currentDeployments=new HashMap();
    private ContextHandlerCollection _contexts;
    private ConfigurationManager _configMgr;
    private PopupMenu menu = null;

    /* ------------------------------------------------------------ */
    protected class ScannerListener implements Scanner.DiscreteListener
    {
        /**
         * Handle a new deployment
         */
        public void fileAdded(String filename) throws Exception
        {
            deploy(filename);
        }

        /**
         * Handle a change to an existing deployment. Undeploy then redeploy.
         *
         */
        public void fileChanged(String filename) throws Exception
        {
            redeploy(filename);
        }

        /**
         * Handle an undeploy.
         *
         */
        public void fileRemoved(String filename) throws Exception
        {
            undeploy(filename);
        }
    }

    /**
     * Constructor
     *
     * @throws Exception
     */
    public ApplinoDeployer() throws Exception
    {
        // set up the default scan location to be $jetty.home/applini
        String home=System.getProperty("jetty.home");
        if (home==null)
            home=".";
        Log.debug("applino.home="+home);
        setConfigurationDir(Resource.newResource(home).addPath("applini"));
        Log.debug("hot deploy dir="+_configurationDir.getFile().getCanonicalPath());
        _scanner=new Scanner();
    }

    /**
     * @return the ContextHandlerColletion to which to deploy the contexts
     */
    public ContextHandlerCollection getContexts()
    {
        return _contexts;
    }

    /* ------------------------------------------------------------ */
    /**
     * Associate with a {@link ContextHandlerCollection}.
     *
     * @param contexts
     *            the ContextHandlerColletion to which to deploy the contexts
     */
    public void setContexts(ContextHandlerCollection contexts)
    {
        if (isStarted()||isStarting())
            throw new IllegalStateException("Cannot set Contexts after deployer start");
        _contexts=contexts;
    }

    /* ------------------------------------------------------------ */
    /**
     * @param seconds
     *            The period in second between scans for changed configuration
     *            files. A zero or negative interval disables hot deployment
     */
    public void setScanInterval(int seconds)
    {
        if (isStarted()||isStarting())
            throw new IllegalStateException("Cannot change scan interval after deployer start");
        _scanInterval=seconds;
    }

    /* ------------------------------------------------------------ */
    public int getScanInterval()
    {
        return _scanInterval;
    }

    /* ------------------------------------------------------------ */
    /**
     * @param dir
     * @throws Exception
     */
    public void setConfigurationDir(String dir) throws Exception
    {
        setConfigurationDir(Resource.newResource(dir));
    }

    /* ------------------------------------------------------------ */
    /**
     * @param file
     * @throws Exception
     */
    public void setConfigurationDir(File file) throws Exception
    {
        setConfigurationDir(Resource.newResource(file.toURL()));
    }

    /* ------------------------------------------------------------ */
    /**
     * @param resource
     */
    public void setConfigurationDir(Resource resource)
    {
        if (isStarted()||isStarting())
            throw new IllegalStateException("Cannot change hot deploy dir after deployer start");
        _configurationDir=resource;
    }

    /* ------------------------------------------------------------ */
    /**
     * @param directory
     */
    public void setDirectory(String directory) throws Exception
    {
		setConfigurationDir(directory);
    }

    /* ------------------------------------------------------------ */
    /**
     * @return Directory name
     */
    public String getDirectory()
    {
        return getConfigurationDir().getName();
    }

    /* ------------------------------------------------------------ */
    /**
     * @return Resource
     */
    public Resource getConfigurationDir()
    {
        return _configurationDir;
    }

    /* ------------------------------------------------------------ */
    /**
     * @param configMgr
     */
    public void setConfigurationManager(ConfigurationManager configMgr)
    {
        _configMgr=configMgr;
    }

    /* ------------------------------------------------------------ */
    /**
     * @return ConfigurationManager
     */
    public ConfigurationManager getConfigurationManager()
    {
        return _configMgr;
    }

    public void setPopupMenu(PopupMenu menu)
    {
        this.menu = menu;
    }

    /* ------------------------------------------------------------ */
    private void deploy(String filename) throws Exception
    {
        ApplinoContext context = createApplinoContext(filename);
        Log.info("Deploy " + filename + " -> " + context);
        _contexts.addHandler(context);
        _currentDeployments.put(filename, context);

        if (menu != null)
        {
            String name = context.getContextPath().substring(1);
            MenuItem menuItem = new MenuItem("Start " + name);
            context.setMenuItem(menuItem);
            menuItem.addActionListener(new StartApplinoListner(context));
            menu.insert(menuItem, 0);
        }

        /*
        if (_contexts.isStarted())
            context.start();
        */
    }

    /* ------------------------------------------------------------ */
    private void undeploy(String filename) throws Exception
    {
        ContextHandler context=(ContextHandler)_currentDeployments.get(filename);
        Log.info("Undeploy "+filename+" -> "+context);
        if (context==null)
            return;
        context.stop();
        _contexts.removeHandler(context);
        _currentDeployments.remove(filename);
    }

    /* ------------------------------------------------------------ */
    private void redeploy(String filename) throws Exception
    {
        undeploy(filename);
        deploy(filename);
    }

    /* ------------------------------------------------------------ */
    /**
     * Start the hot deployer looking for webapps to deploy/undeploy
     *
     * @see org.mortbay.component.AbstractLifeCycle#doStart()
     */
    protected void doStart() throws Exception
    {
        if (_configurationDir==null)
            throw new IllegalStateException("No configuraition dir specified");

        if (_contexts==null)
            throw new IllegalStateException("No context handler collection specified for deployer");

        _scanner.setScanDir(_configurationDir.getFile());
        _scanner.setScanInterval(getScanInterval());
        // Accept changes only in files that could be the equivalent of
        // jetty-web.xml files.
        // That is, files that configure a single webapp.
        _scanner.setFilenameFilter(new FilenameFilter()
        {
            public boolean accept(File dir, String name)
            {
                try
                {
                    if (name.endsWith(".aar")&&dir.equals(getConfigurationDir().getFile()))
                        return true;
                    return false;
                }
                catch (IOException e)
                {
                    Log.warn(e);
                    return false;
                }
            }
        });
        _scannerListener=new ScannerListener();
        _scanner.addListener(_scannerListener);
        _scanner.scan();
        _scanner.start();
        _contexts.getServer().getContainer().addBean(_scanner);
    }

    /* ------------------------------------------------------------ */
    /**
     * Stop the hot deployer.
     *
     * @see org.mortbay.component.AbstractLifeCycle#doStop()
     */
    protected void doStop() throws Exception
    {
        _scanner.removeListener(_scannerListener);
        _scanner.stop();
    }

    /* ------------------------------------------------------------ */
    /**
     * Create a WebAppContext for the webapp being hot deployed, then apply the
     * xml config file to it to configure it.
     *
     * @param filename
     *            the config file found in the hot deploy directory
     * @return
     * @throws Exception
     */
    private ContextHandler createXmlContext(String filename) throws Exception
    {
        // The config file can call any method on WebAppContext to configure
        // the webapp being deployed.
        File hotDeployXmlFile=new File(filename);
        if (!hotDeployXmlFile.exists())
            return null;

        XmlConfiguration xmlConfiguration=new XmlConfiguration(hotDeployXmlFile.toURL());
        HashMap properties = new HashMap();
        properties.put("Server", _contexts.getServer());
        if (_configMgr!=null)
            properties.putAll(_configMgr.getProperties());

        xmlConfiguration.setProperties(properties);
        ContextHandler context=(ContextHandler)xmlConfiguration.configure();
        return context;
    }

    private ApplinoContext createApplinoContext(String filename) throws Exception
    {
        // The config file can call any method on WebAppContext to configure
        // the webapp being deployed.
        File warFile = new File(filename);
        if (!warFile.exists())
            return null;

        String contextPath = warFile.getName();
        int pos = contextPath.lastIndexOf(".");
        contextPath = "/" + contextPath.substring(0, pos);
        ApplinoContext wah = new ApplinoContext();
        wah.setContextPath(contextPath);
        wah.setExtractWAR(false);
        wah.setWar(warFile.toURI().toString());

        return wah;
    }

    public void startApps() throws Exception
    {
        Iterator i = _currentDeployments.keySet().iterator();
        while(i.hasNext())
        {
            String appName = (String)i.next();
            ContextHandler context = (ContextHandler)_currentDeployments.get(appName);
            context.start();
        }
    }

    public void stopApps() throws Exception
    {
        Iterator i = _currentDeployments.keySet().iterator();
        while(i.hasNext())
        {
            String appName = (String)i.next();
            ContextHandler context = (ContextHandler)_currentDeployments.get(appName);
            context.stop();
        }
    }

    private static class StartApplinoListner implements ActionListener
    {
        private ApplinoContext  context;

        public StartApplinoListner(ApplinoContext context)
        {
            this.context = context;
        }

        public void actionPerformed(ActionEvent e)
        {
            if (!context.isRunning())
                context.startApplino();
            else
                context.stopApplino();
        }
    };
}
