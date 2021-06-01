package geocat.csw.csw;


import geocat.csw.http.IHTTPRetriever;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Scope("prototype")
public class CSWEngine {

    public static String GETCAP_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<csw:GetCapabilities xmlns:csw=\"http://www.opengis.net/cat/csw/2.0.2\" service=\"CSW\">\n" +
            "<ows:AcceptVersions xmlns:ows=\"http://www.opengis.net/ows\">\n" +
            "<ows:Version>2.0.2</ows:Version>\n" +
            "</ows:AcceptVersions>\n" +
            "<ows:AcceptFormats xmlns:ows=\"http://www.opengis.net/ows\">\n" +
            "<ows:OutputFormat>application/xml</ows:OutputFormat>\n" +
            "</ows:AcceptFormats>\n" +
            "</csw:GetCapabilities>\n";
    public static String GETCAP_KVP = "request=GetCapabilities&service=CSW&acceptVersions=2.0.2&acceptFormats=application%2Fxml";
    @Autowired
    @Qualifier("cookieAttachingRetriever")
    IHTTPRetriever retriever;
    @Autowired
    OGCFilterService ogcFilterService;

    //----
    public String GetCapabilities(String url) throws Exception {
        try {
            return GetCapabilitiesPOST(url);
        } catch (IOException e) {
            return GetCapabilitiesGET(url);
        }
    }

    protected String GetCapabilitiesPOST(String url) throws Exception {
        return retriever.retrieveXML("POST", url, GETCAP_XML, null);
    }

    protected String GetCapabilitiesGET(String url) throws Exception {
        if (url.endsWith("?"))
            url += GETCAP_KVP;
        else if (!url.contains("?"))
            url += "?" + GETCAP_KVP;
        //otherwise, likely already has the request=GetCapabilities in it!
        return retriever.retrieveXML("GET", url, null, null);
    }
    //--

    public String GetRecords(String url, String requestXML) throws Exception {
        return retriever.retrieveXML("POST", url, requestXML, null);
    }


}
