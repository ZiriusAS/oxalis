package itella.service.client;

import itella.api.PrintInvoiceDTO;

import itella.api.SmsDTO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author vasanthis
 */
public class PrintServiceClient {
    
    private static final String CONTENT_TYPE = "application/json";
    private static final String REALM_NAME = "Print(Itella) Service Authentication";
    private static String PRINT_SERVER_URL = "";
    private static final String EQUAL = "=";
    private static final String LOCALE = "locale";
    private static final String IS_PRODUCTION_ENV = "isProductionEnvironment";

    
    private static PrintServiceClient instance;
    
    public static PrintServiceClient getInstance() {
        
        if (instance == null) {
            instance = new PrintServiceClient();
        }
        synchronized (PrintServiceClient.class) {
            return createSingleton();
        }
    }
    
    private static PrintServiceClient createSingleton() {
        return instance = new PrintServiceClient();
    }
    
    public void initialize(String clientURL) {
        PRINT_SERVER_URL = clientURL;
    }

    private PostMethod getHttpPostMethod() {

        PostMethod httpPost = new PostMethod(PRINT_SERVER_URL);
        return httpPost;
    }

    private HttpClient getHttpClient(String userName, String password) {

        HttpClient httpClient = new HttpClient();
        httpClient.getState().setCredentials(
                new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, REALM_NAME),
                new UsernamePasswordCredentials(userName, password));

        return httpClient;
    }

    private String getTextMessage(InputStream is) throws IOException {

        StringWriter writer = new StringWriter();
        IOUtils.copy(is, writer);
        return writer.toString();
    }    

    public String sendToPrintService(PrintInvoiceDTO printInvoiceDTO, String locale,
            boolean isProductionEnvironment, String userName, String Password) throws IOException, Exception {

        ObjectMapper mapper = new ObjectMapper();
        HttpClient httpClient = getHttpClient(userName, Password);
        PostMethod httpPost = getHttpPostMethod();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        mapper.writeValue(baos, printInvoiceDTO);
        httpPost.setQueryString(URIUtil.encodeQuery(LOCALE + EQUAL + locale) +  "&" + IS_PRODUCTION_ENV + EQUAL + isProductionEnvironment);

        try {
            RequestEntity requestEntity = new InputStreamRequestEntity(new ByteArrayInputStream(baos.toByteArray()), CONTENT_TYPE);

            httpPost.setRequestEntity(requestEntity);
            int status = httpClient.executeMethod(httpPost);

            if (status == HttpStatus.SC_OK) {
                return getTextMessage(httpPost.getResponseBodyAsStream());
            }
            throw new Exception(getTextMessage(httpPost.getResponseBodyAsStream()));
        } finally {
            httpPost.releaseConnection();
        }
    } 

    /**
     * Send sms.
     * 
     * @param smsDTO
     *            the sms dto
     * @param userName
     *            the user name
     * @param Password
     *            the password
     * @return the string
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws Exception
     *             the exception
     */
    public String sendSms(SmsDTO smsDTO, String userName, String password) throws IOException, Exception {

        ObjectMapper mapper = new ObjectMapper();
        HttpClient httpClient = getHttpClient(userName, password);
        PostMethod httpPost = getHttpPostMethod();

        smsDTO.setUserId(userName);        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        mapper.writeValue(baos, smsDTO);

        try {
            RequestEntity requestEntity = new InputStreamRequestEntity(new ByteArrayInputStream(baos.toByteArray()), CONTENT_TYPE);

            httpPost.setRequestEntity(requestEntity);
            int status = httpClient.executeMethod(httpPost);

            if (status == HttpStatus.SC_OK) {
                return getTextMessage(httpPost.getResponseBodyAsStream());
            }
            throw new Exception(getTextMessage(httpPost.getResponseBodyAsStream()));
        } finally {
            httpPost.releaseConnection();
        }
    }
}