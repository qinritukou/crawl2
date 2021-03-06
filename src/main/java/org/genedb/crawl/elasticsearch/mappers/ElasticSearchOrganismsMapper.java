package org.genedb.crawl.elasticsearch.mappers;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.genedb.crawl.mappers.OrganismsMapper;
import org.genedb.crawl.model.Organism;
import org.genedb.crawl.model.Property;
import org.springframework.stereotype.Component;

@Component
public class ElasticSearchOrganismsMapper extends ElasticSearchBaseMapper implements OrganismsMapper {
	
	private Logger logger = Logger.getLogger(ElasticSearchOrganismsMapper.class);
	
	private int getTotalOrganisms() {
		
		CountResponse cr = connection.getClient()
		 	.prepareCount(connection.getIndex())
		 	.setTypes(connection.getOrganismType())
		 	.setQuery( QueryBuilders.matchAllQuery())
		 	.execute()
	        .actionGet();
		
		long count = cr.count();
		
		return (int) count;
	}
	
	
	public List<Organism> list() {
		
	    logger.info("Fetching organisms from " + connection);
	    
		SearchResponse response = connection.getClient()
			.prepareSearch(connection.getIndex())
			.setTypes(connection.getOrganismType())
			.setQuery(QueryBuilders.matchAllQuery())
			.setSize(getTotalOrganisms())
			.execute()
			.actionGet();
		
		return getAllMatches(response, Organism.class);
		
	}

	
	public Organism getByID(int ID) {
		Organism o = (Organism) this.getFirstMatch(connection.getIndex(), connection.getOrganismType(), "ID", String.valueOf(ID), Organism.class);
		logger.info(o);
		return o;
	}

	
	public Organism getByTaxonID(String taxonID) {
		return (Organism) this.getFirstMatch(connection.getIndex(), connection.getOrganismType(), "taxonID", taxonID, Organism.class);
	}

	
	public Organism getByCommonName(String commonName) {
		return (Organism) this.getFirstMatch(connection.getIndex(), connection.getOrganismType(), "common_name", commonName, Organism.class);
	}

	
	public Property getOrganismProp(Organism organism, String cv, String cvterm) {
	    if (organism.properties == null)
	        return null;
	    
	    for (Property property : organism.properties) {
	        
	        if (! property.name.equals(cvterm))
	            continue;
	        
	        if (cv == null || property.type.name.equals(cv))
	            return property;
	        
	    }
		return null;
	}
	
	@Override
    public List<Property> getOrganismProps(Organism organism, String cv) {
	    if (cv == null)
	        return organism.properties;
        List<Property> properties = new ArrayList<Property>();
        for (Property property : organism.properties) {
            if (property.type.name.equals(cv))
                properties.add(property);
        }
        return properties;
    }
	
    public void createOrUpdate(Organism organism) {

        //String source = jsonIzer.toJson(organism);
        //logger.info(source);

        logger.info(String.format("Storing organism as %s in index %s and type %s", connection.getIndex(), connection.getOrganismType(), organism.common_name));

        // connection.getClient().prepareIndex(connection.getIndex(),
        // connection.getOrganismType(),
        // organism.common_name).setSource(source).execute().actionGet();

        createOrUpdate(connection.getIndex(), connection.getOrganismType(), organism.common_name, organism);
        
    }

    


}
