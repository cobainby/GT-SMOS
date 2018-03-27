package com.southgt.smosplat.organ;

import java.io.IOException;

import org.junit.Test;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

public class WarnMsg {

	
	public interface GSMModem extends Library {

		GSMModem INSTANCE = (GSMModem)

            Native.loadLibrary((Platform.isWindows() ? "GT.GSMModem" : "/smosplat/src/main/resources/"),

            		GSMModem.class);

   

        void printf(String format, Object... args);

    }

 
	@Test
    public void main() {
		String[] args={"nd","dd"};
    	GSMModem.INSTANCE.printf("Hello, World/n");

        for (int i=0;i < args.length;i++) {

        	GSMModem.INSTANCE.printf("Argument %d: %s/n", i, args[i]);

        }

    }
	public boolean GsmOpen(String com) throws IOException
    {
        return true;
    }
	
}
