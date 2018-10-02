package au.edu.uts.doccomm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Mix on 27/8/18.
 */

public class FetchMapData {

    public String urlToRead(String urlPlaceholder) throws IOException {
        String Data ="";
        InputStream inputstream = null;
        HttpURLConnection httpURLConnection = null;

        try {

            URL url = new URL(urlPlaceholder);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();
            inputstream=httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputstream));
            StringBuffer stringBuffer = new StringBuffer();

            String line = "";

            while((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line);
            }
            Data = stringBuffer.toString();
            bufferedReader.close();

        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        finally {
            inputstream.close();
            httpURLConnection.disconnect();

        }
        return Data;
    }

}
