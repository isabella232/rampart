package org.apache.axis2.tools.bean;

import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.WSDL2AxisServiceBuilder;
import org.apache.axis2.util.URLProcessor;
import org.apache.axis2.wsdl.codegen.CodeGenConfiguration;
import org.apache.axis2.wsdl.codegen.CodeGenerationEngine;
import org.apache.axis2.wsdl.util.CommandLineOption;
import org.apache.axis2.wsdl.util.CommandLineOptionConstants;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/*
* Copyright 2004,2005 The Apache Software Foundation.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
*
*/

public class CodegenBean {

    private String WSDLFileName = null;
    private String output = ".";
    private String packageName = URLProcessor.DEFAULT_PACKAGE;
    private String language = "java";

    private boolean asyncOnly = false;
    private boolean syncOnly = false;
    private boolean serverSide = true;
    private boolean testcase = true;
    private boolean generateServerXml = true;
    private boolean isServerXML;
    private boolean isGenerateAll;
    private boolean isTestCase;
    private String serviceName;
    private String portName;
    private String databindingName;

    public boolean isServerXML() {
        return isServerXML;
    }

    public void setServerXML(boolean serverXML) {
        isServerXML = serverXML;
    }

    public boolean isGenerateAll() {
        return isGenerateAll;
    }

    public void setGenerateAll(boolean generateAll) {
        isGenerateAll = generateAll;
    }

    public boolean isTestCase() {
        return isTestCase;
    }

