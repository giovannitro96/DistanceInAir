package it.unimi.distanceinair.server.service.callback;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.soap.SoapMessage;

public final class ClientMessageCallBack
        implements WebServiceMessageCallback {

    /**the soapAction to be appended to the soap message.*/
    private final String soapAction;

    /**constructor.
     * @param action the soapAction to be set.*/
    public ClientMessageCallBack(final String action) {
        this.soapAction = action;
    }



    @Override
    public void doWithMessage(WebServiceMessage webServiceMessage) {
        if (webServiceMessage instanceof SoapMessage soapMessage) {
            soapMessage.setSoapAction(soapAction);
        }

    }
}