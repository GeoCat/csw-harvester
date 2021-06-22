package geocat.routes.rest;

import geocat.model.HarvesterConfig;
import geocat.service.GetStatusService;
import org.apache.camel.BeanScope;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.stereotype.Component;


@Component
public class GetStatus extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        restConfiguration().component("jetty").host("localhost").port(9999);

        JacksonDataFormat jsonDefHarvesterConfig = new JacksonDataFormat(HarvesterConfig.class);

        //--- incoming start process request (HTTP)
        rest("/api/getstatus/")
                .get("/{processID}")
                .route()
                .routeId("rest.rest.getstatus")
                .bean(GetStatusService.class, "getStatus( ${header.processID} )", BeanScope.Request)

                .setHeader("content-type", constant("application/json"))
                .marshal().json()

        ;
    }
}