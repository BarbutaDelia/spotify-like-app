package pos.PlaylistV2.Model.Util;

import com.example.consumingwebservice.wsdl.Authorize;
import com.example.consumingwebservice.wsdl.AuthorizeResponse;
import com.example.consumingwebservice.wsdl.ObjectFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class SoapClient extends WebServiceGatewaySupport {
    public String AuthorizeUser(String token) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Authorize.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        Authorize request = new Authorize();

        ObjectFactory objectFactory = new ObjectFactory();
        JAXBElement<String> jaxbElement = objectFactory.createAuthorizeToken(token);
        request.setToken(jaxbElement);
        JAXBElement<Authorize> auth = objectFactory.createAuthorize(request);

        JAXBElement<AuthorizeResponse> authorizeResponse = (JAXBElement<AuthorizeResponse>) getWebServiceTemplate().marshalSendAndReceive("http://127.0.0.1:8000", auth, null);

        return authorizeResponse.getValue().getAuthorizeResult().getValue();
    }
}
