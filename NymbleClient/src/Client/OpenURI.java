/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

/**
 *
 * @author Inbo
 */
import java.net.URI;
import java.awt.Desktop;

public class OpenURI {

    java.awt.Desktop desktop = java.awt.Desktop.getDesktop();

    
    //String iexppath ="\"C:\\Program Files\\Internet Explorer\\iexplore.exe\"";
 // iexppath ="\"C:\\Users\\Zahir\\AppData\\Local\\Google\\Chrome\\Application\\chrome.exe\"";
 String iexppath ="\"C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe\"";
 //String iexppath="\"C:\\Program Files\\Mozilla Firefox\\firefox.exe\"";
    
    public OpenURI(String str) {

        try {
            System.err.println("in open uri");
//            java.net.URI uri = new java.net.URI(str);
//            desktop.browse(uri);
//            System.err.println("opened");
            
            Runtime r = Runtime.getRuntime();
            r.exec(iexppath+" "+str);
            
        } catch (Exception e) {
e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        new OpenURI("http://192.168.1.4:8080/NymbleClientLogin/index.jsp");
    //     new OpenURI("http://www.google.com");
    }
}