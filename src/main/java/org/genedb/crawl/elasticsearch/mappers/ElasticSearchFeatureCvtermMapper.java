package org.genedb.crawl.elasticsearch.mappers;

import java.util.List;

import org.genedb.crawl.mappers.FeatureCvtermMapper;
import org.genedb.crawl.model.Dbxref;
import org.genedb.crawl.model.Organism;
import org.genedb.crawl.model.Pub;
import org.springframework.stereotype.Component;

@Component
public class ElasticSearchFeatureCvtermMapper extends ElasticSearchBaseMapper implements FeatureCvtermMapper {

	@Override
	public List<Pub> featureCvTermPubs(int feature_cvterm_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Dbxref> featureCvTermDbxrefs(int feature_cvterm_id) {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public Integer countInOrganism(Organism organism, String cv, String cvterm) {
        // TODO Auto-generated method stub
        return null;
    }

	
//	public static String getIndex() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	
//	public static String getType() {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
