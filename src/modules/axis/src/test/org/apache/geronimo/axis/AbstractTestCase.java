/*
 * Copyright 2001-2004 The Apache Software Foundation.
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
 */
 
package org.apache.geronimo.axis;

import java.io.File;

import junit.framework.TestCase;

/**
 * Abstract base class for test cases.
 */
public abstract class AbstractTestCase
	extends TestCase 
{
	protected String testDir = "src/test/";
	protected String sampleDir = "src/samples/";
	protected String outDir = "target/generated/samples/";
	protected String tempDir = "target/generated/temp";
	/** 
	 * Basedir for all file I/O. Important when running tests from
	 * the reactor.
	 */
	public String basedir = System.getProperty("basedir");
    
	/**
	 * Constructor.
	 */
	public AbstractTestCase(String testName)
	{
		super(testName);
		if(basedir == null){
			basedir = new File(".").getAbsolutePath();
			if(!(basedir.endsWith("axis")
				||basedir.endsWith("axis\\")
				||basedir.endsWith("axis/"))){
				basedir = new File("./modules/axis").getAbsolutePath();
			}
		}
		testDir = new File(basedir,testDir).getAbsolutePath();
		sampleDir = new File(basedir,sampleDir).getAbsolutePath();
		outDir = new File(basedir,outDir).getAbsolutePath();
		tempDir = new File(basedir,tempDir).getAbsolutePath();
	}
    
	/**
	 * Get test input file.
	 *
	 * @param path Path to test input file.
	 */
	public String getTestFile(String path)
	{
		return new File(basedir,path).getAbsolutePath();
	}
}

