package es.udc.ws.app.client.service;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

import java.lang.reflect.InvocationTargetException;

public class ClientEventoServiceFactory {
    private final static String CLASS_NAME_PARAMETER
            = "ClientEventoServiceFactory.className";
    private static Class<ClientEventoService> serviceClass = null;

    private ClientEventoServiceFactory() {
    }

    @SuppressWarnings("unchecked")
    private synchronized static Class<ClientEventoService> getServiceClass() {

        if (serviceClass == null) {
            try {
                String serviceClassName = ConfigurationParametersManager
                        .getParameter(CLASS_NAME_PARAMETER);
                serviceClass = (Class<ClientEventoService>) Class.forName(serviceClassName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return serviceClass;

    }

    public static ClientEventoService getService() {

        try {
            return (ClientEventoService) getServiceClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }
}