    public void setTestCase(boolean testCase) {
        isTestCase = testCase;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public String getDatabindingName() {
        return databindingName;
    }

    public void setDatabindingName(String databindingName) {
        this.databindingName = databindingName;
    }

    /**
     * Maps a string containing the name of a language to a constant defined in CommandLineOptionConstants.LanguageNames
     *
     * @param UILangValue a string containg a language, e.g. "java", "cs", "cpp" or "vb"
     * @return a normalized string constant
     */
    private String mapLanguagesWithCombo(String UILangValue) {
        return UILangValue;
    }

    /**
     * Creates a list of parameters for the code generator based on the decisions made by the user on the OptionsPage
     * (page2). For each setting, there is a Command-Line option for the Axis2 code generator.
     *
     * @return a Map with keys from CommandLineOptionConstants with the values entered by the user on the Options Page.
     */
    public Map fillOptionMap() {
        Map optionMap = new HashMap();
        //WSDL file name
        optionMap.put(CommandLineOptionConstants.WSDL2JavaConstants.WSDL_LOCATION_URI_OPTION, new CommandLineOption(
                CommandLineOptionConstants.WSDL2JavaConstants.WSDL_LOCATION_URI_OPTION, getStringArray(WSDLFileName)));

        //Async only
        if (asyncOnly) {
            optionMap.put(CommandLineOptionConstants.WSDL2JavaConstants.CODEGEN_ASYNC_ONLY_OPTION, new CommandLineOption(
                    CommandLineOptionConstants.WSDL2JavaConstants.CODEGEN_ASYNC_ONLY_OPTION, new String[0]));
        }
        //sync only
        if (syncOnly) {
            optionMap.put(CommandLineOptionConstants.WSDL2JavaConstants.CODEGEN_SYNC_ONLY_OPTION, new CommandLineOption(
                    CommandLineOptionConstants.WSDL2JavaConstants.CODEGEN_SYNC_ONLY_OPTION, new String[0]));
        }
        //serverside
        if (serverSide) {
            optionMap.put(CommandLineOptionConstants.WSDL2JavaConstants.SERVER_SIDE_CODE_OPTION, new CommandLineOption(
                    CommandLineOptionConstants.WSDL2JavaConstants.SERVER_SIDE_CODE_OPTION, new String[0]));
            //server xml
            if (isServerXML) {
                optionMap.put(CommandLineOptionConstants.WSDL2JavaConstants.GENERATE_SERVICE_DESCRIPTION_OPTION, new CommandLineOption(
                        CommandLineOptionConstants.WSDL2JavaConstants.GENERATE_SERVICE_DESCRIPTION_OPTION, new String[0]));
            }
            if (isGenerateAll) {
                optionMap.put(CommandLineOptionConstants.WSDL2JavaConstants.GENERATE_ALL_OPTION, new CommandLineOption(
                        CommandLineOptionConstants.WSDL2JavaConstants.GENERATE_ALL_OPTION, new String[0]));
            }
        }
        //test case
        if (isTestCase) {
            optionMap.put(CommandLineOptionConstants.WSDL2JavaConstants.GENERATE_TEST_CASE_OPTION, new CommandLineOption(
                    CommandLineOptionConstants.WSDL2JavaConstants.GENERATE_TEST_CASE_OPTION, new String[0]));
        }
        //package name
        optionMap.put(CommandLineOptionConstants.WSDL2JavaConstants.PACKAGE_OPTION, new CommandLineOption(
                CommandLineOptionConstants.WSDL2JavaConstants.PACKAGE_OPTION, getStringArray(packageName)));
        //selected language
        optionMap.put(CommandLineOptionConstants.WSDL2JavaConstants.STUB_LANGUAGE_OPTION, new CommandLineOption(
                CommandLineOptionConstants.WSDL2JavaConstants.STUB_LANGUAGE_OPTION, getStringArray(mapLanguagesWithCombo(language))));
        //output location
        optionMap.put(CommandLineOptionConstants.WSDL2JavaConstants.OUTPUT_LOCATION_OPTION, new CommandLineOption(
                CommandLineOptionConstants.WSDL2JavaConstants.OUTPUT_LOCATION_OPTION, getStringArray(output)));

        //databinding
        optionMap.put(CommandLineOptionConstants.WSDL2JavaConstants.DATA_BINDING_TYPE_OPTION, new CommandLineOption(
                CommandLineOptionConstants.WSDL2JavaConstants.DATA_BINDING_TYPE_OPTION, getStringArray(databindingName)));

        //port name
        if (portName != null) {
            optionMap.put(CommandLineOptionConstants.WSDL2JavaConstants.PORT_NAME_OPTION, new CommandLineOption(
                    CommandLineOptionConstants.WSDL2JavaConstants.PORT_NAME_OPTION, getStringArray(portName)));
        }
        //service name
        if (serviceName != null) {
            optionMap.put(CommandLineOptionConstants.WSDL2JavaConstants.SERVICE_NAME_OPTION, new CommandLineOption(
                    CommandLineOptionConstants.WSDL2JavaConstants.SERVICE_NAME_OPTION, getStringArray(serviceName)));
        }
        return optionMap;

    }

    public String getBaseUri(String wsdlURI) {

        try {
            URL url;
            if (wsdlURI.indexOf("://") == -1) {
                url = new URL("file", "", wsdlURI);
            } else {
                url = new URL(wsdlURI);
            }


            String baseUri;
            if ("file".equals(url.getProtocol())) {
                baseUri = new File(url.getFile()).getParentFile().toURL().toExternalForm();
            } else {
                baseUri = url.toExternalForm().substring(0,
                        url.toExternalForm().lastIndexOf("/")
                );
            }


            return baseUri;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads the WSDL Object Model from the given location.
     *
     * @param wsdlURI the filesystem location (full path) of the WSDL file to read in.
     * @return the WSDLDescription object containing the WSDL Object Model of the given WSDL file
     * @throws IOException on errors reading the WSDL file
     */
    public AxisService getAxisService(String wsdlURI) throws Exception {

        URL url;
        if (wsdlURI.indexOf("://") == -1) {
            url = new URL("file", "", wsdlURI);
        } else {
            url = new URL(wsdlURI);
        }


        WSDL2AxisServiceBuilder builder =
                new WSDL2AxisServiceBuilder(url.openConnection().getInputStream());

        builder.setBaseUri(getBaseUri(wsdlURI));
        return builder.populateService();
    }

    /**
     * Converts a single String into a String Array
     *
     * @param value a single string
     * @return an array containing only one element
     */
    private String[] getStringArray(String value) {
        String[] values = new String[1];
        values[0] = value;
        return values;
    }

    public String getWSDLFileName() {
        return WSDLFileName;
    }

    public void setWSDLFileName(String WSDLFileName) {
        this.WSDLFileName = WSDLFileName;
    }

    public boolean isSyncOnly() {
        return syncOnly;
    }

    public void setSyncOnly(boolean syncOnly) {
        this.syncOnly = syncOnly;
    }

    public boolean isAsyncOnly() {
        return asyncOnly;
    }

    public void setAsyncOnly(boolean asyncOnly) {
        this.asyncOnly = asyncOnly;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public boolean isServerSide() {
        return serverSide;
    }

    public void setServerSide(boolean serverSide) {
        this.serverSide = serverSide;
    }

    public boolean isGenerateServerXml() {
        return generateServerXml;
    }

    public void setGenerateServerXml(boolean generateServerXml) {
        this.generateServerXml = generateServerXml;
    }

    public boolean isTestcase() {
        return testcase;
    }

    public void setTestcase(boolean testcase) {
        this.testcase = testcase;
    }

    public void generate() throws Exception {
        CodeGenConfiguration codegenConfig = new CodeGenConfiguration(getAxisService(WSDLFileName), fillOptionMap());
        //set the baseURI
        codegenConfig.setBaseURI(getBaseUri(WSDLFileName));
        new CodeGenerationEngine(codegenConfig).generate();
    }
}
