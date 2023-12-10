package evento;
import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class SqlEventoDaoFactory {
    private final static String CLASS_NAME_PARAMETER = "SqlEventoDaoFactory.className";
    private static SqlEventoDao dao = null;

    private SqlEventoDaoFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static SqlEventoDao getInstance() {
        try {
            String daoClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class daoClass = Class.forName(daoClassName);
            return (SqlEventoDao) daoClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static SqlEventoDao getDao() {

        if (dao == null) {
            dao = getInstance();
        }
        return dao;

    }

}
