package geocat.config;

import geocat.dblogging.MYUnitOfWorkFactory;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.ExtendedCamelContext;
import org.apache.camel.component.activemq.ActiveMQComponent;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.jms.ConnectionFactory;


@Configuration
public class Config {


    String brokerUrl = "tcp://localhost:61616?jms.prefetchPolicy.queuePrefetch=1";

    int maxConnections = 11;

    @Autowired
    CamelContext camelContext;

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL(brokerUrl);
        org.apache.activemq.RedeliveryPolicy redeliveryPolicy = factory.getRedeliveryPolicy();
        redeliveryPolicy.setMaximumRedeliveries(3);
        redeliveryPolicy.setRedeliveryDelay(3000);
        redeliveryPolicy.setInitialRedeliveryDelay(3000);
        redeliveryPolicy.setMaximumRedeliveryDelay(5000);
        return factory;
    }

    //    @Bean
//    public JmsTransactionManager jmsTransactionManager(ActiveMQConnectionFactory connectionFactory) {
//        JmsTransactionManager jmsTransactionManager = new JmsTransactionManager();
//        jmsTransactionManager.setConnectionFactory(connectionFactory);
//        return jmsTransactionManager;
//    }

    @Bean
    public CamelContextConfiguration contextConfiguration() {
        return new CamelContextConfiguration() {
            @Override
            public void beforeApplicationStart(CamelContext context) {
                context.setUseMDCLogging(true);
                context.adapt(ExtendedCamelContext.class).setUnitOfWorkFactory(new MYUnitOfWorkFactory());
            }

            @Override
            public void afterApplicationStart(CamelContext camelContext) {
            }
        };
    }


    @Bean
    //@Primary
    public ActiveMQComponent activemq(ActiveMQConnectionFactory connectionFactory) {

        PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory(connectionFactory);
        pooledConnectionFactory.setMaxConnections(50);

         JmsTransactionManager jmsTransactionManager = new JmsTransactionManager();
        jmsTransactionManager.setConnectionFactory(pooledConnectionFactory);

        ActiveMQComponent activeMQComponent = new ActiveMQComponent();
        activeMQComponent.setConnectionFactory(pooledConnectionFactory);
        activeMQComponent.setTransacted(true);
        activeMQComponent.setTransactedInOut(true);
        activeMQComponent.setTransactionManager(jmsTransactionManager);

        //  activeMQComponent.setLazyCreateTransactionManager (false);

        activeMQComponent.setCacheLevelName("CACHE_NONE");
         activeMQComponent.setAcknowledgementModeName("SESSION_TRANSACTED");


        return activeMQComponent;
    }

//    @Bean
//    public CamelContext camelContext(){
//        CamelContext ctx = new DefaultCamelContext();
//       // ctx.setMessageHistory(true);
//        return ctx;
//    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager dbTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();

        return transactionManager;
    }

}
