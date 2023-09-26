package pos.PlaylistV2.Model.Util;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class Config {
    @Bean
    public Jaxb2Marshaller marshaller(){
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setContextPath("com.example.consumingwebservice.wsdl");
        return jaxb2Marshaller;
    }
    @Bean
    public SoapClient soapClient(Jaxb2Marshaller jaxb2Marshaller){
        SoapClient soapClient = new SoapClient();
        soapClient.setDefaultUri("http://127.0.0.1:8000/?wsdl");
        soapClient.setMarshaller(jaxb2Marshaller);
        soapClient.setUnmarshaller(jaxb2Marshaller);
        return soapClient;
    }
}
