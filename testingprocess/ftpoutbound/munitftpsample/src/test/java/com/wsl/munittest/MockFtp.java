package com.wsl.munittest;

import org.mule.munit.runner.functional.FunctionalMunitSuite;

public class MockFtp extends FunctionalMunitSuite 
	{
		public void ftpmock()
		{
			
			whenMessageProcessor("outbound-endpoint").ofNamespace("ftp").thenReturnSameEvent();
		}
	}
