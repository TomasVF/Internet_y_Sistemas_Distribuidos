package eventoservice;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class EventoServiceFactory {
    private final static String CLASS_NAME_PARAMETER = "EventoServiceFactory.className";
    private static EventoService service = null;

    private EventoServiceFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static EventoService getInstance() {
        try {
            String serviceClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class serviceClass = Class.forName(serviceClassName);
            return (EventoService) serviceClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static EventoService getService() {

        if (service == null) {
            service = getInstance();
        }
        return service;

    }
}
