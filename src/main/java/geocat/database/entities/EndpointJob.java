package geocat.database.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

//create table endpoint_job ( endpoint_job_id varchar(40), harvest_job_id varchar(40), state varchar(40), url varchar(255), url_get_records varchar(255), expected_number_of_records int, filter text,look_for_nested_discovery_service bool);
@Entity
@Table(name="endpoint_job",
    indexes= {
        @Index(
                name="harvestJobId_idx",
                columnList="harvestJobId",
                unique=false
        )
    })
public class EndpointJob {

    @Id
    private String endpointJobId;
    private String harvestJobId;
    private String url;
    private boolean lookForNestedDiscoveryService;
    private String filter;
    private Integer expectedNumberOfRecords;
    private String urlGetRecords;
    private String state;

    public boolean isLookForNestedDiscoveryService() {
        return lookForNestedDiscoveryService;
    }

    public void setLookForNestedDiscoveryService(boolean lookForNestedDiscoveryService) {
        this.lookForNestedDiscoveryService = lookForNestedDiscoveryService;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Integer getExpectedNumberOfRecords() {
        return expectedNumberOfRecords;
    }

    public void setExpectedNumberOfRecords(int expectedNumberOfRecords) {
        this.expectedNumberOfRecords = expectedNumberOfRecords;
    }

    public String getUrlGetRecords() {
        return urlGetRecords;
    }

    public void setUrlGetRecords(String urlGetRecords) {
        this.urlGetRecords = urlGetRecords;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEndpointJobId() {
        return endpointJobId;
    }

    public void setEndpointJobId(String endpointJobId) {
        this.endpointJobId = endpointJobId;
    }

    public String getHarvestJobId() {
        return harvestJobId;
    }

    public void setHarvestJobId(String harvestJobId) {
        this.harvestJobId = harvestJobId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
