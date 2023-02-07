import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException {
        String hostName = "localhost";
        String portNum = "9999";
        //Аргумент для вызываемой
        String[] argsForOperations = new String[]{""};
        String BEAN_NAME = "org.springframework.boot:type=Endpoint,name=Health";
        //Вызываемая операция
        String OPERATION_FOR_RUN = "health";
        JMXConnector connector = null;

        String jmxString = "";

        try {
            JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + hostName + ":" + portNum +  "/jmxrmi");
            //JMXServiceURL url = new JMXServiceURL(jmxString);
            connector = JMXConnectorFactory.connect(url);
            MBeanServerConnection remote = connector.getMBeanServerConnection();

            ObjectName bean = new ObjectName(BEAN_NAME);
            MBeanInfo info = remote.getMBeanInfo(bean);
            final MBeanOperationInfo[] operations = info.getOperations();
            final MBeanOperationInfo mBeanOperationInfo = Arrays.stream(operations)
                    .filter(it -> it.getName().equals(OPERATION_FOR_RUN))
                    .findFirst()
                    .orElse(null);

            if (Objects.isNull(mBeanOperationInfo)) {
                System.out.println("Operation " + OPERATION_FOR_RUN + " not found!");
                return;
            }

            String  operationSignature[] = {String[].class.getName()};

            //Вызов операции
            //System.out.println(remote.invoke(bean, OPERATION_FOR_RUN, new Object[] {argsForOperations}, operationSignature));
            System.out.println(remote.invoke(bean, OPERATION_FOR_RUN, null, operationSignature));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (Objects.nonNull(connector)) {
                connector.close();
            }
        }

    }
}