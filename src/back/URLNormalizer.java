package back;
import java.io.Serializable;

import com.sun.jndi.toolkit.url.Uri;

import java.net.MalformedURLException;
import java.net.URL;

public class URLNormalizer extends Object implements Serializable{
	public StringBuilder Url;
	
	public URLNormalizer(String url){
		Url = new StringBuilder(url);
	}
	
	
	/* normalize
	 * 1-Convert upper case letters to upper case.
	   Example:
       HTTP://www.Example.com/ -> http://www.example.com/
	 * 2-Capitalizing letters in escape sequences.
	   Example:
       http://www.example.com/a%c2%b1b -> http://www.example.com/a%C2%B1b
	 * 3-Decode unreserved characters.
	   Example:
       http://www.example.com/%7Eusername/ -> http://www.example.com/~username/
	 * 4-Removing the default port.
	   Example:
       http://www.example.com:80/bar.html -> http://www.example.com/bar.html
     * 5-Removing duplicate slashes.
       Example:
       http://www.example.com/foo//bar.html -> http://www.example.com/foo/bar.html
     * 6-Removing the fragment.
       Example:
       http://www.example.com/bar.html#section1 -> http://www.example.com/bar.html
     * 7-Adding trailing
       http://www.example.com/alice -> http://www.example.com/alice/
     * 8-Removing default query parameters
       http://www.example.com/display?id=&sort=ascending -> http://www.example.com/display
     * 9-Removing the "?" when the query is empty
       http://www.example.com/display? -> http://www.example.com/display
     

	 */
	public String normalize(){
		
		
		/*URL aURL = null;
        try {
            aURL = new URL(Url.toString());
        } catch (MalformedURLException e) {

        }
        String host = aURL.getHost().toString();
		
        for(int ii=0; ii<host.length() ;ii++  )
        {
        	
        	host.setCharAt(ii, Character.toLowerCase(Url.charAt(ii)));
        	
        }*/
		for(int i=0;i<Url.length()-1;i++){
			
			Character c1 = Url.charAt(i);
			Character c2 = Url.charAt(i+1);
			Character lc = Url.charAt(Url.length()-1);
			//1
			
			
			
			
			//2
			if(c1.equals('%') && !Character.isDigit(c2)){
				Url.setCharAt(i+1, Character.toUpperCase(c2));
				i++;
			}
			//3
			if(c1.equals('%') && Character.isDigit(c2)){
				Url.replace(i, i+3, "~");
			}
			//4
			if(c1.equals(':') && Character.isDigit(c2)){
				Url.delete(i, i+3);
				i-=3;
			}
			//5
			if(c1.equals('/') && c2.equals('/') && i>6){
				Url.delete(i, i+1);
				i--;
			}
			//6
			if(c1.equals('#')){
				Url.delete(i, Url.length());	
			}
			//7
			if(lc.equals('/')){
				Url.delete(Url.length()-1, Url.length());
			}
			//8 & 9
			if(c1.equals('?')){
				Url.delete(i, Url.length());
			}
			//
		}
		return Url.toString();
	}
	

}
