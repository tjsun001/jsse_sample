package jsse_sample;

/*
 *
 * Copyright (c) 1994, 2004, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 *
 * -Redistribution of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * Redistribution in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 *
 * Neither the name of Oracle nor the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN MICROSYSTEMS, INC. ("SUN") AND ITS LICENSORS SHALL
 * NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT
 * OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR
 * ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */



import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.rmi.ssl.SslRMIClientSocketFactory;

import clientserver.KerberosClient;
import jsse_sample.Hello;

public class HelloClient {

    private static final int PORT 			= 7777;
    private String clientKerberosTicket 	= null;

    public static void main(String args[]) {

            HelloClient client = new HelloClient();
            client.callRemoteMethod(null, null);
        }
    
    public String callRemoteMethod(String username, String password) {
    	try {
            // Make reference to SSL-based registry
            Registry registry = LocateRegistry.getRegistry(
                InetAddress.getLocalHost().getHostName(), PORT,
                new SslRMIClientSocketFactory());

            // "obj" is the identifier that we'll use to refer
            // to the remote object that implements the "Hello"
            // interface
            
            Hello obj = (Hello) registry.lookup("HelloServer");
            
            clientKerberosTicket = this.getTicket(username, password);

            String message = "blank";
            message = obj.sayHello(clientKerberosTicket);
            System.out.println(message+"\n");
            return message;
        } catch (Exception e) {
            System.out.println("HelloClient exception: " + e.getMessage());
            e.printStackTrace();
        }
		return "remote Method Failed";
    	
    }
    public String getTicket(String username, String password) {
    	KerberosClient kerberosClient 	= new KerberosClient();
    	String ticket 					= 	kerberosClient.getKerberosTicket(username, password);
    	return ticket;
    }
}

